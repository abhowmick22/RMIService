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
			this.method = object.getClass().getMethod(this.message.methodName, params);
			 
			Object result = this.method.invoke(this.object, this.message.args);
			// write back result to client
<<<<<<< Updated upstream
			ObjectOutputStream out = new ObjectOutputStream(this.client.getOutputStream());
			out.writeObject(result);
			
			out.close();
			client.close();
			}
			catch (SecurityException|NoSuchMethodException|IllegalArgumentException|
					IllegalAccessException|InvocationTargetException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				RemoteException rex = new RemoteException();
	        	rex.type = e.getClass();
	        	rex.message = "RemoteException";
	        	try {
					ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
					out.writeObject(rex);
					out.close();
					client.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
=======
			out = new ObjectOutputStream(this.client.getOutputStream());
			out.writeObject(result);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
		    //communication with client error, needs to be handled here.
			e.printStackTrace();
			return; //can't let it go to the finally block
		} catch (SecurityException e) {
            GenerateRemoteException(e);                
        } catch (NoSuchMethodException e) {
            GenerateRemoteException(e);
        } catch (IllegalArgumentException e) {
            GenerateRemoteException(e);
        } catch (IllegalAccessException e) {
            GenerateRemoteException(e);
        } catch (InvocationTargetException e) {
            GenerateRemoteException(e);
        } finally {
            try {
                out.close();
                client.close();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();                
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
>>>>>>> Stashed changes

	}

}
