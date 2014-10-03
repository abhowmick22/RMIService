package rmiservice.rmi.server;

/*
 * [Neil]
 * This class basically acts as the dispatcher on the server. 
 * According to our implementation, it should continuously listen to requests by different clients.
 * It maintains an RORtable so that a new request from client must contain only the string that 
 * describes the class reference, eg. "computePI" to reference ComputePi.class. This table
 * does the mapping.
 * Now, the reference to ComputePi that the client obtained should contain:
 *  1. The host and port of the yourRMI service
 *  2. The string that describes the class that the client wants to use. So client will get
 *      a reference to the ComputePi object, and will send back the "computePI" string to
 *      yourRMI service. This is a bit redundant, but seems logical right now.
 *  
 */

/*
 * The arguments it gets are follows :
 * javac yourRMI InitialClassname registryHost registryPort
 * InitialClassName is the application server class
 * One object/one serviceName per application server class
 * OR 
 * javac yourRMI registryHost registryPort servicename1
 * Assume all InitialClassNames are known at start, and it binds an object of each type
 * However, in the future - let it take a list of available InitialClassNames as an argument
 */

/* This does not offer the code of the whole communication module 
(CM) for RMI: but it gives some hints about how you can make 
it. I call it simply "yourRMI". 

For example, it  shows how you can get the host name etc.,
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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class yourRMI
{
    static String host;
    static int port;
    static ArrayList<String> serviceNames = new ArrayList<String>();

    // It will use a hash table, which contains ROR together with
    // reference to the remote object.
    // As you can see, the exception handling is not done at all.
    public static void main(String args[])    
    throws Exception
    {
        String registryHost = args[0];
        int registryPort = Integer.parseInt(args[1]);
        serviceNames.add(args[2]);
    
		// List of serviceNames, known at compile time for now
        
        // The RMI dispatcher is available on famous port 12345
        host = (InetAddress.getLocalHost()).getHostName();
        port = 12345;
        
        // Start a RegistryService thread - in the future this can be a process in a different JVM
        Thread regService = new Thread(new RegistryService());
        regService.start();
        
        // Get hold of in/out streams for communicating with registry
        Socket s = new Socket(registryHost, registryPort);
        ObjectOutputStream toRegistry = new ObjectOutputStream(s.getOutputStream());
        //ObjectInputStream fromRegistry = new ObjectInputStream(s.getInputStream());
        
        // Instantiate objects of every serviceName, create the map RORtbl and bind these services
        Class<?> initialclass;
        Object o;
        RORtbl tbl = new RORtbl();
        Integer objkey = 0;
        for (String objectName : serviceNames){
        	initialclass = Class.forName(objectName + "Impl");	// gives you ZipCodeServerImpl
        	o = initialclass.newInstance();
        	tbl.addObj(o, objkey.toString());
        	objkey++;
        	
        	RemoteObjectRef ror = new RemoteObjectRef(host, port, objkey, objectName);
        	
        	RegistryMsg drm = new RegistryMsg();
        	drm.message = "bind";
        	drm.serviceName = objectName;
        	drm.refObject = ror;
        	toRegistry.writeObject(drm);
        }      
 
        // it now have two classes from MainClassName: 
        // (1) the class itself (say ZipCodeServerImpl) and
        // (2) its skeleton.
        //Class initialclass = Class.forName(InitialClassName);
        //Class initialskeleton = Class.forName(InitialClassName+"_skel");
 

        // create a socket to listen for RMI requests.
        ServerSocket serverSoc = new ServerSocket(port);
        Integer tid = 0;

        // Now we go into a loop.
        // Look at rmiregistry.java for a simple server programming.
        // The code is far from optimal but in any way you can get basics.
        // Actually you should use multiple threads, or this easily
        // deadlocks. But for your implementation I do not ask it.
        // For design, consider well.
        while (true)
        {
            // (1) receives an invocation request.
            // (2) creates a socket and input/output streams.
            // (3) gets the invocation, in martiallled form.
            // (4) gets the real object reference from tbl.
            // (5) Either:
            //      -- using the interface name, asks the skeleton,
            //         together with the object reference, to unmartial
            //         and invoke the real object.
            //      -- or do unmarshalling directly and invokes that
            //         object directly.
            // (6) receives the return value, which (if not marshalled
            //     you should marshal it here) and send it out to the 
            //     the source of the invoker.
            // (7) closes the socket.
        	
        	Socket client = serverSoc.accept();
        	ClientRmiMsg msg = (ClientRmiMsg) new ObjectInputStream(client.getInputStream()).readObject();
        	Object obj = tbl.findObj(msg.obj_key);
        	new Thread(new RemoteObjThread(obj, msg, client), (tid++).toString()).start();
        	
        	// dispatcher's work is done hopefully
        }
    }

    
}