package bg.sofia.uni.fmi.core.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server implements AutoCloseable {

	private final int SERVER_PORT;

	private ServerSocket serverSocket = null;

	private List<ClientConnectionThread> clients = new LinkedList<>();

	private Logger logger;

	public int getServerPort() {
		return SERVER_PORT;
	}

	// should it throw exception?
	public Server(int port, String logFileName) {
		this.SERVER_PORT = port;
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.logger = new Logger(logFileName);

	}

	public void start() {

		System.out.println("Server started. Waiting for clients.");
		while (true) {

			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				ClientConnectionThread client = new ClientConnectionThread(clientSocket, logger, clients);
				clients.add(client);
				client.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close the server socket. " + e.getMessage());
			}
		}

		for (ClientConnectionThread client : clients) {
			client.stopThread();
		}
		clients.clear();
	}

	public static void main(String[] args) {
		final int PORT = 8000;
		final String LOGFILE = "log.txt";
		Server server = new Server(PORT, LOGFILE);
		server.start();

		// System.out.println("Server stopped.");
	}
}
