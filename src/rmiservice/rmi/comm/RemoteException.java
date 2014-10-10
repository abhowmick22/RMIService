package rmiservice.rmi.comm;

public class RemoteException extends Exception
{    
    private static final long serialVersionUID = 1L;
    
    private Class<?> type;
    private Exception e;
    
    public RemoteException() {        
    }
    
    public RemoteException(String message) {
        super(message);
    }
    
    public RemoteException(Exception e) {
        this.e = e;
    }
    
    public RemoteException(String message, Class<?> type) {
        super(message);
        this.type = type;
    }
    
    public Class<?> getType() {
        return this.type;
    }
    
    public Exception getWrappedException() {
        return this.e;
    }
    
}
