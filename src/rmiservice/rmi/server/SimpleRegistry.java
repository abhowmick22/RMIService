package rmiservice.rmi.server;

import java.util.concurrent.ConcurrentHashMap;
import rmiservice.rmi.comm.RemoteObjectRef;

public class SimpleRegistry 
{  
	// the actual table storing RORs
	public ConcurrentHashMap<String, RemoteObjectRef> registry = new ConcurrentHashMap<String, RemoteObjectRef>();
    
    // simple constructor.
    public SimpleRegistry()
    {
    }

    // returns the ROR (if found) or null (if else)
    public RemoteObjectRef lookup(String serviceName)     
    { 
        if(!registry.containsKey(serviceName))
            return null;
        return registry.get(serviceName);         
    }

    // bind an ROR. 
    public void bind(String serviceName, RemoteObjectRef ror) 
    {
        if(serviceName == null || ror == null)
            return;
    	registry.put(serviceName, ror);
    }
    
    // rebind an ROR. 
    public void rebind(String serviceName, RemoteObjectRef ror)  
    {
        if(serviceName == null || ror == null)
            return;        
        registry.put(serviceName, ror);
    }
    
    // unbind an ROR. 
    public void unbind(String serviceName) 
    {
        if(registry.contains(serviceName))
            registry.remove(serviceName);
    }    
} 
  
