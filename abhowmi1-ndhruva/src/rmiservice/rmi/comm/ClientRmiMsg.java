package rmiservice.rmi.comm;
/**
 * The ClientRmiMsg class helps marshall a client's request for an RMI. The client stub sends
 * the method name, argument list, and list of argument classes to the server. Additionally, it
 * sends the object key derived from the ROR to identify the remote object on the server.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class ClientRmiMsg implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public String methodName;
    public int obj_key;
    public ArrayList<Object> args;
    public ArrayList<Class<?> > argParams;
}
