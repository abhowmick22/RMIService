package rmiservice.rmi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


// This one listens for Registry requests and handles the SimpleRegistry directly
public class RegistryService implements Runnable{

	private int port;
	private ServerSocket server;
	SimpleRegistry rs;
	
	public RegistryService(int registryPort) {
		// TODO Auto-generated constructor stub
		this.port = registryPort;
		try {
			this.server = new ServerSocket(this.port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		rs = new SimpleRegistry();
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

				String reply = "I am a simple registry.";
				outStream.writeObject(reply);
				outStream.flush();
			}
			
			else if (mesg.equals("lookup")){
				String serviceName = rrm.serviceName;
				// return the ROR to client
				RemoteObjectRef ror = rs.lookup(serviceName);
				outStream.writeObject(ror);
				outStream.flush();
			}
			
			else if (mesg.equals("bind")){
				// bing ROR to service name
				//System.out.println("fdsfs");
				String serviceName = rrm.serviceName;
				RemoteObjectRef ror = rrm.refObject;
				rs.bind(serviceName, ror);
				outStream.writeObject("ACK");
			}
			
			else {
				String reply = "Invalid Request.";
				outStream.writeObject(reply);
				outStream.flush();
			}
			
			
			
			inStream.close();
			outStream.close();
			//client.close();
			
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
