package syncDemoViewer;

import java.util.ArrayList;

import syncDemoViewer.packet.Packet;
import syncDemoViewer.packet.ResumeDemoPacket;
import syncDemoViewer.packet.SyncPosPacket;
import syncDemoViewer.packet.SyncTickPacket;

public class PacketMessageConverter {

	public static Packet convertMessageToPacket(ArrayList<String> message, int id) {
		String type = message.get(0).trim();
		if (type.equals(Message.start.getMessage())) {
			return new ResumeDemoPacket(id);
		}
		if (type.equals(Message.tick.getMessage())) {
			int tick = -1;
			for (int i = 1; i < message.size(); i++) {
				String line = message.get(i);
				if (line.contains("Currently playing ")) {
					String tick_string = line.split("Currently playing ")[1].split(" of ")[0];
					try {
						tick = Integer.parseInt(tick_string);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			if (tick != -1)
				return new SyncTickPacket(id, tick);
		}
		if (type.equals(Message.pos.getMessage())) {
			String pos_string = message.get(1).trim();
			String[] pos_string_arr = pos_string.split(" ");
			float[] pos = new float[pos_string_arr.length];
			for (int i = 0; i < pos.length; pos[i] = Float.parseFloat(pos_string_arr[i++]))
				;
			return new SyncPosPacket(id, pos);
		}

		return null;
	}

	public static ArrayList<String> convertPacketToMessage(Packet packet) {
		ArrayList<String> list = new ArrayList<>();
		list.add(SyncDemoViewerCfg.stop_spam_alias);
		if (packet instanceof ResumeDemoPacket) {
			list.add("demo_resume");
		}
		if (packet instanceof SyncPosPacket) {
			SyncPosPacket syncPosPacket = (SyncPosPacket) packet;
			list.add("spec_mode 6");
			list.add("spec_goto " + syncPosPacket.toString());
		}
		if (packet instanceof SyncTickPacket) {
			SyncTickPacket syncTickPacket = (SyncTickPacket) packet;
			list.add("demo_goto " + String.valueOf(syncTickPacket.getTick()) + " 0 1");
		}
		list.add(SyncDemoViewerCfg.delete_old_message_alias);
		return list;
	}

}
