package rmiservice.rmi.comm;


public interface ZipCodeServer extends YourRemote
{
    public void initialise(ZipCodeList newlist) throws RemoteException;
    public String find(String city) throws RemoteException;
    public ZipCodeList findAll() throws RemoteException;
    public void printAll() throws RemoteException;
}