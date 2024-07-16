package logic;

import java.io.Serializable;

//used for communication between a server and a client
//include the action the server did, and the relevant object the server sent to client.
public class ServerDataContainer implements Serializable {
	private static final long serialVersionUID = 9164656011709934534L;
	private ServerActionsEnum serverAction;
	private Object message;
	
	// Constructor to initialize the serverAction and message fields
	public ServerDataContainer(ServerActionsEnum action,Object message) 
	{
		serverAction=action;
		this.message=message;
	}
	
	// action getter
	public ServerActionsEnum getAction() {
		return serverAction;
	}
	
	// message getter
	public Object getMessage() {
		return message;
	}
}
