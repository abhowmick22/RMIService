package rmiservice.rmi.server;

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
