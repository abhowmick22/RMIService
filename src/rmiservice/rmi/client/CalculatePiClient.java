package rmiservice.rmi.client;
/**
 * The actual client which interacts with the user, calls methods on the stub, which in turn 
 * marshalls them for execution on the server. It receives the return value from the stub, and a 
 * remote exception if one is thrown in the server.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rmiservice.rmi.comm.CalculatePi;
import rmiservice.rmi.comm.RemoteException;
import rmiservice.rmi.comm.RemoteObjectRef;

public class CalculatePiClient
{
    public static void main(String[] args) 
    {
        if(args.length!=3) {
            System.out.println("Unmatched args");
            System.out.print("Required arguments: ");
            System.out.println("<RegistryAddress> <RegistryPort> CalculatePi");
            System.exit(0);
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String serviceName = args[2];
        
        // locate the registry and get ROR
        SimpleRegistry sr = LocateSimpleRegistry.getRegistry(host, port);
        if(sr == null) {
            //message printed by LocateSimpleRegistry
            System.exit(0);
        }
        RemoteObjectRef ror = sr.lookup(serviceName);
        if(ror == null) {
            //message printed by SimpleRegistry
            System.exit(0);
        }         
        
        // get (create) the stub out of ror
        CalculatePi calcPi = (CalculatePi) ror.localise();
        if(calcPi == null) {
            System.exit(0);
        }
        
        while(true) {
            System.out.println("Please enter number of digits to calculate Pi to: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                int input = Integer.parseInt(br.readLine());
                if(input==0) {
                    break;
                }
                calcPi.initialise(input);
                calcPi.printServer();
                System.out.println(calcPi.getPi());
            }
            catch (NumberFormatException e) {
                System.out.println("Client side exception");
                e.printStackTrace();
            }
            catch (IOException e) {
                System.out.println("Client side exception");
                e.printStackTrace();
            }
            catch (RemoteException e) {
                System.out.println("Printing Stack Trace");
                e.getWrappedException().printStackTrace();                
            }            
        }
    }
}
