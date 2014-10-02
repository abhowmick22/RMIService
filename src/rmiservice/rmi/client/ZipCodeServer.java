package rmiservice.rmi.client;


public interface ZipCodeServer //TODO: implements YourRemote?
{
    public void initialise(ZipCodeList newlist);
    public String find(String city);
    public ZipCodeList findAll();
    public void printAll();
}