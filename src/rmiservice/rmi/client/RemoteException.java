package rmiservice.rmi.client;

public class RemoteException extends Exception
{
    
    private static final long serialVersionUID = 1L;
    
    public String message;  
    public Class<?> type;

    //when you encounter a new exception, 
    //1. create new RemoteException object
    //2. set message and exception class
    //3. communicate back to the client in the catch close
    //4. close and streams and sockets that are active with the client
    //5. return if the exception occured in the execution thread and not the dispatcher
}
