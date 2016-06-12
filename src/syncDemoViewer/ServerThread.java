package syncDemoViewer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import syncDemoViewer.packet.ClientGreetingPacket;
import syncDemoViewer.packet.ConnectPacket;
import syncDemoViewer.packet.DisconnectPacket;
import syncDemoViewer.packet.Packet;

public class ServerThread implements Runnable {

	private int clientId;
	private Socket socket;
	private Server server;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ServerThread(Socket socket, Server server) {
		this.clientId = server.createID();

		this.socket = socket;
		this.server = server;

	}

	public int getClientId() {
		return clientId;
	}

	public Socket getSocket() {
		return socket;
	}

	@Override
	public void run() {

		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new ClientGreetingPacket(clientId, server.getController().getConnectedUsers()));
			oos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

		while (!Thread.interrupted() && !socket.isOutputShutdown()) {
			try {

				Object object = ois.readObject();
				if (object instanceof ConnectPacket) {
					ConnectPacket connectPacket = (ConnectPacket) object;
					if (server.getController().nameTaken(connectPacket.getName())) {
						int i = 2;
						for (; server.getController().nameTaken(connectPacket.getName() + String.valueOf(i)); i++)
							;
						connectPacket.setName(connectPacket.getName() + String.valueOf(i));
					}
				}
				if (object instanceof DisconnectPacket) {
					server.removeServerThread(clientId);
					socket.close();
				}
				if (object instanceof Packet) {
					server.sendPacket((Packet) object);
				}

				if (socket.isClosed()) {
					return;
				}

			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}

		}
	}

	public void sendPacket(Packet packet) {
		try {
			oos.writeObject(packet);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
