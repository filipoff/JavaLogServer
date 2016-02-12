package bg.sofia.uni.fmi.core.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientConnectionThread extends Thread {

	private Socket socket;
	private Logger logger;

	public ClientConnectionThread(Socket socket, Logger logger) {
		this.socket = socket;
		this.logger = logger;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			
			String name = null;
			name = reader.readLine();
			if (name == null) {
				stopThread();
				System.out.println("Name of " + socket + " not recieved.");
				return;
			}
			
			String message = null;

			while ((message = reader.readLine()) != null) {
				System.out.println("Message \"" + message + "\" recieved from client.");
				logger.writeToLog(name, message);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public void stopThread() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
