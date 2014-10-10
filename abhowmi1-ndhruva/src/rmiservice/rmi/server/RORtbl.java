package rmiservice.rmi.server;
/**
 * RORtbl contains a mapping between a unique object key and an actual object instantiated by the
 * server.
 */

import java.util.Hashtable;

public class RORtbl
{
    private Hashtable<Integer, Object> table;
 
    /**
     * Constructor.
     */
    public RORtbl()
    {
        this.table = new Hashtable<Integer, Object>();
    }

    /**
     * Adds object to table.
     * @param obj_key Object key. 
     * @param obj Actual object.
     */
    public void addObj(int obj_key, Object obj)
    {
        this.table.put(obj_key, obj);
    }

    /**
     * Finds object from table.
     * @param obj_key Object key.
     * @return Actual object.
     */
    public Object findObj(int obj_key)
    {
        return this.table.get(obj_key);
    }
}
