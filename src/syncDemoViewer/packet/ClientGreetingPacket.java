package syncDemoViewer.packet;

import java.util.HashMap;

public class ClientGreetingPacket extends Packet {

	private static final long serialVersionUID = -2578720091696639410L;

	private HashMap<Integer, String> connectedPlayers;

	public ClientGreetingPacket(int id, HashMap<Integer, String> connectedPlayers) {
		super(id);
		this.connectedPlayers = connectedPlayers;
	}

	public HashMap<Integer, String> getConnectedPlayers() {
		return connectedPlayers;
	}

}
