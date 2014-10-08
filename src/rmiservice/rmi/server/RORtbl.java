package rmiservice.rmi.server;

import java.util.Hashtable;

public class RORtbl
{
    private Hashtable<Integer, Object> table;
 
    public RORtbl()
    {
        this.table = new Hashtable<Integer, Object>();
    }

    public void addObj(int obj_key, Object obj)
    {
        this.table.put(obj_key, obj);
    }

    public Object findObj(int obj_key)
    {
        return this.table.get(obj_key);
    }
}
