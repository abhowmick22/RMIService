package rmiservice.rmi.server;

/*
This will be used by RegistryService to get a reference to the SimpleRegistry
Listening from the client stub for this class will be done by RegistryService
*/

// this should be removed
public class LocateSimpleRegistry 
{ 
    // this is the SOLE static method.
    // you use it as: LocateSimpleRegistry.getRegistry(123.123.123.123, 2048)
    // and it returns null if there is none, else it returns the registry.

    public static void getRegistry()
    {
    	    	
    }
}

