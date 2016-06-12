package syncDemoViewer.packet;

public class DisconnectPacket extends Packet {

	private static final long serialVersionUID = -3959163257933985841L;

	public DisconnectPacket(int id) {
		super(id);
	}

	@Override
	public String message(String sender) {
		return sender + " disconnected";
	}
}
