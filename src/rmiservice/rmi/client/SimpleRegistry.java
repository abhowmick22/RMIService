package rmiservice.rmi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
        return retValue;
    }
    
    //TODO: what to do with this on the client? Also, for rebind, make registry check if the request is coming from server.
    // rebind a ROR. ROR can be null. again no check, on this or whatever. 
    // I hate this but have no time.
    public void rebind(String serviceName, RemoteObjectRef ror) 
    throws IOException
    {
        // open socket. same as before.
        Socket soc = new Socket(this.regHost, this.regPort);
            
        // get TCP streams and wrap them. 
        BufferedReader in = 
            new BufferedReader(new InputStreamReader (soc.getInputStream()));
        PrintWriter out = 
            new PrintWriter(soc.getOutputStream(), true);
    
        // it is a rebind request, with a service name and ROR.
        out.println("rebind");
        out.println(serviceName);
        out.println(ror.IP_adr);
        out.println(ror.Port); 
        out.println(ror.Obj_Key);
        out.println(ror.Remote_Interface_Name);
    
        // it also gets an ack, but this is not used.
        String ack = in.readLine();
    
        // close the socket.
        soc.close();
    }
}
