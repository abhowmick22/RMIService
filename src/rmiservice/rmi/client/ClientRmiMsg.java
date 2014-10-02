package rmiservice.rmi.client;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientRmiMsg implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public String methodName;
    public int obj_key;
    public ArrayList<Object> args;    
    
    
    // type : 
    // RMI_request, RMI_response, Registry_request, Registry_response
    public String msgType;
}
