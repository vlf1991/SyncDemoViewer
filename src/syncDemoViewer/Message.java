package syncDemoViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public enum Message {

	start("START"), pos("POS"), tick("TICK");

	public final static String stop_spam_response_file_name = "sync_demo_synchronization_done",
			start_spam_request_file_name = "sync_demo_synchronization_start",
			delete_old_message_file_name = "sync_demo_delete_old_message",
			message_from_cs_file_name = "sync_demo_message_from_cs",

	message_to_cs_file_name = "cfg/sync_demo_message_to_cs.cfg";

	public static void writeMessageToCs(SyncDemoViewerProperties properties, ArrayList<String> message)
			 {
		File f = new File(properties.getCSGODir() + message_to_cs_file_name);
		f.delete();
		PrintWriter writer;
		try {
			writer = new PrintWriter(f);
			for (String s : message)
				writer.println(s);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			GUI.showError("Failed to write to " + f.getAbsolutePath());
		}
		
	}

	private String message;

	Message(String s) {
		message = s;
	}

	public String getMessage() {
		return message;
	}

	public static String getAboutText() {
		return    "Made by /u/arvyy; PM if you have questions and/or suggestions \n"
				+ "\n\n\n"
				+ "How to use this thing \n"
				+ "First of all, edit your properties, as this won't work with at least valid CSGO directory. One person host, and others connect to him. The port "
				+ "will be shown on host's screen on bottom right. The host's IP can be found by host running 'ipconfig' in cmd, one of those numbers should work. "
				+ "When everyone is connected, launch CS:GO, and type in 'exec sync_demp.cfg'. Pressing the synchronisation key will attempt to apply a waiting packet;"
				+ " if it does not exist, it will keep retrying for (sync duration) seconds every (sync interval) ms."
				+ " Share pos / share tick / resume demo keys will send according packets to everyone "
				+ "else connected to same host. Keep in mind that you can have only one packet waiting; if new one arrives, the old one will be overwritten. "
				+ " Overall, if you want to share pos / tick, then you press according button, tell your mates to press sync button, and once they press it, they get to same "
				+ "pos / tick as you. If you want to have a simultanious demo playback resume: you tell your mates to press sync button roughly at same time, and then you "
				+ "press resume demo key. You have (sync duration) timewindow from first gal pressing sync button to you pressing resume demo key for it work, so adjust it "
				+ "accordingly if you need to."
				+ "\n\n\n"
				+ "Is it VAC safe?\n"
				+ "Short version: yes. Long version: This program never attempts to write to or read from csgo process memory. It operates through files in cs directory: "
				+ "essentially sending information from cs to this program involves con_logfile and echo trickery; the program has a thread constantly checking whether files with certain names"
				+ " appeared, and if they do, lets the program know and deletes them. Sending information from this program to cs, essentially means creating a cfg file, which"
				+ " is executed on button press. And this button press is imitated by program in a loop once cs lets know that synchronisation button was pressed.";
	}
}
