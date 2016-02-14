package bg.sofia.uni.fmi.core.java;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

	private String host;

	private int port;

	private String name;

	private AtomicInteger messagesCounter;

	private Queue<String> messages;

	private MessageSenderThread messageSenderThread;

	private class MessageSenderThread extends Thread {
		private Socket socket;
		private PrintWriter out;

		private void connect() throws IOException {
			socket = new Socket(host, port);

			if (socket != null) {
				out = new PrintWriter(socket.getOutputStream());
				System.out.println("Connection successful form client " + name);
				out.println(name);
			}

		}

		@Override
		public void run() {

			try {

				while (true) {

					try {

						connect();

						while (true) {

							String message = null;
							synchronized (messages) {
								message = messages.peek();
								if (message == null) {
									messages.wait();
								}
							}
							if (message != null) {
								out.println(message);
								if (out.checkError()) {
									throw new IOException("Error sending message from client " + name);
								}
								synchronized (messages) {
									messages.poll();
									messagesCounter.decrementAndGet();
								}
								System.out.println(
										"Message sender thread sent \"" + message + "\" to server from client " + name);
							}
						}
					} catch (IOException e) {
						
						System.out.println("IO exception in client " + name + ", reconnecting.");
						Thread.sleep(500);

					} finally {
						if (out != null) {
							out.close();
							out = null;
						}
						if (socket != null) {
							try {
								socket.close();
								System.out.println("Closed socket from client " + name);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							socket = null;
						}
					}
				}
			} catch (InterruptedException e1) {
				System.out.println("Message sender thread was stopped in  client " + name);
			}
		}
	}

	Client(String host, int port, String name) {
		this.host = host;
		this.port = port;
		this.name = name;
		this.messages = new LinkedList<>();
		this.messageSenderThread = new MessageSenderThread();
		this.messageSenderThread.start();
		this.messagesCounter = new AtomicInteger(0);
	}

	public void sendMessage(String message) {
		System.out.println("Queueing message " + message + " from client " + name);
		synchronized (messages) {
			messages.add(message);
			messagesCounter.incrementAndGet();
			messages.notify();
		}
	}

	public int pendingMessagesCount() {

		return messagesCounter.intValue();
	}

	public void disconnect() {
		System.out.println("Disconnect called from " + name);
		synchronized (messages) {
			messageSenderThread.interrupt();
		}

	}
}
