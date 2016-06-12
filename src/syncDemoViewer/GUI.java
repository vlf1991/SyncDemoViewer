package syncDemoViewer;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import static javax.swing.SpringLayout.*;

public class GUI {

	private final String connect = "connect",
						 host = "host",
						 properties = "properties",
						 about = "about",
						 
						 client = "client",
						 menu = "menu";
	
	private Controller controller;

	private JFrame frame;
	private JPanel panels;
	private CardLayout cardLayout;

	private JPanel client_panel;
	private JTextArea log;
	private JScrollPane log_scroll_pane;
	private JList<String> connected_users;
	private DefaultListModel<String> list_model;
	private JButton disconnect;
	private JLabel log_label, client_list_label, status_label;
	
	private JPanel menu_panel, menu_panels;
	private JButton start_host, start_connect, start_edit_properties, start_about;
	
	private JPanel edit_properties_panel;
	private JButton save_properties, open_fchooser;
	private JLabel property_name[];
	private JTextField property_val[];
	private JFileChooser file_chooser;
	private JTextField cs_dir_field;
	
	private JPanel connect_panel;
	private JButton button_connect;
	private JLabel ip_label, port_label;
	private JTextField ip_field, port_field;
	
	private JTextArea about_textpane;
	
	private JPanel host_panel;
	private JButton host_button;
	private JLabel host_label;
	

