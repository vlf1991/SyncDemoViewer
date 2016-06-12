package syncDemoViewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CSMessageListener implements Runnable {

	private Controller controller;
	private File message_from_cs_file, start_spam_request_file, delete_old_message_file, message_to_cs_file;
	private int interval;

	public CSMessageListener(Controller controller, SyncDemoViewerProperties properties) {
		start_spam_request_file = new File(properties.getCSGODir() + Message.start_spam_request_file_name);
		start_spam_request_file.delete();
		message_from_cs_file = new File(properties.getCSGODir() + Message.message_from_cs_file_name);
		message_from_cs_file.delete();
		delete_old_message_file = new File(properties.getCSGODir() + Message.delete_old_message_file_name);
		delete_old_message_file.delete();
		message_to_cs_file = new File(properties.getCSGODir() + Message.message_to_cs_file_name);
		message_to_cs_file.delete();
		interval = properties.getSyncInterval();
		this.controller = controller;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				if (start_spam_request_file.exists()) {
					controller.startExchangeButtonSpam();
					start_spam_request_file.delete();
				}
				if (message_from_cs_file.exists()) {
					BufferedReader br = new BufferedReader(new FileReader(message_from_cs_file));
					ArrayList<String> message = new ArrayList<>();
					String line;
					while ((line = br.readLine()) != null) {
						message.add(line);
					}
					br.close();
					controller.createAndSendPacketFromMessage(message);
					message_from_cs_file.delete();
				}
				if (delete_old_message_file.exists()) {
					delete_old_message_file.delete();
					message_to_cs_file.delete();
				}
				Thread.sleep(interval);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
