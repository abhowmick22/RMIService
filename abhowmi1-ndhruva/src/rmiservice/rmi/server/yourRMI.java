package rmiservice.rmi.server;
/**
 * The yourRMI class acts as the application server and dispatcher on the server side. It begins by
 * creating a thread which runs the registry service, that communicates with the registry.
 * It instantiates all the remote implementation classes of the user defined interfaces, and binds
 * the corresponding reference objects to the registry. It then acts as a server, listening to 
 * client requests for invoking a method on a server object. 
 * It unmarshalls client requests and creates a servant thread to invoke methods requested by the user.
 * It maintains a table that maps a unique object key to an actual object, the reference of which
 * is bound to the registry. The user can get this key from the reference and send it to this server
 * for identifying an object. 
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
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
            //wait till the registry is up and running
            System.out.println("Waiting for registry to be up...");
            while(!regServiceThread.isAlive());
            System.out.println("Registry is up!");
            
            int objkey = 0;
            // Instantiate objects of every serviceName, create the map RORtbl and bind these services                        
            for (String objectName : serviceNames){
    
            	Socket registrySocket = new Socket(registryHost, registryPort);
            	registrySocket.setTcpNoDelay(true);
            	ObjectOutputStream toRegistry = new ObjectOutputStream(registrySocket.getOutputStream());
            	ObjectInputStream fromRegistry = new ObjectInputStream(registrySocket.getInputStream());
            	
            	Class<?> initialclass = Class.forName("rmiservice.rmi.server."+objectName +"_Impl");	
            	        // gives you rmiservice.rmi.server.ZipCodeServer_Impl for example
            	boolean checkInterfaceExists = false; 
            	Class<?> yourRemoteInterface = null;
            	for(Class<?> inter : initialclass.getInterfaces()) {
                	for(Class<?> inter2 : inter.getInterfaces()) {
                	    if(inter2.getName().equals("rmiservice.rmi.comm.YourRemote")){
                	        yourRemoteInterface = inter;
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
            	} else {
            	    //now get all the methods for this interface and check if they all
                    //throw a RemoteException or java.lang.Exception (superclass)
                    boolean checkException = false;
                    for(Method met : yourRemoteInterface.getDeclaredMethods()) {
                        checkException = false;
                        for(Class<?> excType: met.getExceptionTypes()) {
                            if(excType.getName().equals("rmiservice.rmi.comm.RemoteException") ||
                                    excType.getName().equals("java.lang.Exception")) {
                                checkException = true;
                                break;
                            }
                        }
                        if(!checkException) {
                            System.out.println(met.getName() +" in " +objectName + " does not throw"
                                    + " RemoteException or java.lang.Exception. Hence "+ objectName
                                    + " cannot be bound to registry.");
                            break;
                        }
                    }
                    if(!checkException) {
                        //RemoteException or java.lang.Exception not thrown by some method, 
                        //so can't bind the class from this interface
                        continue;
                    }
            	}
            	
            	Object obj = initialclass.newInstance();
            	objkey++;  //unique object key
            	tbl.addObj(objkey, obj);   //add the object and object key to the rortable
            	            	
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
            	fromRegistry.readObject();     //ACK from registry service about successful bind
            	registrySocket.shutdownInput();            	         	       
            }      
     	} catch (Exception e) {
			System.out.println("An exception occured while binding services to the registry. Printing stack trace:");
			e.printStackTrace();
		} 
 
		Integer tid = 0;  //unique thread ID for future use (for say a thread pool)
		Socket client = null;
		// socket to listen for RMI requests
		ServerSocket serverSoc = null;
		try {
			serverSoc = new ServerSocket(dispatcherPort);
		} catch (IOException e) {
			System.out.println("Exception while creating the server socket:");
			e.printStackTrace();
			System.out.println("Exiting system.");
			System.exit(0);
		}
		
        while (true)
        {
            // (1) receives an invocation request.
            // (2) creates a socket and input/output streams.
            // (3) gets the invocation in marshaled form, and unmarshals it.
            // (4) gets the real object reference from tbl.
            // (5) creates servant thread to invoke the method on the real object.          
        	try {
				client = serverSoc.accept();
	        	ClientRmiMsg msg;
				msg = (ClientRmiMsg) new ObjectInputStream(client.getInputStream()).readObject();
				Object obj = tbl.findObj(msg.obj_key);
				if(obj == null) {
				    //no object with this key found; shouldn't happen because the obj_key
				    //was derived from the ROR that the registry sent
				    //but the user might have sent a faulty key; convey this to the user
				    RemoteException rex = new RemoteException("RemoteException was generated. No such object found.", RemoteException.class);
	                try {
	                    new ObjectOutputStream(client.getOutputStream()).writeObject(rex);
	                } catch (IOException e) {
	                    System.out.println("Following error occured while communicating with the client:");
	                    e.printStackTrace();
	                    continue;
	                }
				}
				
				//create new thread (servant) to service client's request
				//again, tid is just for future use, for say a thread pool				
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