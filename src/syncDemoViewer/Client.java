package syncDemoViewer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import syncDemoViewer.packet.ClientGreetingPacket;
import syncDemoViewer.packet.ConnectPacket;
import syncDemoViewer.packet.DisconnectPacket;
import syncDemoViewer.packet.Packet;

public class Client implements Runnable {

	private Controller controller;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String name;
	private int id;

	private Thread client_thread;

	public Client(Controller controller, String name, String host, int port)
			throws UnknownHostException, IOException {
		this.controller = controller;
		this.name = name;
		socket = new Socket(host, port);
		client_thread = new Thread(this);
		client_thread.start();
	}

	@Override
	public void run() {
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			ClientGreetingPacket greetingPacket = (ClientGreetingPacket) ois.readObject();
			id = greetingPacket.getID();
			controller.updateConnectedUsersList(greetingPacket.getConnectedPlayers());
			oos.writeObject(new ConnectPacket(name, id));
			oos.flush();
			
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		while (!Thread.interrupted() && !socket.isClosed()) {

			Object object;
			try {
				object = ois.readObject();

				if (object instanceof Packet) {
					Packet packet = (Packet) object;
					controller.resolveReceivedPacket(packet);
				}
			} catch (ClassNotFoundException | IOException e) {
				return;
			}

		}
	}

	public void sendPacket(Packet packet) {

		try {
			oos.writeObject(packet);
			oos.flush();

			if (packet instanceof DisconnectPacket) {
				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void disconnect() {
		sendPacket(new DisconnectPacket(id));
	}

	public int getID() {
		return id;
	}
}
