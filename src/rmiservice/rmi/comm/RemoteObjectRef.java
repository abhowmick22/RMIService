package rmiservice.rmi.comm;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class RemoteObjectRef implements Serializable
{
    public String IP_adr;
    public int Port;
    public int Obj_Key;
    public String Remote_Interface_Name;

    public RemoteObjectRef(String ip, int port, int obj_key, String riname) 
    {
        IP_adr=ip;
        Port=port;
        Obj_Key=obj_key;
        Remote_Interface_Name=riname;
    }

    /**
     * Creates the stub for the remote object that contains the logic for the task.
     * @return The stub of the remote object.
     */
    public Object localise()
    {
        String className = "rmiservice.rmi.client." + this.Remote_Interface_Name + "_stub"; //TODO: any other way?
        Class<?> c;
        try {
            c = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Could not create class for the interface: "+ this.Remote_Interface_Name);
            return null;
        }
        
        Object o;
        try {
            o = c.getConstructor(String.class, int.class, int.class).newInstance(this.IP_adr, this.Port, this.Obj_Key);
        }
        catch (InstantiationException e) {
            System.out.println("Could not instantiate class: "+ className);
            return null;
        }
        catch (IllegalAccessException e) {
            System.out.println("Illegal access for class: "+ className);
            return null;
        }
        catch (IllegalArgumentException e) {
            System.out.println("Illegal arguments for class: "+ className);
            return null;
        }
        catch (SecurityException e) {
            System.out.println("Security exception while instantiating: "+ className);
            return null;
        }
        catch (InvocationTargetException e) {
            System.out.println("Target invocation exception while instantiating: "+ className);
            return null;
        }
        catch (NoSuchMethodException e) {
            //won't happen
            System.out.println("Constructor exception while instantiating: "+ className);
            return null;
        }
        return o;
        
    // Implement this as you like: essentially you should 
    // create a new stub object and returns it.
    // Assume the stub class has the name e.g.
    //
    //       Remote_Interface_Name + "_stub".
    //
    // Then you can create a new stub as follows:
    // 
    //       Class c = Class.forName(Remote_Interface_Name + "_stub");
    //       Object o = c.newinstance()
    //
    // For this to work, your stub should have a constructor without arguments.
    // You know what it does when it is called: it gives communication module
    // all what it got (use CM's static methods), including its method name, 
    // arguments etc., in a marshalled form, and CM (yourRMI) sends it out to 
    // another place. 
    }
}
