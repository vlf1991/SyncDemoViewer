package syncDemoViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SyncDemoViewerCfg {

	public static final String CONFIG_FILE_NAME = "cfg/sync_demo.cfg", STOPLOG_FILE_NAME = "cfg/sync_demo_stop_log.cfg";

	public static final String delete_old_message_alias = "sync_demo_delete_old_message",
			stop_spam_alias = "sync_demo_end_sync_loop",

	sync_alias = "sync_demo_synchronize", share_pos_alias = "sync_demo_sharepos",
			share_tick_alias = "sync_demo_sharetick", resume_demo_alias = "sync_demo_resume",
			send_message_alias = "sync_demo_send_message";

	public static void verifyCfg(SyncDemoViewerProperties properties) throws Exception {

		PrintWriter writer;
		
		{
			File f = new File (properties.getCSDirectory());
			if (!f.exists()){
				GUI.showError("Folder \"" + f.getAbsolutePath() + "\" does not exist. Check your properties!");
				throw new Exception ();
			}
		}
		
		File stoplog_file = new File(properties.getCSGODir() + STOPLOG_FILE_NAME);
		if (stoplog_file.exists())
			stoplog_file.delete();
		try {
			writer = new PrintWriter(stoplog_file);
			writer.println("con_logfile \"\"");
			writer.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			GUI.showError("Could not create " + stoplog_file.getAbsolutePath() + ".");
			throw new Exception();
		}

		File cfg_file = new File(properties.getCSGODir() + CONFIG_FILE_NAME);
		if (cfg_file.exists())
			cfg_file.delete();
		try {
			writer = new PrintWriter(cfg_file);
			writer.println("//do not call these manually");
			writer.println("alias " + stop_spam_alias + " \" con_logfile " + Message.stop_spam_response_file_name
					+ " ; echo ; " + send_message_alias + "\"");
			writer.println("alias " + delete_old_message_alias + " \"con_logfile "
					+ Message.delete_old_message_file_name + " ; echo ; " + send_message_alias + "\"");
			writer.println("alias " + send_message_alias + " \"exec " + STOPLOG_FILE_NAME.substring(4) + "\"");
			writer.println();
			writer.println("alias " + sync_alias + " \"con_logfile " + Message.start_spam_request_file_name
					+ " ; echo ; " + send_message_alias + "\"");
			writer.println("alias " + share_pos_alias + " \"con_logfile " + Message.message_from_cs_file_name
					+ " ; echo " + Message.pos.getMessage() + "; spec_pos; " + send_message_alias + "\"");
			writer.println("alias " + share_tick_alias + " \"con_logfile " + Message.message_from_cs_file_name
					+ " ; echo " + Message.tick.getMessage() + "; demo_goto; " + send_message_alias + "\"");
			writer.println("alias " + resume_demo_alias + " \"con_logfile " + Message.message_from_cs_file_name
					+ " ; echo " + Message.start.getMessage() + "; demo_resume; " + send_message_alias + "\"");
			writer.println();
			writer.println("bind " + properties.getSyncKey() + " " + sync_alias);
			writer.println("bind " + properties.getSharePosKey() + " " + share_pos_alias);
			writer.println("bind " + properties.getShareTickKey() + " " + share_tick_alias);
			writer.println("bind " + properties.getResumeDemoKey() + " " + resume_demo_alias);
			writer.println();
			writer.println("// do not change this one, else it won't work.");
			writer.println("bind " + properties.getExchangeKey() + " \" exec "
					+ Message.message_to_cs_file_name.substring(4) + " \"");
			writer.println();
			writer.println();
			writer.println();
			writer.println("echo \"Synchronized Demo Viewer configuration loaded \"     ");
			writer.println("echo   ");
			writer.println("echo  -----------------------------------------------      ");
			writer.println("echo \"Synchronization key was bound to " + properties.getSyncKey() + " \"      ");
			writer.println("echo \"Share Pos key was bound to " + properties.getSharePosKey() + " \"      ");
			writer.println("echo \"Share Tick key was bound to " + properties.getShareTickKey() + " \"      ");
			writer.println("echo \"Demo resume key was bound to " + properties.getResumeDemoKey() + " \"      ");
			writer.println("echo  -----------------------------------------------      ");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			GUI.showError("Could not create " + cfg_file.getAbsolutePath() + ".");
			throw new Exception();
		}
	}

}
