package rmiservice.rmi.comm;
/**
 * RemoteException is generated when an exception occurs on the server side that needs to be
 * conveyed to the client. Every client method should throw this exception in order to gain
 * information about exceptions on the server.
 */

public class RemoteException extends Exception
{    
    private static final long serialVersionUID = 1L;
    
    private Class<?> type;
    private Exception e;
    
    /**
     * Constructor.
     */
    public RemoteException() {        
    }
    
    /**
     * Constructor that accepts an exception message.
     */
    public RemoteException(String message) {
        super(message);
    }
    
    /** 
     * Constructor that wraps another exception in this object.
     * @param e
     */
    public RemoteException(Exception e) {
        this.e = e;
    }
    
    /**
     * Constructor that takes an exception message and the actual type of exception that occured.
     */
    public RemoteException(String message, Class<?> type) {
        super(message);
        this.type = type;
    }
    
    /**
     * Get actual type of exception that occured on the server.
     */
    public Class<?> getType() {
        return this.type;
    }
    
    /**
     * Get the actual exception that occured on the server.
     */
    public Exception getWrappedException() {
        return this.e;
    }
    
}
