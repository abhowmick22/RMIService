package rmiservice.rmi.client;
/**
 * The stub for CalculatePi on the client side implementing the CalculatePi interface that 
 * communicates with the server on behalf of the client. The client makes local calls to methods 
 * in this stub, which in turn marshalls the client's request to the server. Additionally, it waits 
 * for server reply on the successful execution of a remote method invocation, and the return value 
 * of the method. It also throws a RemoteException obtained from the server back to the client.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import rmiservice.rmi.comm.CalculatePi;
import rmiservice.rmi.comm.ClientRmiMsg;
import rmiservice.rmi.comm.RemoteException;

public class CalculatePi_stub implements CalculatePi
{
    private ClientRmiMsg obj;
    private String serverIP;
    private int serverPort;
    
    /**
     * Constructor.
     */
    public CalculatePi_stub(String serverIP, int serverPort, int obj_key) 
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
    public void initialise(int digits) throws RemoteException
    {
        this.obj.methodName = "initialise";
        this.obj.args.clear();
        this.obj.args.add(digits);
        this.obj.argParams.clear();
        this.obj.argParams.add(int.class);
        Object retValue = marshall();
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method   
            return;
        } else if(retValue.getClass().equals(String.class)) {
            //acknowledgment is string "ACK"; do nothing for this method
            return;
        } else {
            //exception
            System.out.println("Remote Exception occured:");
            throw (RemoteException) retValue;
        }
    }

    /**
     * Marshalls the printServer method.
     */
    @Override
    public void printServer() throws RemoteException
    {       
        this.obj.methodName = "printServer";
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
     * Marshalls the getPi method.
     */
    @Override
    public BigDecimal getPi() throws RemoteException
    {
        this.obj.methodName = "getPi";
        this.obj.args.clear();
        this.obj.argParams.clear();
        Object retValue = marshall();
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method
            return null;
        } else if(retValue.getClass().equals(BigDecimal.class)) {
            //correct return value
            return (BigDecimal) retValue;
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
