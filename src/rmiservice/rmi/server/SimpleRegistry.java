package rmiservice.rmi.server;

import java.util.*;
import java.net.*;
import java.io.*;

public class SimpleRegistry 
{ 
    // registry holds its port and host, and connects to it each time. 
    private String host;
    private int port; 
    
    // ultra simple constructor.
    public SimpleRegistry(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    // returns the ROR (if found) or null (if else)
    public RemoteObjectRef lookup(String serviceName)     
    {
        // open socket.
        // it assumes registry is already located by locate registry.
        // you should usually do try-catch here (and later).
        Socket soc = null;
        try {
            soc = new Socket(this.host, this.port);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            //could be unknown host
            e.printStackTrace();
        }
    
        System.out.println("socket made.");
            
        // get TCP streams and wrap them. 
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader (soc.getInputStream()));
            out = new PrintWriter(soc.getOutputStream(), true);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        System.out.println("stream made.");    
        
        // it is locate request, with a service name.
        out.println("lookup");
        out.println(serviceName);    
        System.out.println("command and service name sent.");
    
        // branch according to the answer.
        String res = null;
        try {
            res = in.readLine();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        RemoteObjectRef ror;    
        if (res.equals("found"))
        {    
            System.out.println("it is found!.");    
            // receive ROR data, witout check.
            String ro_IPAdr = null, ro_InterfaceName = null;
            int ro_PortNum = 0, ro_ObjKey = 0;
            try {
                ro_IPAdr = in.readLine();
                System.out.println(ro_IPAdr);                
                ro_PortNum = Integer.parseInt(in.readLine());        
                System.out.println(ro_PortNum);        
                ro_ObjKey = Integer.parseInt(in.readLine());        
                System.out.println(ro_ObjKey);        
                ro_InterfaceName = in.readLine();        
                System.out.println(ro_InterfaceName);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }        
            // make ROR.
            ror = new RemoteObjectRef(ro_IPAdr, ro_PortNum, ro_ObjKey, ro_InterfaceName);
        }
        else        
        {
            System.out.println("it is not found!.");    
            ror = null;
        }
    
        // close the socket.
        try {
            soc.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
        // return ROR.
        return ror;
    }

    // rebind a ROR. ROR can be null. again no check, on this or whatever. 
    // I hate this but have no time.
    public void rebind(String serviceName, RemoteObjectRef ror) 
    throws IOException
    {
        // open socket. same as before.
        Socket soc = new Socket(this.host, this.port);            
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
  
