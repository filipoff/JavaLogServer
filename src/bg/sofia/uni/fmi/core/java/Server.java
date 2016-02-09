package bg.sofia.uni.fmi.core.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Server implements AutoCloseable {

	private final int SERVER_PORT;

	private ServerSocket serverSocket = null;

	private String logFileName;

	private List<ClientConnectionThread> clients = new LinkedList<>();

	public int getServerPort() {
		return SERVER_PORT;
	}

	public Server(int port, String logFileName) throws IOException {
		this.SERVER_PORT = port;
		this.serverSocket = new ServerSocket(port);
		this.logFileName = logFileName;

	}

	public void start() throws IOException {

		Scanner sc = new Scanner(System.in);
		System.out.println("Server has started, waiting for clients");
		System.out.println("Type showall to view all clients, type stop to stop the server.");

		while (true) {
			String input = sc.nextLine();
			if (input.equals("showall")) {
				printAllConnectedClients();
			}
			if (input.equals("stop")) {
				break;
			}
			Socket socket = serverSocket.accept();
			ClientConnectionThread client = new ClientConnectionThread(socket, logFileName);
			clients.add(client);
			client.setDaemon(true);
			client.start();
		}
		sc.close();
	}

	public void printAllConnectedClients() {
		if (clients.size() == 0) {
			System.out.println("No clients currently connected.");
			return;
		}
		for (ClientConnectionThread client : this.clients) {
			System.out.println(client.getSocket() + " is currently connected.");
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
		final String LOG_FILE = "test.log";

		try (Server server = new Server(PORT, LOG_FILE))

		{
			server.start();

		} catch (Exception e) {
			System.err.println("An error has occured. " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Server stopped.");
	}
}
