package rmiservice.rmi.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ZipCodeServer_stub implements ZipCodeServer
{
    private ClientRmiMsg obj;
    private String serverIP;
    private int serverPort;
    
    public ZipCodeServer_stub(String serverIP, int serverPort, int obj_key) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;        
        this.obj = new ClientRmiMsg();
        this.obj.obj_key = obj_key;        
        this.obj.args = new ArrayList<Object>();                
    }
    
    @Override
    public void initialise(ZipCodeList newList)
    {
        this.obj.methodName = "initialise";
        this.obj.args.clear();
        this.obj.args.add(newList);
        Object retValue = marshall();
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method            
        } else if(retValue.getClass().equals(String.class)) {
            //acknowledgment is string "ack"; do nothing for this method
        } else {
            //exception
            //TODO deal with this
        }
    }

    @Override
    public String find(String city)
    {
        this.obj.methodName = "find";
        this.obj.args.clear();
        this.obj.args.add(city);
        Object retValue = marshall();   
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method
            return null;
        } else if(retValue.getClass().equals(String.class)) {
            //correct return value
            return (String) retValue;
        } else {
            //exception
            //TODO deal with this
            return null;
        }
        
    }

    @Override
    public ZipCodeList findAll()
    {
        this.obj.methodName = "findAll";
        this.obj.args.clear();        
        Object retValue = marshall();   
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method
            return null;
        } else if(retValue.getClass().equals(ZipCodeList.class)) {
            //correct return value
            return (ZipCodeList) retValue;
        } else {
            //exception
            //TODO deal with this
            return null;
        }
        
    }

    @Override
    public void printAll()
    {       
        this.obj.methodName = "printAll";
        this.obj.args.clear();
        Object retValue = marshall();
        if(retValue == null) {
            //exception on client side, already dealt with in marshall method            
        } else if(retValue.getClass().equals(String.class)) {
            //acknowledgment is string "ack"; do nothing for this method            
        } else {
            //exception
            //TODO deal with this
        }
    }
    
    private Object marshall() {        
        
        Socket clientSocket = null;
        Object retValue = null;
        try {
            clientSocket = new Socket(this.serverIP, this.serverPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            outStream.writeObject(this.obj);
            outStream.flush();  //can't close outStream yet because it screws up the connection
            retValue = (Object) inStream.readObject();                        
            inStream.close();
            outStream.close();
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
