package bg.sofia.uni.fmi.core.java;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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

	public void connect() throws InterruptedException {
		try {
			this.socket = new Socket(host, port);
			this.messageSender = new MessageSender(socket);
			this.sendMessage(name);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMessage(String message) {
		this.messageSender.send(message);
	}

	public void disconnect() {

		if (this.messageSender != null) {
			this.messageSender.close();
		}

		if (this.socket != null)

		{
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
