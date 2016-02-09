package bg.sofia.uni.fmi.core.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private String host;
	private int port;

	Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() {
		try (Socket socket = new Socket(host, port);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				Scanner console = new Scanner(System.in)) {

			String consoleInput = null;
			while ((consoleInput = console.nextLine()) != null) {
				out.println(consoleInput);
				out.flush();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
