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
    private ClientRmiMsg obj;
    private String serverIP;
    private int serverPort;
    
    public ZipCodeServer_stub(String IP, int port, int obj_key) {
        this.serverIP = IP;
        this.serverPort = port;        
        this.obj = new ClientRmiMsg();
        this.obj.obj_key = obj_key;        
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
        this.obj.args.clear();
        this.obj.args.add(newList);
        
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(this.serverIP, this.serverPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            outStream.writeObject(this.obj);
            outStream.flush();
            outStream.close();
            //Since there is no return value, we send back an ack to confirm server execution completion.
            String ack = (String) inStream.readObject();                        
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
        try {
            this.obj.hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host exception while getting local hostname.");
            return null;
        }        
        this.obj.methodName = "find";
        this.obj.args.clear();
        this.obj.args.add(city);
        
        Socket clientSocket = null;
        String zipCode = null;
        try {
            clientSocket = new Socket(this.serverIP, this.serverPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            outStream.writeObject(this.obj);
            outStream.flush();
            outStream.close();                        
            zipCode = (String) inStream.readObject();                       
            inStream.close();
            clientSocket.close();            
        }
        catch (UnknownHostException e) {
            System.out.println("ERROR: Cannot connect to server. Unknown host: " + this.serverIP);
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
        return zipCode;
    }

    @Override
    public ZipCodeList findAll()
    {
        try {
            this.obj.hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host exception while getting local hostname.");
            return null;
        }        
        this.obj.methodName = "findAll";
        this.obj.args.clear();
        
        Socket clientSocket = null;
        ZipCodeList zipCodeList = null;
        try {
            clientSocket = new Socket(this.serverIP, this.serverPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            outStream.writeObject(this.obj);
            outStream.flush();
            outStream.close();                        
            zipCodeList = (ZipCodeList) inStream.readObject();                                   
            inStream.close();
            clientSocket.close();            
        }
        catch (UnknownHostException e) {
            System.out.println("ERROR: Cannot connect to server. Unknown host: " + this.serverIP);
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
        return zipCodeList;
    }

    @Override
    public void printAll()
    {
        try {
            this.obj.hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host exception while getting local hostname.");
            return;
        }        
        this.obj.methodName = "printAll";
        this.obj.args.clear();
        Object retValue = marshall();   //do nothing because it's just an ack
        if(retValue == null) {
            return;
        } else if(retValue.getClass().equals(String.class)) {
            //acknowledgement; do nothing for this method
            return;
        } else {
            //exception
            //TODO deal with this
        }
    }
    
    private Object marshall() {
        try {
            this.obj.hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host exception while getting local hostname.");
            return null;
        }        
        
        Socket clientSocket = null;
        Object retValue = null;
        try {
            clientSocket = new Socket(this.serverIP, this.serverPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            outStream.writeObject(this.obj);
            outStream.flush();
            outStream.close();
            //Since there is no return value, we send back an ack to confirm server execution completion.
            retValue = (Object) inStream.readObject();                        
            inStream.close();
            clientSocket.close();            
        }
        catch (UnknownHostException e) {
            System.out.println("ERROR: Cannot connect to server. Unknown host: " + this.serverIP);
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
    

}
