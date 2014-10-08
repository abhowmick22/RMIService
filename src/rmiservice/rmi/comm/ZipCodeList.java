package rmiservice.rmi.comm;

import java.io.Serializable;

public class ZipCodeList implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public String city;
    public String ZipCode;
    public ZipCodeList next;

    public ZipCodeList(String c, String z, ZipCodeList n)
    {
        this.city=c;
        this.ZipCode=z;
        this.next=n;
    }
}
