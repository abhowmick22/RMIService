package rmiservice.rmi.client;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientRmiMsg implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public String hostName;
    public int port;
    public String methodName;
    public int obj_key;
    public String interfaceName;
    public ArrayList<Object> args;    
}
