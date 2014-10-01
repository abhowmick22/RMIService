package rmiservice.rmi.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ZipCodeServer_stub implements ZipCodeServer
{
    private Generic_Object obj;
    private String serverIP;
    private int serverPort;
    
    public ZipCodeServer_stub(String IP, int port) {
        this.serverIP = IP;
        this.serverPort = port;
        this.obj = new Generic_Object();        
        this.obj.interfaceName = "ZipCodeServer";        
        this.obj.args = new ArrayList<Object>();        
    }
    
    @Override
    public void initialise(ZipCodeList newList)
    {
        try {
            this.obj.hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host exception while getting local hostname.");
            return;
        }        
        this.obj.methodName = "initialise";
        this.obj.args.add(newList);
        
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(this.serverIP, this.serverPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            outStream.writeObject(this.obj);
            outStream.flush();
            outStream.close();            
            @SuppressWarnings("unchecked")
            ArrayList<Object> retObj = (ArrayList<Object>) inStream.readObject();            
            //do nothing here because no return value. this just makes sure that we return
            //to the client only after the server has executed the method. So it is some sort of
            //an ack.            
            inStream.close();
            clientSocket.close();            
        }
        catch (UnknownHostException e) {
            System.out.println("ERROR: Cannot connect to server. Unknown host: " + this.serverIP);
            System.out.println("Please call the method again.");
            return;
        }
        catch (IOException e) {
            System.out.println("ERROR: Cannot connect to server. IOException.");
            System.out.println("Please call the method again.");
            return;
        }
        catch (ClassNotFoundException e) {
            System.out.println("ERROR: Cannot resolve class in the stream from server. Class not found.");
            System.out.println("Please call the method again.");
            return;
        }
      
    }

    @Override
    public String find(String city)
    {
        return null;
    }

    @Override
    public ZipCodeList findAll()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void printAll()
    {
        // TODO Auto-generated method stub
        
    }

}
