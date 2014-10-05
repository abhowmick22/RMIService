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
			//System.out.println(this.message.methodName);
			//System.out.println(params[0].getCanonicalName());
			
		
			//System.out.println(object.getClass().getMethods());
			/*
			for (Method m : object.getClass().getMethods()){
				System.out.println("svc");
				System.out.println(m.getName());
				System.out.println(m.getParameterTypes()[0].getName());
			}
			*/
			
	
			Method method = object.getClass().getMethod(this.message.methodName, params);
			

			System.out.println("method obtained");
			if(method.getReturnType().equals(Void.TYPE)) {
				System.out.println("void returned");
			    method.invoke(this.object, this.message.args);
			} else {
				System.out.println("type returned");
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
		    result = "ack";
		}
		// write back result to client
		try {
			System.out.println("trying to send res");
            out.writeObject(result);
            
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();    //handle this on server side
        }
	}
	
	private void GenerateRemoteException(Exception e, ObjectOutputStream out) {
        RemoteException rex = new RemoteException();
        rex.type = e.getClass();
        rex.message = "RemoteException";
        try {
            out.writeObject(rex);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

	}

}
