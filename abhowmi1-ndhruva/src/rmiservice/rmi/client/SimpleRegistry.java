package rmiservice.rmi.client;
/**
 * SimpleRegistry on the client acts as a stub for the actual SimpleRegistry (on the server).
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import rmiservice.rmi.comm.RegistryMsg;
import rmiservice.rmi.comm.RemoteObjectRef;

public class SimpleRegistry
{
    private String regHost;
    private int regPort;
    
    public SimpleRegistry(String regHost, int regPort) {
        this.regHost = regHost;
        this.regPort = regPort;
    }
    
    /**
     * Communicates with the registry to look up a service.
     * @param serviceName The name of the service.
     * @return The remote object reference for the service.
     */
    public RemoteObjectRef lookup(String serviceName) {
        
        Socket clientSocket;
        RemoteObjectRef retValue;
        try {
            clientSocket = new Socket(this.regHost, this.regPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            RegistryMsg crm = new RegistryMsg();
            crm.message = "lookup";
            crm.serviceName = serviceName;
            outStream.writeObject(crm);
            outStream.flush();  //can't close outStream yet because it screws up the connection
            retValue = (RemoteObjectRef) inStream.readObject();                        
            inStream.close();
            outStream.close();
            clientSocket.close(); 
            if(retValue == null) {
                System.out.println("Remote Object Reference not found.");
            }
            return retValue;
        }
        catch (UnknownHostException e) {
            System.out.println("ERROR: Cannot connect to server. Unknown host: " + this.regHost);
            System.out.println("Please call the method again.");
            return null;
        }
        catch (IOException e) {
            System.out.println("ERROR: Cannot connect to server. IOException.");
            System.out.println("Please call the method again.");
            return null;
        }
        catch (ClassNotFoundException e) {
            System.out.println("ERROR: Cannot resolve class in the stream from server. Class not found.");
            System.out.println("Please call the method again.");
            return null;
        }
        
    }
    
    public void bind(String serviceName, RemoteObjectRef ror) 
    throws IOException
    {        
        //client cannot execute this method
        System.out.println("Can't execute bind from client.");
        return;
    }
    
    public void rebind(String serviceName, RemoteObjectRef ror) 
    throws IOException
    {        
        //client cannot execute this method
        System.out.println("Can't execute rebind from client.");
        return;
    }
    
    public void unbind(String serviceName) 
    throws IOException
    {        
        //client cannot execute this method
        System.out.println("Can't execute unbind from client.");
        return;
    }
    
    
}
