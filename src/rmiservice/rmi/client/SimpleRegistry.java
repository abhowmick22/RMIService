package rmiservice.rmi.client;

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
    
    //TODO: what to do with this on the client? 
    public void bind(String serviceName, RemoteObjectRef ror) 
    throws IOException
    {        
        //client cannot execute this method
        return;
    }
    
    //TODO: what to do with this on the client? 
    public void rebind(String serviceName, RemoteObjectRef ror) 
    throws IOException
    {        
        //client cannot execute this method
        return;
    }
    
    //TODO: what to do with this on the client? Also, for rebind, make registry check if the request is coming from server.
    public void unbind(String serviceName, RemoteObjectRef ror) 
    throws IOException
    {        
        //client cannot execute this method
        return;
    }
    
    
}
