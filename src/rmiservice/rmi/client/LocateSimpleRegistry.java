package rmiservice.rmi.client;

import java.net.*;
import java.io.*;

//Needs to act as stub for LocateSimpleRegistry on the server
public class LocateSimpleRegistry 
{ 
    
    public static SimpleRegistry getRegistry(String regHost, int regPort)
    {
        try{
            Socket clientSocket = new Socket(regHost, regPort);
            ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            ClientRegMsg crm = new ClientRegMsg();
            crm.message = "Who are you?";
            outStream.writeObject(crm);
            outStream.flush();  //can't close outStream yet because it screws up the connection
            String retValue = (String) inStream.readObject();    
            inStream.close();
            outStream.close();
            clientSocket.close();
            if (retValue.equals("I am a simple registry.")) {
                return new SimpleRegistry(regHost, regPort);
            }
            else {
                System.out.println("Somebody is there but not a registry!");
                return null;
            }
        } catch (Exception e) { 
            System.out.println("Nobody is there!"+e);
            return null;
        }
    }
}

