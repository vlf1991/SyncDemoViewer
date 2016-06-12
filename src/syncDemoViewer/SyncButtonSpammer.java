package syncDemoViewer;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

public class SyncButtonSpammer implements Runnable {

	private Robot robot;
	private File cs_resp_file;
	private int keycode;
	private int interval;
	private int duration;

	public SyncButtonSpammer(SyncDemoViewerProperties properties) {
		try {
			robot = new Robot();
		} catch (Exception e) {
			throw new Error("Failed to initialize Robot", e);
		}
		this.keycode = KeyEvent.getExtendedKeyCodeForChar(properties.getExchangeKey());
		this.interval = properties.getSyncInterval();
		this.duration = properties.getSyncDuration() * 1000;
		cs_resp_file = new File(properties.getCSGODir() + Message.stop_spam_response_file_name);
	}

	@Override
	public void run() {
		while (duration > 0 && !cs_resp_file.exists()) {

			try {
				robot.keyPress(keycode);
				Thread.sleep(interval / 2);
				robot.keyRelease(keycode);
				Thread.sleep(interval / 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			duration -= interval;
		}
		if (cs_resp_file.exists())
			cs_resp_file.delete();
	}

}
