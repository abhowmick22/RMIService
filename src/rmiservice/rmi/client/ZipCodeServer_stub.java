package rmiservice.rmi.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import rmiservice.rmi.comm.ClientRmiMsg;
import rmiservice.rmi.comm.ZipCodeList;
import rmiservice.rmi.comm.ZipCodeServer;
import rmiservice.rmi.comm.RemoteException;

public class ZipCodeServer_stub implements ZipCodeServer
{
    private ClientRmiMsg obj;
    private String serverIP;
    private int serverPort;
    
    public ZipCodeServer_stub(String serverIP, int serverPort, int obj_key) 
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;        
        this.obj = new ClientRmiMsg();
        this.obj.obj_key = obj_key;        
        this.obj.args = new ArrayList<Object>();
        this.obj.argParams = new ArrayList<Class<?>>();
    }
    
    @Override
    public void initialise(ZipCodeList newList)
    {
        this.obj.methodName = "initialise";
        this.obj.args.clear();
        this.obj.args.add(newList);
        this.obj.argParams.clear();
        this.obj.argParams.add(ZipCodeList.class);
        Object retValue = marshall();
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method   
            return;
        } else if(retValue.getClass().equals(String.class)) {
            //acknowledgment is string "ack"; do nothing for this method
            return;
        } else {
            //exception
            System.out.println("Remote Exception occured:");
            //throw (RemoteException) retValue;
        }
    }

    @Override
    public String find(String city) throws RemoteException
    {
        this.obj.methodName = "find";
        this.obj.args.clear();
        this.obj.args.add(city);
        this.obj.argParams.clear();
        this.obj.argParams.add(String.class);
        Object retValue = marshall();   
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method
            return null;
        } else if(retValue.getClass().equals(String.class)) {
            //correct return value
            return (String) retValue;
        } else {
            //exception
            System.out.println("Remote Exception occured:");
            throw (RemoteException) retValue;
        }
        
    }

    @Override
    public ZipCodeList findAll() throws RemoteException
    {
        this.obj.methodName = "findAll";
        this.obj.args.clear();      
        this.obj.argParams.clear();
        Object retValue = marshall();   
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method
            return null;
        } else if(retValue.getClass().equals(ZipCodeList.class)) {
            //correct return value
            return (ZipCodeList) retValue;
        } else {
            //exception
            System.out.println("Remote Exception occured:");
            throw (RemoteException) retValue;
        }
        
    }

    @Override
    public void printAll() throws RemoteException
    {       
        this.obj.methodName = "printAll";
        this.obj.args.clear();
        this.obj.argParams.clear();
        Object retValue = marshall();
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method
            return;
        } else if(retValue.getClass().equals(String.class)) {
            //acknowledgment is string "ack"; do nothing for this method  
            return;
        } else {
            //exception
            System.out.println("Remote Exception occured:");
            throw (RemoteException) retValue;
        }
    }
    
    private Object marshall() 
    {                
        Socket clientSocket = null;
        Object retValue = null;        
        try {
            clientSocket = new Socket(this.serverIP, this.serverPort);            
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());            
            outStream.writeObject(this.obj);            
            outStream.flush();  //can't close outStream yet because it screws up the connection
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());                             
            retValue = (Object) inStream.readObject();                        
            inStream.close();
            outStream.close();
            //client can close the socket. 
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
