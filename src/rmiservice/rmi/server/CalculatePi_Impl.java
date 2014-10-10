package rmiservice.rmi.server;
/**
 * This is the actual implementation of the CalculatePi interface. Methods for this class are
 * invoked remotely by the client. 
 */

import java.math.BigDecimal;

import rmiservice.rmi.comm.CalculatePi;
import rmiservice.rmi.comm.RemoteException;

public class CalculatePi_Impl implements CalculatePi {

    private int digits;
    
    public CalculatePi_Impl() {        
    }
    
    /**
     * Initialises the number of digits to which to calculate Pi.
     */
    @Override
    public void initialise(int digits) throws RemoteException {        
        if(digits<0) {
            throw new RemoteException ("Cannot print negative decimal digits.");            
        }
        this.digits = digits;
    }

    /**
     * Prints the Pi value on the server to the number of digits initialized by the client.
     */
    @Override
    public void printServer() throws RemoteException
    {        
        System.out.println(computePi(this.digits));        
    }

    /**
     * Returns the value of Pi (up to the number of digits requested by the client) to the client.
     */
    @Override
    public BigDecimal getPi() throws RemoteException
    {
        return computePi(this.digits);
    }
    
    /**
     * The Pi computation logic. Obtained from http://docs.oracle.com/javase/tutorial/rmi/client.html
     */
    private BigDecimal computePi(int digits) {
        int scale = digits + 5;
        BigDecimal arctan1_5 = arctan(5, scale);
        BigDecimal arctan1_239 = arctan(239, scale);
        BigDecimal pi = arctan1_5.multiply(BigDecimal.valueOf(4)).subtract(
                                  arctan1_239).multiply(BigDecimal.valueOf(4));               
        return pi.setScale(digits, 
                           BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Part of Pi computation logic. Obtained from http://docs.oracle.com/javase/tutorial/rmi/client.html.
     */
    private static BigDecimal arctan(int inverseX, 
                                    int scale) 
    {
        BigDecimal result, numer, term;
        BigDecimal invX = BigDecimal.valueOf(inverseX);
        BigDecimal invX2 = 
            BigDecimal.valueOf(inverseX * inverseX);

        numer = BigDecimal.ONE.divide(invX,
                                      scale, BigDecimal.ROUND_HALF_EVEN);

        result = numer;
        int i = 1;
        do {
            numer = 
                numer.divide(invX2, scale, BigDecimal.ROUND_HALF_EVEN);
            int denom = 2 * i + 1;
            term = 
                numer.divide(BigDecimal.valueOf(denom),
                             scale, BigDecimal.ROUND_HALF_EVEN);
            if ((i % 2) != 0) {
                result = result.subtract(term);
            } else {
                result = result.add(term);
            }
            i++;
        } while (term.compareTo(BigDecimal.ZERO) != 0);
        return result;
    }
}