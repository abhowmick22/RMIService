package rmiservice.rmi.comm;
/**
 * The CalculatePi interface created by the client.
 */

import java.math.BigDecimal;

public interface CalculatePi extends YourRemote
{
    void initialise(int digits) throws RemoteException;
    void printServer() throws RemoteException;
    BigDecimal getPi() throws RemoteException;
}
