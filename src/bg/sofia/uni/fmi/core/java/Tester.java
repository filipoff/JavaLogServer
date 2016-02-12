package bg.sofia.uni.fmi.core.java;

public class Tester {

	public static void main(String[] args) {

		Client client = new Client("localhost", 8000, "Tester1");
		Client client2 = new Client("localhost", 8000, "Tester2");
		try {
			client.connect();
			client2.connect();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread t1 = new Thread() {
			public void run() {
				int i = 1;
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					client.sendMessage("msg " + i);
					i++;
				}
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				int i = 1;
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					client2.sendMessage("msg " + i);
					i++;
					// client.disconnect();
				}
			}
		};
		t1.start();
		t2.start();
	}
}
