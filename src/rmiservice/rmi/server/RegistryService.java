package rmiservice.rmi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import rmiservice.rmi.comm.RegistryMsg;
import rmiservice.rmi.comm.RemoteObjectRef;

// This one listens for Registry requests and handles the SimpleRegistry directly
public class RegistryService implements Runnable{

	private int port;
	private ServerSocket server;
	SimpleRegistry rs;
	
	public RegistryService(int registryPort) {
		this.port = registryPort;
		try {
			this.server = new ServerSocket(this.port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.rs = new SimpleRegistry();
	}

	@Override
	public void run() {
	
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
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
    		}    			
		}
		
	}

}
