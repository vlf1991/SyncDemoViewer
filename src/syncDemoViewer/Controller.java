package syncDemoViewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import syncDemoViewer.packet.*;

public class Controller {

	private SyncDemoViewerProperties properties;
	private GUI viewer;

	private Thread exchange_button_spammer;
	private Thread csMessageListener;

	private int status = 0;
	private static int HOST = 1, CLIENT = 2;

	private Server server;
	private Client client;

	private HashMap<Integer, String> connected_users;

	Controller(SyncDemoViewerProperties properties) {
		this.properties = properties;
		connected_users = new HashMap<>();
		server = null;
		client = null;
	}

	public void setViewer(GUI viewer) {
		this.viewer = viewer;
	}

	public void startHosting() {
		if (status != 0)
			return;
		try {
			SyncDemoViewerCfg.verifyCfg(properties);
			server = new Server(this, properties.getPort());
			status = HOST;
			client = new Client(this, properties.getName(), "localhost", properties.getPort());
			startCSMessageListener();
			viewer.openClientView(true, "", properties.getPort());
		} catch (IOException e) {
			GUI.showError("Failed to host. Check if you have nothing running on port " + String.valueOf(properties.getPort()));
			e.printStackTrace();
		} catch (Exception e) {
			return;
		}
	}

	public void shutDownServer() {
		if (status != HOST)
			return;
		try {
			client.disconnect();
			client = null;
			server.shutDown();
			viewer.openMenuView();
			status = 0;
			connected_users.clear();
			csMessageListener.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(String host, String port) {
		if (status != 0)
			return;
		try {
			SyncDemoViewerCfg.verifyCfg(properties);
			int port_int = Integer.parseInt(port.trim());
			client = new Client(this, properties.getName(), host, port_int);
			viewer.openClientView(false, host, port_int);
			status = CLIENT;
			startCSMessageListener();
		} catch (NumberFormatException e) {
			GUI.showError("Port is not a number");
		} catch (IOException e) {
			GUI.showError("Could not connect to server");
		} catch (Exception e) {
			return;
		}
	}

	public void disconnect() {
		if (status != CLIENT)
			return;
		client.disconnect();
		client = null;
		viewer.openMenuView();
		status = 0;
		connected_users.clear();
		csMessageListener.interrupt();
	}

	public void startExchangeButtonSpam() {
		exchange_button_spammer = new Thread(new SyncButtonSpammer(properties));
		exchange_button_spammer.start();
	}

	public void startCSMessageListener() {
		csMessageListener = new Thread(new CSMessageListener(this, properties));
		csMessageListener.start();
	}

	public void createAndSendPacketFromMessage(ArrayList<String> message) {
		Packet packet = PacketMessageConverter.convertMessageToPacket(message, client.getID());
		if (packet != null)
			client.sendPacket(packet);
	}

	public void resolveReceivedPacket(Packet packet) {

		viewer.log(packet.message(connected_users.get(packet.getID())));

		if (packet instanceof ConnectPacket) {
			ConnectPacket connectPacket = (ConnectPacket) packet;
			viewer.addClientToList(connectPacket.getName());
			connected_users.put(connectPacket.getID(), connectPacket.getName());
		} else if (packet instanceof DisconnectPacket) {
			DisconnectPacket disconnectPacket = (DisconnectPacket) packet;
			viewer.removeClientFromList(connected_users.get(disconnectPacket.getID()));
			connected_users.remove(disconnectPacket.getID());
		} else
			if (packet.getID() != client.getID())
			Message.writeMessageToCs(properties, PacketMessageConverter.convertPacketToMessage(packet));

	}

	public void disconnectOrShutdown() {
		if (status == HOST)
			shutDownServer();
		else if (status == CLIENT)
			disconnect();
		viewer.removeAllClientsFromList();
		viewer.clearLog();
	}

	public boolean nameTaken(String name) {
		return connected_users.containsValue(name);
	}

	public HashMap<Integer, String> getConnectedUsers() {
		return connected_users;
	}

	public void updateConnectedUsersList(HashMap<Integer, String> connectedPlayers) {
		connected_users.clear();
		connected_users.putAll(connectedPlayers);
		viewer.removeAllClientsFromList();
		for (Integer i : connected_users.keySet())
			viewer.addClientToList(connected_users.get(i));
	}
	
	public SyncDemoViewerProperties getProperties(){
		return properties;
	}
}
