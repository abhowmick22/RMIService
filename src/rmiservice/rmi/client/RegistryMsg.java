package rmiservice.rmi.client;

import java.io.Serializable;

public class RegistryMsg implements Serializable
{
    public String message;
    public String serviceName;
    public RemoteObjectRef refObject;
}
