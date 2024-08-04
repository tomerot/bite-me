package containers;

import java.io.Serializable;

import enums.ClientRequest;

/**
 * This class is used for communication between a server and a client.
 * It includes the action the client performed and the relevant object the client sent to the server.
 */
public class ClientRequestDataContainer implements Serializable {
	private static final long serialVersionUID = -2549221640908833373L;
	private ClientRequest request;
	private Object message;

    /**
     * Constructs a new ClientRequestDataContainer with the specified action and message.
     *
     * @param action the action the client performed
     * @param message the relevant object the client sent to the server
     */
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
