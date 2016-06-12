package syncDemoViewer.packet;

public class SyncTickPacket extends Packet {

	private static final long serialVersionUID = -4449689980282873595L;

	private final int tick;

	public SyncTickPacket(int id, int tick) {
		super(id);
		this.tick = tick;
	}

	public int getTick() {
		return tick;
	}

	@Override
	public String message(String sender) {
		return sender + " shared his tick";
	}

}
