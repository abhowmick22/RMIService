package rmiservice.rmi.server;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class RemoteObjThread implements Runnable{
	
	public Object object;
	public ClientRmiMsg message;
	public Socket client;
	public Method method;
	
	public RemoteObjThread(Object obj, ClientRmiMsg msg, Socket client) {
		// TODO Auto-generated constructor stub
		this.object = obj;
		this.message = msg;
		this.client = client;
	}

	@Override
	public void run() {
		// get a Class<?>[] for params to be fed into reflection API
		Class<?>[] params = this.message.argParams.toArray(new Class<?>[this.message.argParams.size()]);		
		ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(this.client.getOutputStream());
        }
        catch (IOException e1) {
            // TODO handle this, print message on server side
            e1.printStackTrace();   //can't communicate this back to the client. also can't process forward.
            return;
        }
        
		try {
			this.method = object.getClass().getMethod(this.message.methodName, params);			 
			Object result = this.method.invoke(this.object, this.message.args);
			// write back result to client
			out.writeObject(result);			
		} catch (SecurityException e) {   //could have combined all these into one, but can't compile that on GHC machines
            GenerateRemoteException(e);                
        } catch (NoSuchMethodException e) {
            GenerateRemoteException(e);
        } catch (IllegalArgumentException e) {
            GenerateRemoteException(e);
        } catch (IllegalAccessException e) {
            GenerateRemoteException(e);
        } catch (InvocationTargetException e) {
            GenerateRemoteException(e);
        }
		catch (IOException e) {
            // TODO Auto-generated catch block
            //caused by out.writeObject();
            e.printStackTrace();    //can't communicate this back to the server
            return; //can't let it go to the finally block
        } finally {
            try {
                out.close();
                client.close();
            }
            catch (IOException e) {
                // TODO handle this on server side, by printing a message
                e.printStackTrace();  //caused by out.writeObject();        
            }            
        }
	}
	
	private void GenerateRemoteException(Exception e) {
        RemoteException rex = new RemoteException();
        rex.type = e.getClass();
        rex.message = "RemoteException";
        try {
            new ObjectOutputStream(client.getOutputStream()).writeObject(rex);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

	}

}
