package containers;

import java.io.Serializable;

import enums.ServerResponse;

//used for communication between a server and a client
//include the action the server did, and the relevant object the server sent to client.
public class ServerResponseDataContainer implements Serializable {
	private static final long serialVersionUID = 9164656011709934534L;
	private ServerResponse response;
	private Object message;
	
	
	public ServerResponseDataContainer() {
		
	}
	
	// Constructor to initialize the serverAction and message fields
	public ServerResponseDataContainer(ServerResponse response,Object message) 
	{
		this.response=response;
		this.message=message;
	}
	
	// action getter
	public ServerResponse getResponse() {
		return response;
	}
	
	public void setResponse(ServerResponse response) {
		this.response = response;
	}
	
	// message getter
	public Object getMessage() {
		return message;
	}
	
	public void setMessage(Object message) {
		this.message = message;
	}
}
