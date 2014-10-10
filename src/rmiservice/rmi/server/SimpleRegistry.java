package rmiservice.rmi.server;
/**
 * SimpleRegistry maintains a registry table that has a list of the interface name and the remote
 * object reference of the actual object that implements the interface.
 */

import java.util.concurrent.ConcurrentHashMap;
import rmiservice.rmi.comm.RemoteObjectRef;

public class SimpleRegistry 
{  
	// the actual table storing RORs
	public ConcurrentHashMap<String, RemoteObjectRef> registry = new ConcurrentHashMap<String, RemoteObjectRef>();
    
    /** 
     * simple constructor
     */
    public SimpleRegistry()
    {
    }

    /**
     * returns the ROR (if found) or null (if else)
     * @param serviceName Name of service
     * @return ROR
     */
    public RemoteObjectRef lookup(String serviceName)     
    { 
        if(!registry.containsKey(serviceName))
            return null;
        return registry.get(serviceName);         
    }

    /**
     * Binds an ROR. 
     * @param serviceName Name of service
     * @param ror ROR
     */
    public void bind(String serviceName, RemoteObjectRef ror) 
    {
        if(serviceName == null || ror == null)
            return;
    	registry.put(serviceName, ror);
    }
    
    /**
     * Rebinds an ROR with different service name or vice versa. 
     * @param serviceName Name of service
     * @param ror ROR
     */
    public void rebind(String serviceName, RemoteObjectRef ror)  
    {
        if(serviceName == null || ror == null)
            return;        
        registry.put(serviceName, ror);
    }
    
    /**
     * Unbinds an ROR. 
     * @param serviceName Name of service
     */ 
    public void unbind(String serviceName) 
    {
        if(registry.contains(serviceName))
            registry.remove(serviceName);
    }    
} 
  
