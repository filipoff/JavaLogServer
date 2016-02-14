package bg.sofia.uni.fmi.core.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tester {

	private int messageCount;

	private List<TesterThread> threads;

	private class TesterThread extends Thread {

		private Client client;

		public TesterThread(String name) {
			this.client = new Client("localhost", 8000, name);
		}

		@Override
		public void run() {

			Random rand = new Random();

			for (int i = 0; i < messageCount; i++) {
				int interval = rand.nextInt(1000);
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client.sendMessage("message " + i);
			}

			while (client.pendingMessagesCount() > 0) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			client.disconnect();

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

		int counter = 0;
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
			counter++;
		}
		System.out.println("Number of joined threads : " + counter);
	}

	public static void main(String[] args) {

		final int CLIENT_COUNT = 10;
		final int MESSAGE_COUNT = 100;
		Tester tester = new Tester(CLIENT_COUNT, MESSAGE_COUNT);
		try {
			tester.startTest();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("final reached.");
	}
}
