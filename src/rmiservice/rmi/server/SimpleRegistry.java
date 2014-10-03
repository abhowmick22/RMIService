package rmiservice.rmi.server;

import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class SimpleRegistry 
{  
	// the actual table storing RORs
	ConcurrentHashMap<String, RemoteObjectRef> registry = new ConcurrentHashMap<String, 
																				RemoteObjectRef>();
    
    // simple constructor.
    public SimpleRegistry()
    {
    }

    // returns the ROR (if found) or null (if else)
    public RemoteObjectRef lookup(String serviceName)     
    { 
        RemoteObjectRef ror = registry.get(serviceName);    
        // return ROR.
        return ror;
    }

    // bind an ROR. ROR can be null. again no check, on this or whatever. 
    // I hate this but have no time.
    public void bind(String serviceName, RemoteObjectRef ror) 
    throws IOException
    {
    	registry.put(serviceName, ror);
    }
    
    
} 
  
