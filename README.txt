Ask:
1. Can we have the RemoteObjectReference.class in the client's classpath?

AB:
1. ZipCodeServer does not extend remote. Only RemoteObjectReference does? --> our implementation is a bit
	different, but ZipCodeServer might as well extend the remote interface in order to check if it itself
	can be provided as a service.
2. There is no need to Msg_type in the clientrmimsg because we send it only from client to server, and
	only for rmi, not for getRegistry. Also, the server should not have this class because the server
	sends a return value, or the reference object as a single object, and does not need to be wrapped in clientmsgrmi.
	
	
Communication: 
1. No need to send client hostname and port because server can get it from the accepted socket connection. 
	i. socket.getPort() gives the port of the other guy.
	socket.getLocalPort() gives your own port.
	ii. also, socket.getInetAddress().getHostName() gives the other guy's hostname.
	
	
TODOs:
1. check for null for all values returned by helper or stub in all client and server classes.
2. All objects which implement “Remote” are passed by reference. Other objects are passed by value (serialized)
3. make server_impl methods synchrnized