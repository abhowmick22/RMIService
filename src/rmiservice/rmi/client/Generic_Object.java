package rmiservice.rmi.client;

import java.io.Serializable;
import java.util.ArrayList;

public class Generic_Object implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public String hostName;
    public int port;
    public String methodName;
    public String interfaceName;
    public ArrayList<Object> args;    
}
