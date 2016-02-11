package bg.sofia.uni.fmi.core.java;

public class Tester {

	public static void main(String[] args) {

		Client client = new Client("localhost", 8000, "Tester1");
		client.connect();
		client.sendMessage("pink floyd");
//		client.disconnect();
	}

}
