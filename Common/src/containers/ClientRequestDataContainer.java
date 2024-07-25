package containers;

import java.io.Serializable;

import enums.ClientRequest;

//used for communication between a server and a client
// include the action the client did, and the relevant object the client send to server.
public class ClientRequestDataContainer implements Serializable {
	private static final long serialVersionUID = -2549221640908833373L;
	private ClientRequest request;
	private Object message;

	// Constructor to initialize the clientAction and message fields.
	public ClientRequestDataContainer(ClientRequest action, Object message) {
		request = action;
		this.message = message;
	}

	// getter for action
	public ClientRequest getRequest() {
		return request;
	}

	// getter for message
	public Object getMessage() {
		return message;
	}
}
