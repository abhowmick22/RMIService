package rmiservice.rmi.server;

/*
 * The arguments it gets are follows :
 * javac yourRMI InitialClassname registryHost registryPort
 * InitialClassName is the application server class
 * One object/one serviceName per application server class
 * OR 
 * java yourRMI registryHost registryPort servicename1(eg. ZipCodeServer)
 * Assume all InitialClassNames are known at start, and it binds an object of each type
 * However, in the future - let it take a list of available InitialClassNames as an argument
 */

/* This does not offer the code of the whole communication module 
(CM) for RMI: but it gives some hints about how you can make 
it. I call it simply "yourRMI". 

For example, it  shows how you can get the dispatcherHost name etc.,
(well you can hardwire it if you like, I should say),
or how you can make a class out of classname as a string.

This just shows one design option. Other options are
possible. We assume there is a unique skeleton for each
remote object class (not object) which is called by CM 
by static methods for unmarshalling etc. We can do without
it, in which case CM does marshalling/unmarhshalling.
Which is simpler, I cannot say, since both have their
own simpleness and complexity.
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

import rmiservice.rmi.comm.ClientRmiMsg;
import rmiservice.rmi.comm.RegistryMsg;
import rmiservice.rmi.comm.RemoteException;
import rmiservice.rmi.comm.RemoteObjectRef;

public class yourRMI
{
    
    public static void main(String args[])    
    {
        if(args.length < 4){
        	System.out.println("Enter atleast three arguments: <LocalMachineName> <DispatcherPort> <RegistryPort> <ServiceName(s)>");
        	System.exit(0);
        }
        String dispatcherHost = args[0];
        String registryHost = dispatcherHost;
        int dispatcherPort = Integer.parseInt(args[1]);
        int registryPort = Integer.parseInt(args[2]);
        ArrayList<String> serviceNames = new ArrayList<String>();
        for (int i = 3; i< args.length; i++) {
        	serviceNames.add(args[i]);        	
        }
        
        //table that maps a service (associated with a remote object) with the local object
        RORtbl tbl = new RORtbl();
        
        try {		
            // Start a RegistryService thread - in the future this can be a process in a different JVM
            RegistryService regService = new RegistryService(registryPort);
            Thread regServiceThread = new Thread(regService);
            regServiceThread.start();        
            // TODO : better way to check if it is up ?
            //wait till the registry is up and running
            while(!regServiceThread.isAlive());
        
            // Instantiate objects of every serviceName, create the map RORtbl and bind these services
            int objkey = 0;
            for (String objectName : serviceNames){
    
            	Socket registrySocket = new Socket(registryHost, registryPort);
            	registrySocket.setTcpNoDelay(true);
            	ObjectOutputStream toRegistry = new ObjectOutputStream(registrySocket.getOutputStream());
            	ObjectInputStream fromRegistry = new ObjectInputStream(registrySocket.getInputStream());
            	
            	Class<?> initialclass = Class.forName(objectName + "_Impl");	// gives you ZipCodeServer_Impl for example
            	boolean checkInterfaceExists = false; 
            	for(Class<?> inter : initialclass.getInterfaces()) {
                	for(Class<?> inter2 : inter.getInterfaces()) {
                	    if(inter2.getName().equals("rmiservice.rmi.comm.YourRemote")){
        	    			checkInterfaceExists = true;
            	        	break;
            	    	}
            		}
            	}
            	if(!checkInterfaceExists) {
            	    //the service does not extent YourRemote interface, so can't instantiate 
            	    //it and add to registry
            	    System.out.println(objectName + " does not extend YourRemote interface and "
            	            + "hence cannot be instantiated and bound to registry.");
            	    continue;
            	}
            	
            	Object obj = initialclass.newInstance();
            	objkey++;
            	tbl.addObj(objkey, obj);
            	            	
            	String[] parts = objectName.split("\\.");
            	String name = parts[parts.length-1];
            	RemoteObjectRef ror = new RemoteObjectRef(dispatcherHost, dispatcherPort, objkey, name);
            	
            	RegistryMsg drm = new RegistryMsg();
            	drm.message = "bind";
            	
            	drm.serviceName = name;
            	drm.refObject = ror;
            	toRegistry.writeObject(drm);
            	toRegistry.flush();
            
            	registrySocket.shutdownOutput();
            	fromRegistry.readObject();     //ACK
            	registrySocket.shutdownInput();
            	//registrySocket.close();  //TODO: why not do this?         	        
            }      
     	} catch (Exception e) {
			System.out.println("An exception occured while binding services to the registry:");
			e.printStackTrace();
		} 
 
		Integer tid = 0;
		Socket client = null;
		
		// socket to listen for RMI requests
		ServerSocket serverSoc = null;
		try {
			serverSoc = new ServerSocket(dispatcherPort);
		} catch (IOException e) {
			System.out.println("Exception while creating the server socket:");
			e.printStackTrace();
			System.exit(0);
		}
		
        while (true)
        {
            // (1) receives an invocation request.
            // (2) creates a socket and input/output streams.
            // (3) gets the invocation, in marshaled form.
            // (4) gets the real object reference from tbl.
            // (5) unmarshalls directly and invokes that object directly.            
        	try {
				client = serverSoc.accept();
	        	ClientRmiMsg msg;
				msg = (ClientRmiMsg) new ObjectInputStream(client.getInputStream()).readObject();
				Object obj = tbl.findObj(msg.obj_key);
				if(obj == null) {
				    //no object with this key found; shouldn't happen because the obj_key
				    //was derived from the ROR that the registry sent
				    RemoteException rex = new RemoteException("RemoteException was generated. No such object found.", RemoteException.class);
	                try {
	                    new ObjectOutputStream(client.getOutputStream()).writeObject(rex);
	                } catch (IOException e) {
	                    System.out.println("Following error occured while communicating with the client:");
	                    e.printStackTrace();
	                }
				}
				//create new thread to service client's request
				//tid is just for future use, for say a thread pool
				(new Thread(new RemoteObjThread(obj, msg, client), (tid++).toString())).start(); 
				//now the dispatcher will communicate with the client, and yourRMI can go back to 
				//accepting requests from other clients
	        	
			} catch (IOException e) {
			    //cannot communicate with client
				System.out.println("Following I/O error occured while servicing client's request:");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {				
				RemoteException rex = new RemoteException("RemoteException was generated.", e.getClass());
				try {
					new ObjectOutputStream(client.getOutputStream()).writeObject(rex);
				} catch (IOException e1) {
				    //cannot communicate with client.
	                System.out.println("Following I/O error occured while servicing client's request:");
	                e.printStackTrace();
				}
			}			
        }
    }    
}