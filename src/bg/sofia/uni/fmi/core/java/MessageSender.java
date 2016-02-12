package bg.sofia.uni.fmi.core.java;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class MessageSender {

	private Socket socket;

	private Queue<String> messages;

	private MessageSenderThread messageSenderThread;

	private class MessageSenderThread extends Thread {

		@Override
		public void run() {

			try (PrintWriter out = new PrintWriter(socket.getOutputStream())) {

				while (true) {
					synchronized (messages) {
						messages.wait();
						String message = null;
						while ((message = messages.poll()) != null) {
							System.out.println("Message sender thread is sending \"" + message + "\" to server.");
							out.println(message);
							out.flush();

						}
					}
				}

			} catch (InterruptedException e1) {
				System.out.println("Message sender thread was stopped.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	MessageSender(Socket socket) {
		this.socket = socket;
		this.messages = new LinkedList<>();
		this.messageSenderThread = new MessageSenderThread();
		messageSenderThread.start();
	}

	public void send(String message) {

		synchronized (messages) {
			messages.add(message);
			messages.notify();
		}
	}

	public void stop() {
		try {
			messageSenderThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messageSenderThread.interrupt();
	}
}
