package syncDemoViewer;

import java.io.IOException;
import java.net.Socket;

public class ClientConnectionListener implements Runnable {

	private Server server;

	public ClientConnectionListener(Server server) {
		this.server = server;
	}

	@Override
	public void run() {

		while (!Thread.interrupted()) {
			try {
				Socket socket = server.getServerSocket().accept();
				ServerThread serverThread = new ServerThread(socket, server);
				server.startServerThread(serverThread);
			} catch (IOException e) {
				return;
			}
		}

	}

}
