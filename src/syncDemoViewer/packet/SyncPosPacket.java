package syncDemoViewer.packet;

public class SyncPosPacket extends Packet {

	private static final long serialVersionUID = -3884637718222171804L;

	private float val[];

	public SyncPosPacket(int id, float... values) {
		super(id);
		val = values;
	}

	public float[] getValues() {
		return val;
	}

	@Override
	public String message(String sender) {
		return sender + " shared his pos";
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < val.length; i++) {
			s += String.valueOf(val[i]);
			if (i < val.length - 1)
				s += " ";
		}
		return s;
	}

}
