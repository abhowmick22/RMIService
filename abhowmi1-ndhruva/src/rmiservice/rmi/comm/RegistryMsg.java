package rmiservice.rmi.comm;
/**
 * RegistryMsg is used by the client and the server to communicate with the registry service, which 
 * in turn communicates with the registry. The client can lookup a server, and the server can bind,
 * unbind and rebind ROR's to service names using this message.
 */

import java.io.Serializable;

public class RegistryMsg implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public String message;
    public String serviceName;
    public RemoteObjectRef refObject;
}
