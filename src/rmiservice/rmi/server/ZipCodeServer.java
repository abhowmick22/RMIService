package rmiservice.rmi.server;

public interface ZipCodeServer extends YourRemote// extends YourRemote or whatever
{
    public void initialise(ZipCodeList newlist);
    public String find(String city);
    public ZipCodeList findAll();
    public void printAll();
}