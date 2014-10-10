package rmiservice.rmi.server;
/**
 * This is the actual implementation of the ZipCodeServer interface. Methods for this class are
 * invoked remotely by the client. 
 */

import rmiservice.rmi.comm.ZipCodeList;
import rmiservice.rmi.comm.ZipCodeServer;

public class ZipCodeServer_Impl implements ZipCodeServer
{
    private ZipCodeList l;
    
    /**
     * Constructor
     */
    public ZipCodeServer_Impl()
    {
        l=null;
    }

    /**
     * Initializes the list
     */
    @Override
    public void initialise(ZipCodeList newlist)
    {
        l=newlist;        
    }

    /**
     * Finds a zip code for a city.
     */
    @Override
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

    /**
     * Sends entire list of cities and zip codes back to client.
     */
    @Override
    public ZipCodeList findAll()
    {
        return l;
    }

    /**
     * This method does printing in the remote site, not locally. 
     */
    @Override
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
