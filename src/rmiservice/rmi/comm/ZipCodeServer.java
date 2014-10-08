package rmiservice.rmi.comm;

import rmiservice.rmi.server.YourRemote;

public interface ZipCodeServer extends YourRemote// extends YourRemote or whatever
{
    public void initialise(ZipCodeList newlist) throws RemoteException;
    public String find(String city) throws RemoteException;
    public ZipCodeList findAll() throws RemoteException;
    public void printAll() throws RemoteException;
}