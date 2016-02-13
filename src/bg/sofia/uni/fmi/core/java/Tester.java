package bg.sofia.uni.fmi.core.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tester {

	private int messageCount;

	private List<TesterThread> threads;

	private class TesterThread extends Thread {

		private Client client;
		private String name;

		public TesterThread(String name) {
			this.name = name;
			this.client = new Client("localhost", 8000, name);
		}

		@Override
		public void run() {

			try {
				client.connect();

				for (int i = 0; i < messageCount; i++) {
					client.sendMessage("msg " + i);
				}

				client.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Client " + name + " could not connect to server.");
				// e.printStackTrace();
			}

		}

	}

	public Tester(int threadCount, int messageCount) {
		this.messageCount = messageCount;
		this.threads = new ArrayList<>();

		for (int i = 1; i <= threadCount; i++) {
			TesterThread thread = new TesterThread("Tester" + i);
			threads.add(thread);
		}
	}

	private void startTest() throws InterruptedException {

		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
	}

	public static void main(String[] args) {

		final int CLIENT_COUNT = 5;
		final int MESSAGE_COUNT = 10;
		Tester tester = new Tester(CLIENT_COUNT, MESSAGE_COUNT);
		try {
			tester.startTest();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
