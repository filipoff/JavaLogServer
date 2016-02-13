package bg.sofia.uni.fmi.core.java;

import java.io.IOException;
import java.net.Socket;

public class Client {

	private String host;

	private int port;

	private Socket socket;

	private String name;

	private MessageSender messageSender;

	Client(String host, int port, String name) {
		this.host = host;
		this.port = port;
		this.socket = null;
		this.messageSender = null;
		this.name = name;
	}

	public void connect() throws IOException {
		socket = new Socket(host, port);
		messageSender = new MessageSender(socket);
		sendMessage(name);

	}

	public void sendMessage(String message) {
		messageSender.send(message);
	}

	public void disconnect() {

		if (messageSender != null) {
			System.out.println("disconnect called from " + name);
			messageSender.stop();
		}

		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
