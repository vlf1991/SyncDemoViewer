package syncDemoViewer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;

import syncDemoViewer.packet.Packet;

public class Server {

	private Controller controller;

	private ArrayList<ServerThread> serverThreads;
	private Thread clientConnectionListener;
	private ServerSocket serverSocket;
	private int next_id = 0;

	public Server(Controller controller, int port) throws IOException {
		this.controller = controller;
		serverThreads = new ArrayList<>();
		serverSocket = new ServerSocket(port);
		clientConnectionListener = new Thread(new ClientConnectionListener(this));
		clientConnectionListener.start();
	}

	public int createID() {
		return next_id++;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void startServerThread(ServerThread serverThread) {
		serverThreads.add(serverThread);
		Thread thread = new Thread(serverThread);
		thread.start();
	}

	public synchronized void removeServerThread(int id) {
		Iterator<ServerThread> i = serverThreads.iterator();
		while (i.hasNext()) {
			ServerThread serverThread = i.next();
			if (serverThread.getClientId() == id) {
				i.remove();
				return;
			}
		}
	}

	public synchronized void sendPacket(Packet packet) {
		for (ServerThread serverThread : serverThreads) {
			serverThread.sendPacket(packet);
		}
	}

	public void shutDown() throws IOException {
		Iterator<ServerThread> i = serverThreads.iterator();
		while (i.hasNext()) {
			i.next();
			i.remove();
		}
		clientConnectionListener.interrupt();
		serverSocket.close();
	}

	public Controller getController() {
		return controller;
	}

}
