package syncDemoViewer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JOptionPane;

public class SyncDemoViewerProperties extends Properties {

	private static final long serialVersionUID = -5517680220835665647L;

	public final static String PROPERTIES_FILE_NAME = "sync_demo_viewer.properties";
	
	private HashMap <String, String> names_map;
	private LinkedList <String> ordered_prop_name_list;

	private SyncDemoViewerProperties() {
		names_map = new HashMap<>();
		ordered_prop_name_list = new LinkedList<>();

		put("name", "Name", "Inspector Boris");
		put("cs_dir", "CSGO directory", "C:/...something.../common/Counter-Strike GlobalOffensive/");
		put("host_port", "Hosting port", "27099");
		put("sync_interval", "Synchronisation interval (ms)", "100");
		put("sync_duration", "Synchronisation duration (s)", "5");
		put("sync_key", "Synchronisation key", "i");
		put("resume_demo_key", "Resume demo key", "j");
		put("share_pos_key", "Share your position key", "k");
		put("share_tick_key", "Share your tick key", "l");
		put("exchange_key", "CS exchange key", "-");
	}
	
	private void put (String key, String name, String default_val){
		put (key, default_val);
		names_map.put(key, name);
		ordered_prop_name_list.add(key);
	}

	public static SyncDemoViewerProperties getProperties() {
		try {

			File prop_file = new File("./" + PROPERTIES_FILE_NAME);
			SyncDemoViewerProperties properties;
			properties = new SyncDemoViewerProperties();
			if (!prop_file.exists()) {

				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(prop_file));
				properties.store(bos, "");
				bos.close();
				
				return properties;
			}
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(prop_file));
			properties.load(bis);
			bis.close();

			if (properties.getCSDirectory().endsWith("e"))
				properties.put("cs_dir", properties.getCSDirectory() + "/");

			return properties;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			throw new Error(e);
		}
	}


	public int getPort() {
		return Integer.parseInt(getProperty("host_port"));
	}

	public char getSyncKey() {
		return getProperty("sync_key").charAt(0);
	}

	public char getExchangeKey() {
		return getProperty("exchange_key").charAt(0);
	}

	public char getSharePosKey() {
		return getProperty("share_pos_key").charAt(0);
	}

	public char getShareTickKey() {
		return getProperty("share_tick_key").charAt(0);
	}

	public char getResumeDemoKey() {
		return getProperty("resume_demo_key").charAt(0);
	}

	public int getSyncInterval() {
		return Integer.parseInt(getProperty("sync_interval"));
	}

	public int getSyncDuration() {
		return Integer.parseInt(getProperty("sync_duration"));
	}

	public String getName() {
		return getProperty("name");
	}

	public String getCSDirectory() {
		return getProperty("cs_dir");
	}

	public String getCSGODir() {
		return getCSDirectory() + "csgo/";
	}
	
	public String getNiceName(String key){
		return names_map.get(key);
	}
	
	public Iterator<String> getKeyIterator(){
		return ordered_prop_name_list.iterator();
	}
	
	public int getKeyCount(){
		return ordered_prop_name_list.size();
	}
	
	public String getCSDirKey(){
		return "cs_dir";
	}
	
	public void save() {
		File prop_file = new File("./" + PROPERTIES_FILE_NAME);
		BufferedOutputStream bos;
		try {
			if (getCSDirectory().endsWith("e"))
				put("cs_dir", getCSDirectory() + "/");
			bos = new BufferedOutputStream(new FileOutputStream(prop_file));
			store(bos, "");
			bos.close();
		} catch (IOException e) {
			GUI.showError("Failed to save properties to file");
			e.printStackTrace();
		}
		
	}
}
