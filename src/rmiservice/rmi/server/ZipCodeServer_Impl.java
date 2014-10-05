package rmiservice.rmi.server;

import rmiservice.rmi.comm.ZipCodeList;
import rmiservice.rmi.comm.ZipCodeServer;

//in implementation, you do not have to extend this as in Java RMI. 
//in your design, however, you can do so.
//it is assumed that this is not directly called but as in:
//
//java yourRMI ZipCodeServerImpl registryhost registryport servicename
//  OR
//java yourRMI registryHost registryPort servicename1(eg. ZipCodeServer)
//
//therefore it does not contain main: new object creation, binding etc. is 
//done via yourRMI

public class ZipCodeServer_Impl implements ZipCodeServer
{
    private ZipCodeList l;
    
    // this is a constructor.
    public ZipCodeServer_Impl()
    {
        l=null;
    }

    // when this is called, unmarshalled data
    // should be sent to this remote object,
    // and reconstructed.
    public void initialise(ZipCodeList newlist)
    {
        l=newlist;
        System.out.println(l);
    }

    // basic function: gets a city name, returns the zip code.
    public String find(String request)
    {
        // search the list.
        ZipCodeList temp = l;
        while (temp != null && !temp.city.equals(request))
            temp = temp.next;

        // the result is either null or we found the match.
        if (temp==null)
            return null;
        else
            return temp.ZipCode;
    }

    // this very short method should send the marshalled 
    // whole list to the local site.
    public ZipCodeList findAll()
    {
        return l;
    }

    // this method does printing in the remote site, not locally.
    public void printAll() 
    {
        ZipCodeList temp=l;
        while (temp !=null)            
        {
            System.out.println
            ("city: "+temp.city+", "+
                    "code: "+temp.ZipCode+"\n");        
            temp = temp.next;                        
        }
    }
}
