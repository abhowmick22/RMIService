package assignment2.rmi.server;

public class ZipCodeList
{
    String city;
    String ZipCode;
    ZipCodeList next;

    public ZipCodeList(String c, String z, ZipCodeList n)
    {
        city=c;
        ZipCode=z;
        next=n;
    }
}
