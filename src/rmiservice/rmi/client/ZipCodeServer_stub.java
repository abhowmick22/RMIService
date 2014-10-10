package rmiservice.rmi.client;
/**
 * The stub for the ZipCodeServer on the client side implementing the ZipCodeServer interface that 
 * communicates with the server on behalf of the client. The client makes local calls to methods 
 * in this stub, which in turn marshalls the client's request to the server. Additionally, it waits 
 * for server reply on the successful execution of a remote method invocation, and the return value 
 * of the method. It also throws a RemoteException obtained from the server back to the client.
 */

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
    
    /**
     * Constructor
     */
    public ZipCodeServer_stub(String serverIP, int serverPort, int obj_key) 
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;        
        this.obj = new ClientRmiMsg();
        this.obj.obj_key = obj_key;        
        this.obj.args = new ArrayList<Object>();
        this.obj.argParams = new ArrayList<Class<?>>();
    }
    
    /**
     * Marshalls the initialise method.
     */
    @Override
    public void initialise(ZipCodeList newList) throws RemoteException
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
            throw (RemoteException) retValue;
        }
    }

    /**
     * Marshalls the find method.
     */
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
    /**
     * Marshalls the findAll method.
     */
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

    /**
     * Marshalls the printAll method.
     */
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
    
    /**
     * Writes the marshalled object to the server and gets the return value from the server.
     * @return Return value from server.
     */
    private Object marshall() throws RemoteException
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
            throw new RemoteException(e);
        }
        catch (IOException e) {
            throw new RemoteException(e);
        }
        catch (ClassNotFoundException e) {
            throw new RemoteException(e);
        }
        return retValue;
    }
    

}
