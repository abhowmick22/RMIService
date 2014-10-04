package rmiservice.rmi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


// This one listens for Registry requests and handles the SimpleRegistry directly
public class RegistryService implements Runnable{

	private int port;
	
	public RegistryService(int registryPort) {
		// TODO Auto-generated constructor stub
		this.port = registryPort;
	}

	@Override
	public void run() {
	
		Socket client;
		ObjectInputStream inStream;
		ObjectOutputStream outStream;
		SimpleRegistry rs = new SimpleRegistry();
		ServerSocket server = null;

		try {
			server = new ServerSocket(this.port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true){
			try {
			client = server.accept();
			inStream = new ObjectInputStream(client.getInputStream());
			outStream = new ObjectOutputStream(client.getOutputStream());
			RegistryMsg rrm = (RegistryMsg) inStream.readObject();
			String mesg = rrm.message;
			
			if (mesg.equals("Who are you?")){
				String reply = "I am a simple registry.";
				outStream.writeObject(reply);
			}
			
			else if (mesg.equals("lookup")){
				String serviceName = rrm.serviceName;
				// return the ROR to client
				RemoteObjectRef ror = rs.lookup(serviceName);
				outStream.writeObject(ror);
			}
			
			else if (mesg.equals("bind")){
				// bing ROR to service name
				String serviceName = rrm.serviceName;
				RemoteObjectRef ror = rrm.refObject;
				rs.bind(serviceName, ror);
			}
			
			else {
				String reply = "Invalid Request.";
				outStream.writeObject(reply);
			}
			
			}
			catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
