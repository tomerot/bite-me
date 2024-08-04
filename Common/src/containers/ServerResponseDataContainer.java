package containers;

import java.io.Serializable;

import enums.ServerResponse;

/**
 * This class is used for communication between a server and a client.
 * It includes the action the server performed and the relevant object the server sent to the client.
 */
public class ServerResponseDataContainer implements Serializable {
	private static final long serialVersionUID = 9164656011709934534L;
	private ServerResponse response;
	private Object message;
	
	
	public ServerResponseDataContainer() {
		
	}
	
    /**
     * Constructs a new ServerResponseDataContainer with the specified response and message.
     *
     * @param response the action the server performed
     * @param message the relevant object the server sent to the client
     */
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
