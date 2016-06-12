package syncDemoViewer.packet;

public class ConnectPacket extends Packet {

	private static final long serialVersionUID = 7575169818267951L;

	private String name;

	public ConnectPacket(String name, int id) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String message(String sender) {
		return name + " connected";
	}

}
