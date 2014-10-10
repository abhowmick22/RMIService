package rmiservice.rmi.server;
/**
 * RegistryService accepts registry requests. It accepts requests from a client to lookup a remote
 * object reference (ROR) in the registry, and from the server to bind, unbind or rebind an ROR to the registry.
 * It runs in a separate thread and communications are done over a socket connection. 
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import rmiservice.rmi.comm.RegistryMsg;
import rmiservice.rmi.comm.RemoteObjectRef;

public class RegistryService implements Runnable{

	private int port;
	public SimpleRegistry rs;
	
	/**
	 * Constructor.
	 * @param registryPort Registry port.
	 * @throws IOException
	 */
	public RegistryService(int registryPort) {
		this.port = registryPort;				
		this.rs = new SimpleRegistry();
	}

	@Override
	public void run() {
	
	    ServerSocket server = null;
        try {
            server = new ServerSocket(this.port);
        }
        catch (IOException e1) {
            System.out.println("Following exception encountered while creating registry service.");
            e1.printStackTrace();
            return;
        }
		Socket client;
		ObjectInputStream inStream;
		ObjectOutputStream outStream; 		
		while(true){		
			try {
    			client = server.accept();    
    			inStream = new ObjectInputStream(client.getInputStream());
    			outStream = new ObjectOutputStream(client.getOutputStream());
    			RegistryMsg rrm = (RegistryMsg) inStream.readObject();
    			String mesg = rrm.message;
    			
    			if (mesg.equals("Who are you?")){
    			    //identify as a registry service
    				String reply = "I am a simple registry.";
    				outStream.writeObject(reply);
    			} else if (mesg.equals("lookup")){
    			    // lookup a service
    				String serviceName = rrm.serviceName;
    				// return the ROR to client
    				RemoteObjectRef ror = rs.lookup(serviceName);
    				outStream.writeObject(ror);
    			} else if (mesg.equals("bind")){
    				// bind ROR to service name
    				String serviceName = rrm.serviceName;
    				RemoteObjectRef ror = rrm.refObject;
    				rs.bind(serviceName, ror);
    				outStream.writeObject("ACK");
    			} else if (mesg.equals("rebind")) {
    			    // rebind ROR to new service name or vice versa
                    String serviceName = rrm.serviceName;
                    RemoteObjectRef ror = rrm.refObject;
                    rs.rebind(serviceName, ror);
                    outStream.writeObject("ACK");
    			} else if (mesg.equals("unbind")) {
    			    // unbind a service
                    String serviceName = rrm.serviceName;
                    rs.unbind(serviceName);
                    outStream.writeObject("ACK");
    			} else {
    				String reply = "Invalid Request.";
    				outStream.writeObject(reply);    				
    			}
    			
    			outStream.flush();
    			client.shutdownInput();
    			client.shutdownOutput();
    			
    		} catch (IOException e) {
    			System.out.println("Following IO Exception occured in the Registry Service:");
    			e.printStackTrace();
    		} catch (ClassNotFoundException e) {
                System.out.println("Registry Message class not found.");
                e.printStackTrace();
                System.out.println("No communication possible without this class. System will exit.");
                System.exit(0);
            }    			
		}
		
	}

}
