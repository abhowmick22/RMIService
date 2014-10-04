package rmiservice.rmi.server;

import java.io.Serializable;

public class RegistryMsg implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String message;
    public String serviceName;
    public RemoteObjectRef refObject;
}
