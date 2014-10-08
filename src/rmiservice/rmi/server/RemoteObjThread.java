package rmiservice.rmi.server;

import java.net.Socket;
import rmiservice.rmi.comm.ClientRmiMsg;
import rmiservice.rmi.comm.RemoteException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RemoteObjThread implements Runnable{
	
	public Object object;
	public ClientRmiMsg message;
	public Socket client;
	
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
		Object result = null;
		
		//create stream to send result back to client
        try {
            out = new ObjectOutputStream(this.client.getOutputStream());
        }
        catch (IOException e1) {
            // TODO handle this, print message on server side
            e1.printStackTrace();   //can't communicate this back to the client. also can't process forward.
            return;
        }        
        
        //execute method, and get return value if there is one
		try {
		    Method method = object.getClass().getMethod(this.message.methodName, params);
			if(method.getReturnType().equals(Void.TYPE)) {
			    method.invoke(this.object, this.message.args);
			} else {
			    result = method.invoke(this.object, this.message.args);
			}
		} catch (SecurityException e) {   //could have combined all these into one, but can't compile that on GHC machines
            GenerateRemoteException(e, out);   
            return;
        } catch (NoSuchMethodException e) {
            GenerateRemoteException(e, out);
            return;
        } catch (IllegalArgumentException e) {
            GenerateRemoteException(e, out);
            return;
        } catch (IllegalAccessException e) {
            GenerateRemoteException(e, out);
            return;
        } catch (InvocationTargetException e) {
            GenerateRemoteException(e, out);
            return;
        }
		
		//if there is no return value, return "ack"
		if(result == null) {
		    result = "ACK";
		}
		// write back result to client, could be null
		try {
			out.writeObject(result);
			out.flush();
			client.shutdownOutput();
        }
        catch (IOException e) {
            System.out.println("Could not communicate the result/ACK back to the client due to IOException. The stacktrace is:");
            e.printStackTrace();
        }		
	}
	
	private void GenerateRemoteException(Exception e, ObjectOutputStream out) {
        RemoteException rex = new RemoteException("RemoteException was generated.", e.getClass());
        try {
            out.writeObject(rex);
        } catch (IOException e1) {
            System.out.println("Could not communicate the RemoteException back to the client due to IOException. The stacktrace is:");
            e.printStackTrace();
        }

	}

}
