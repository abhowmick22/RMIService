package rmiservice.rmi.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import rmiservice.rmi.comm.RemoteException;
import rmiservice.rmi.comm.RemoteObjectRef;
import rmiservice.rmi.comm.ZipCodeList;
import rmiservice.rmi.comm.ZipCodeServer;

public class ZipCodeClient { 

     public static void main(String[] args) 
     {
         if(args.length!=4) {
             System.out.println("Insufficient args");
             System.out.print("Required arguments: ");
             System.out.println("<RegistryAddress> <RegistryPort> ZipCodeServer <FileLocation>");
             System.exit(0);
         }
         String host = args[0];
         int port = Integer.parseInt(args[1]);
         String serviceName = args[2];
         BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(args[3]));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: "+args[3]);            
            System.exit(0);
        }

         // locate the registry and get ROR
         SimpleRegistry sr = LocateSimpleRegistry.getRegistry(host, port);
         if(sr == null) {
             //message printed by LocateSimpleRegistry
             System.exit(0);
         }
         RemoteObjectRef ror = sr.lookup(serviceName);
         if(ror == null) {
           //message printed by SimpleRegistry
             System.exit(0);
         }
         
         // get (create) the stub out of ror
         ZipCodeServer zcs = (ZipCodeServer) ror.localise();
         if(zcs == null) {
             System.exit(0);
         }
        
         // reads the data and make a "local" zip code list.
         // later this is sent to the server.
         // again no error check!
         ZipCodeList l = null;
         boolean flag = true;
         while (flag)
         {
             String city;
             try {
                 city = in.readLine();
                 String code = in.readLine();
                 if (city == null)
                     flag = false;
                 else
                     l = new ZipCodeList(city.trim(), code.trim(), l);
             } catch (IOException e) {
                 System.out.println("IOException while creating list.");
                 System.exit(0);
             }             
         }
         
         // the final value of l should be the initial head of 
         // the list.         
         // we print out the local zipcodelist.
         System.out.println("This is the original list.");
         ZipCodeList temp = l;
         while (temp !=null)            
         {
             System.out.println
                 ("city: "+temp.city+", "+
                         "code: "+temp.ZipCode);       
             temp = temp.next;                        
         }
 
         try {
             // test the initialise.
             zcs.initialise(l);
             System.out.println("\n Server initalised.");
    
             // test the find.
             System.out.println("\n This is the remote list given by find.");
             temp = l;
             while (temp !=null)
             {
                 // here is a test.
                 String res = zcs.find(temp.city);
                 System.out.println("city: "+temp.city+", "+
                "code: "+res);
                     temp=temp.next;
             }               
             
             // test the findall.
             System.out.println("\n This is the remote list given by findall.");
             // here is a test.
             temp = zcs.findAll();
             while (temp !=null)
             {
                 System.out.println
                     ("city: "+temp.city+", "+
                             "code: "+temp.ZipCode);
                 temp=temp.next;
             }                        
             // test the printall.
             System.out.println("\n We test the remote site printing.");
             // here is a test.
             zcs.printAll();
         } catch(RemoteException e) {
             System.out.println("Server side exception:");
             System.out.println("Message from server: " + e.getLocalizedMessage());
             System.out.println("Exception class: " + e.getType());
         }
     }
}