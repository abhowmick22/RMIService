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
		// TODO Auto-generated method stub
		
		// get a Class<?>[] for params to be fed into reflection API
		Class<?>[] params = this.message.argParams.toArray(new Class<?>[this.message.argParams.size()]);
		
		try {
			this.method = object.getClass().getMethod(this.message.methodName, params);
			Object result = this.method.invoke(this.object, this.message.args);
			// write back result to client
			ObjectOutputStream out = new ObjectOutputStream(this.client.getOutputStream());
			out.writeObject(result);
			
			out.close();
			client.close();
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}

}
