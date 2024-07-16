package logic;

import java.io.Serializable;

//used for communication between a server and a client
// include the action the client did, and the relevant object the client send to server.
public class ClientDataContainer implements Serializable {
	private static final long serialVersionUID = -2549221640908833373L;
	private ClientActionsEnum clientAction;
	private Object message;

	// Constructor to initialize the clientAction and message fields.
	public ClientDataContainer(ClientActionsEnum action, Object message) {
		clientAction = action;
		this.message = message;
	}

	// getter for action
	public ClientActionsEnum getAction() {
		return clientAction;
	}

	// getter for message
	public Object getMessage() {
		return message;
	}
}
