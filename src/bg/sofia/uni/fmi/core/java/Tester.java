package bg.sofia.uni.fmi.core.java;

public class Tester {

	public static void main(String[] args) throws InterruptedException {

		Client client1 = new Client("localhost", 8000, "Tester1");
		client1.connect();
		Client client2 = new Client("localhost", 8000, "Tester2");
		client2.connect();
		Client client3 = new Client("localhost", 8000, "Tester3");
		client3.connect();
		Client client4 = new Client("localhost", 8000, "Tester4");
		client4.connect();
		Client client5 = new Client("localhost", 8000, "Tester5");
		client5.connect();

		Thread t1 = new Thread() {
			public void run() {

				for (int i = 0; i < 100; i++) {
					client1.sendMessage("msg " + i);
				}
			}
		};

		Thread t2 = new Thread() {
			public void run() {

				for (int i = 0; i < 100; i++) {
					client2.sendMessage("msg " + i);
				}
			}
		};

		Thread t3 = new Thread() {
			public void run() {

				for (int i = 0; i < 100; i++) {
					client3.sendMessage("msg " + i);
				}
			}
		};

		Thread t4 = new Thread() {
			public void run() {

				for (int i = 0; i < 100; i++) {
					client4.sendMessage("msg " + i);
				}
			}
		};

		Thread t5 = new Thread() {
			public void run() {

				for (int i = 0; i < 100; i++) {
					client5.sendMessage("msg " + i);
				}
			}
		};
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		client1.disconnect();
		client2.disconnect();
		client3.disconnect();
		client4.disconnect();
		client5.disconnect();
	}
}
