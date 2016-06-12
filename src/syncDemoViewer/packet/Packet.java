package syncDemoViewer.packet;

import java.io.Serializable;

public class Packet implements Serializable {

	private static final long serialVersionUID = 8254786165438916246L;

	private int sender_id;

	public Packet(int id) {
		sender_id = id;
	}

	public int getID() {
		return sender_id;
	}

	public String message(String sender) {
		return sender + " sent a packet";
	}

}