	public GUI() {
		SwingUtilities.invokeLater(() -> {
			initializeGUI();
		});
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	private void initializeGUI() {
		frame = new JFrame("Sync Demo Viewer");
		cardLayout = new CardLayout();
		panels = new JPanel(cardLayout);
		SpringLayout layout;
		
		// Menu		
		menu_panel = new JPanel();
		layout = new SpringLayout();
		menu_panel.setBorder(BorderFactory.createEtchedBorder());
		menu_panel.setLayout(layout);
		menu_panel.setPreferredSize(new Dimension(240, 160));

		start_host = new JButton("Host");
		start_connect = new JButton ("Connect");
		start_edit_properties = new JButton ("Edit properties");
		start_about = new JButton ("About");
		menu_panels = new JPanel();	
		
		start_host.addActionListener(e -> {((CardLayout)menu_panels.getLayout()).show(menu_panels, host);});
		layout.putConstraint(SpringLayout.NORTH, start_host, 16, SpringLayout.NORTH, menu_panel);
		layout.putConstraint(SpringLayout.WEST, start_host, 8, SpringLayout.WEST, menu_panel);
		layout.putConstraint(EAST, start_host, 0, EAST, start_edit_properties);
		menu_panel.add(start_host);	
		
		start_connect.addActionListener(e -> { ((CardLayout)menu_panels.getLayout()).show(menu_panels, connect); });
		layout.putConstraint(NORTH, start_connect, 8, SOUTH, start_host);
		layout.putConstraint(WEST, start_connect, 0, WEST, start_host);
		layout.putConstraint(EAST, start_connect, 0, EAST, start_edit_properties);
		menu_panel.add(start_connect);	
		
		start_edit_properties.addActionListener( e -> { ((CardLayout)menu_panels.getLayout()).show(menu_panels, properties); updatePropertiesTextFields(); });
		layout.putConstraint(NORTH, start_edit_properties, 8, SOUTH, start_connect);
		layout.putConstraint(WEST, start_edit_properties, 0, WEST, start_connect);
		menu_panel.add(start_edit_properties);

		start_about.addActionListener( e -> { ((CardLayout)menu_panels.getLayout()).show(menu_panels, about);  });
		layout.putConstraint(NORTH, start_about, 8, SOUTH, start_edit_properties);
		layout.putConstraint(WEST, start_about, 0, WEST, start_edit_properties);
		layout.putConstraint(EAST, start_about, 0, EAST, start_edit_properties);
		menu_panel.add(start_about);
		
		menu_panels.setLayout(new CardLayout());
		menu_panels.setBorder(BorderFactory.createEtchedBorder());
		layout.putConstraint(NORTH, menu_panels, 0, NORTH, start_host);
		layout.putConstraint(WEST, menu_panels, 8, EAST, start_host);
		layout.putConstraint(EAST, menu_panels, -8, EAST, menu_panel);
		layout.putConstraint(SOUTH, menu_panels, -8, SOUTH, menu_panel);
		menu_panel.add(menu_panels);

		// Connect Panel
		connect_panel = new JPanel();
		layout = new SpringLayout();
		connect_panel.setLayout(layout);
		connect_panel.setPreferredSize(new Dimension(240, 160));

		ip_label = new JLabel("IP:");
		layout.putConstraint(SpringLayout.WEST, ip_label, 16, SpringLayout.WEST, connect_panel);
		layout.putConstraint(SpringLayout.NORTH, ip_label, 32, SpringLayout.NORTH, connect_panel);
		connect_panel.add(ip_label);

		ip_field = new JTextField();
		layout.putConstraint(SpringLayout.WEST, ip_field, 16, SpringLayout.EAST, ip_label);
		layout.putConstraint(SpringLayout.EAST, ip_field, -16, SpringLayout.EAST, connect_panel);
		layout.putConstraint(SpringLayout.NORTH, ip_field, 0, SpringLayout.NORTH, ip_label);
		connect_panel.add(ip_field);

		port_label = new JLabel("Port:");
		layout.putConstraint(SpringLayout.WEST, port_label, 16, SpringLayout.WEST, connect_panel);
		layout.putConstraint(SpringLayout.NORTH, port_label, 16, SpringLayout.SOUTH, ip_label);
		connect_panel.add(port_label);

		port_field = new JTextField();
		layout.putConstraint(SpringLayout.WEST, port_field, 0, SpringLayout.WEST, ip_field);
		layout.putConstraint(SpringLayout.EAST, port_field, -16, SpringLayout.EAST, connect_panel);
		layout.putConstraint(SpringLayout.NORTH, port_field, 0, SpringLayout.NORTH, port_label);
		connect_panel.add(port_field);

		button_connect = new JButton("Connect");
		button_connect.addActionListener(e -> controller.connect(ip_field.getText(), port_field.getText()));
		layout.putConstraint(SpringLayout.WEST, button_connect, 16, SpringLayout.WEST, connect_panel);
		layout.putConstraint(SpringLayout.NORTH, button_connect, 16, SpringLayout.SOUTH, port_field);
		connect_panel.add(button_connect);
		
		menu_panels.add(connect_panel, connect);
		
		// host panel
		host_panel = new JPanel();
		layout = new SpringLayout();
		host_panel.setLayout(layout);
		
		host_label = new JLabel("Start hosting on this machine");
		layout.putConstraint(NORTH, host_label, 32, NORTH, host_panel);
		layout.putConstraint(WEST, host_label, 16, WEST, host_panel);
		host_panel.add(host_label);
		
		host_button = new JButton ("Start hosting");
		host_button.addActionListener(e -> controller.startHosting() );
		layout.putConstraint(NORTH, host_button, 16, SOUTH, host_label);
		layout.putConstraint(WEST, host_button, 0, WEST, host_label);
		host_panel.add(host_button);
		
		menu_panels.add(host_panel, host);
		
		//about panel
		about_textpane = new JTextArea();
		about_textpane.setText(Message.getAboutText());
		about_textpane.setLineWrap(true);
		about_textpane.setWrapStyleWord(true);
		about_textpane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(about_textpane);
		menu_panels.add(scrollPane, about);
		
		//edit properties
		JPanel properties_list_panel =  new JPanel();
		layout = new SpringLayout();
		properties_list_panel.setLayout(layout);
		SyncDemoViewerProperties prop = controller.getProperties();
		file_chooser = new JFileChooser();
		file_chooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Counter-Strike Global Offensive folder";
			}
			
			@Override
			public boolean accept(File f) {
				return f.isDirectory(); //f.getName().equals("Counter-Strike Global Offensive");
			}
		});
		file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		file_chooser.setAcceptAllFileFilterUsed(false);
		
		open_fchooser = new JButton ("browse");
		open_fchooser.setFont ( open_fchooser.getFont().deriveFont(10f) );
		
		property_name = new JLabel[prop.getKeyCount()];
		property_val = new JTextField[prop.getKeyCount()];
		Iterator<String> it = prop.getKeyIterator();
		for (int i = 0, count = prop.getKeyCount(); i < count; i++){
			String key = it.next();
			property_name[i] = new JLabel(prop.getNiceName(key));
			layout.putConstraint(WEST, property_name[i], 8, WEST, properties_list_panel);
			layout.putConstraint(NORTH, property_name[i], i == 0 ? 8 : 16, i == 0 ? NORTH : SOUTH, i == 0 ? properties_list_panel : property_val[i-1]);
			properties_list_panel.add(property_name[i]);
			property_val[i] = new JTextField((String) prop.get(key));
			layout.putConstraint(WEST, property_val[i], 0, WEST, property_name[i]);
			layout.putConstraint(NORTH, property_val[i], 8, SOUTH, property_name[i]);
			layout.putConstraint(EAST, property_val[i], 604, WEST, properties_list_panel);
			properties_list_panel.add(property_val[i]);
			
			if (key.equals(prop.getCSDirKey())){
				layout.putConstraint(WEST, open_fchooser, 8, EAST, property_name[i]);
				layout.putConstraint(SOUTH, open_fchooser, 0, SOUTH, property_name[i]);
				layout.putConstraint(NORTH, open_fchooser, 0, NORTH, property_name[i]);
				cs_dir_field = property_val[i];
			}	
		}
		
		
		open_fchooser.addActionListener( e -> {
			int rez = file_chooser.showOpenDialog(null);
			if (rez == JFileChooser.APPROVE_OPTION){
				File f = file_chooser.getSelectedFile();
				if (!f.getName().equals("Counter-Strike Global Offensive")){
					GUI.showError("Selected folder is not \"Counter-Strike Global Offensive\"");
					return;
				}
				cs_dir_field.setText(f.getAbsolutePath());
			}
		});
		properties_list_panel.add(open_fchooser);
		
		properties_list_panel.setPreferredSize(new Dimension(512, 700));
		scrollPane = new JScrollPane(properties_list_panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		edit_properties_panel = new JPanel();
		layout = new SpringLayout();
		edit_properties_panel.setLayout(layout);
		
		save_properties = new JButton("Save");
		save_properties.addActionListener(e -> { 
			Iterator<String> keys = prop.getKeyIterator();
			int i = 0;
			while (keys.hasNext()){
				prop.put(keys.next(), property_val[i++].getText());
			}
			prop.save();
		});
		layout.putConstraint(WEST, save_properties, 8 , WEST, edit_properties_panel);
		layout.putConstraint(SOUTH, save_properties, -8 , SOUTH, edit_properties_panel);
		edit_properties_panel.add(save_properties);
		
		layout.putConstraint(NORTH, scrollPane, 0, NORTH, edit_properties_panel);
		layout.putConstraint(EAST, scrollPane, 0, EAST, edit_properties_panel);
		layout.putConstraint(WEST, scrollPane, 0, WEST, edit_properties_panel);
		layout.putConstraint(SOUTH, scrollPane, -20, NORTH, save_properties);
		edit_properties_panel.add(scrollPane);
		
		menu_panels.add(edit_properties_panel, properties);
		
		// Client Panel
		client_panel = new JPanel();
		layout = new SpringLayout();
		client_panel.setBorder(BorderFactory.createEtchedBorder());
		client_panel.setLayout(layout);
		client_panel.setPreferredSize(new Dimension(320, 320));

		client_list_label = new JLabel("Connected clients");
		layout.putConstraint(NORTH, client_list_label, 32, NORTH, client_panel);
		layout.putConstraint(WEST, client_list_label, 16, WEST, client_panel);
		client_panel.add(client_list_label);

		disconnect = new JButton();
		layout.putConstraint(SOUTH, disconnect, -16, SOUTH, client_panel);
		layout.putConstraint(WEST, disconnect, 16, WEST, client_panel);
		disconnect.addActionListener(e -> {
			controller.disconnectOrShutdown();
		});
		client_panel.add(disconnect);

		status_label = new JLabel();
		layout.putConstraint(SOUTH, status_label, -16, SOUTH, client_panel);
		layout.putConstraint(EAST, status_label, -16, EAST, client_panel);
		client_panel.add(status_label);

		list_model = new DefaultListModel<>();
		connected_users = new JList<>(list_model);
		layout.putConstraint(NORTH, connected_users, 4, SOUTH, client_list_label);
		layout.putConstraint(WEST, connected_users, 0, WEST, client_list_label);
		layout.putConstraint(EAST, connected_users, 128, WEST, connected_users);
		layout.putConstraint(SOUTH, connected_users, -16, NORTH, disconnect);
		client_panel.add(connected_users);

		log_label = new JLabel("Log");
		layout.putConstraint(WEST, log_label, 16, EAST, connected_users);
		layout.putConstraint(NORTH, log_label, 0, NORTH, client_list_label);
		client_panel.add(log_label);

		log = new JTextArea();
		log_scroll_pane = new JScrollPane(log);
		layout.putConstraint(NORTH, log_scroll_pane, 0, NORTH, connected_users);
		layout.putConstraint(WEST, log_scroll_pane, 0, WEST, log_label);
		layout.putConstraint(EAST, log_scroll_pane, -16, EAST, client_panel);
		layout.putConstraint(SOUTH, log_scroll_pane, 0, SOUTH, connected_users);
		client_panel.add(log_scroll_pane);
		
		panels.add(menu_panel, menu);
		panels.add(client_panel, client);
		cardLayout.show(panels, menu);
		panels.setPreferredSize(new Dimension(800, 512));
		frame.add(panels);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private void updatePropertiesTextFields(){
		SyncDemoViewerProperties prop = controller.getProperties();
		Iterator<String> keys = prop.getKeyIterator();
		for (int i = 0; i < property_val.length && keys.hasNext(); i++){
			property_val[i].setText(prop.getProperty(keys.next()));
		}
	}

	public static void showError(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void openClientView(boolean isHost, String host, int ip) {
		list_model.clear();
		if (isHost) {
			status_label.setText("Hosting on port " + String.valueOf(ip));
			disconnect.setText("Shutdown host");
		} else {
			status_label.setText("Connected to  " + host + ":" + String.valueOf(ip));
			disconnect.setText("Disconnect");
		}
		cardLayout.show(panels, client);
		panels.setSize(client_panel.getPreferredSize());
		// frame.pack();
	}

	public void openMenuView() {
		cardLayout.show(panels, menu);
		panels.setSize(connect_panel.getPreferredSize());
		// frame.pack();
	}
	
	

	public void addClientToList(String name) {
		list_model.addElement(name);
	}

	public void removeClientFromList(String name) {
		list_model.removeElement(name);
	}

	public void log(String message) {
		log.setText(log.getText() + message + "\n");
	}

	public void removeAllClientsFromList() {
		list_model.clear();
	}

	public void clearLog() {
		log.setText("");
	}
}
