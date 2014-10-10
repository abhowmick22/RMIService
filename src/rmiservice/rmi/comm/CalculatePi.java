package rmiservice.rmi.comm;

import java.math.BigDecimal;

public interface CalculatePi extends YourRemote
{
    void initialise(int digits) throws Exception;
    void printServer() throws RemoteException;
    BigDecimal getPi() throws RemoteException;
}
