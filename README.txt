Ask:
1. Can we have the RemoteObjectReference.class in the client's classpath?

AB:
1. ZipCodeServer does not extend remote. Only RemoteObjectReference does? --> our implementation is a bit
	different, but ZipCodeServer might as well extend the remote interface in order to check if it itself
	can be provided as a service.
2. There is no need to Msg_type in the clientrmimsg because we send it only from client to server, and
	only for rmi, not for getRegistry. Also, the server should not have this class because the server
	sends a return value, or the reference object as a single object, and does not need to be wrapped in clientmsgrmi.