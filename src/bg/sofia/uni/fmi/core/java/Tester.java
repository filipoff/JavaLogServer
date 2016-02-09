package bg.sofia.uni.fmi.core.java;

public class Tester {

	public static void main(String[] args) {

		Client client = new Client("localhost", 8000);
		client.connect();
	}

}
