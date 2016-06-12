package syncDemoViewer.packet;

public class ResumeDemoPacket extends Packet {

	private static final long serialVersionUID = -8889803482278874393L;

	public ResumeDemoPacket(int id) {
		super(id);
	}

	@Override
	public String message(String sender) {
		return sender + " resumed demo";
	}
}
