package rmiservice.rmi.client;
/**
 * Helps the client locate the simple registry by checking if it is up and returning an instance of
 * it back to the client.
 */

import java.net.*;
import java.io.*;
import rmiservice.rmi.comm.RegistryMsg;

public class LocateSimpleRegistry 
{ 
    /**
     * Locates the registry for the client.
     * @param regHost Registry IP.
     * @param regPort Registry port.
     * @return Instance of SimpleRegistry.
     */
    public static SimpleRegistry getRegistry(String regHost, int regPort)
    {
        try{
            Socket clientSocket = new Socket(regHost, regPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            RegistryMsg crm = new RegistryMsg();
            crm.message = "Who are you?";
            outStream.writeObject(crm);
            outStream.flush();  //can't close outStream yet because it screws up the connection
            String retValue = (String) inStream.readObject();    
            inStream.close();
            outStream.close();
            clientSocket.close();
            if (retValue.equals("I am a simple registry.")) {
                //the registry is up and running, so return a new communication instance to the client.
                return new SimpleRegistry(regHost, regPort);
            }
            else {
                System.out.println("Somebody is there but not a registry! System exiting.");
                return null;
            }
        } catch (Exception e) { 
            System.out.println("Nobody is there! Exception: "+e.getLocalizedMessage());
            return null;
        }
    }
}

