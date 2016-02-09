package bg.sofia.uni.fmi.core.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientConnectionThread extends Thread {

	private Socket socket;

	private String logFileName;

	private static final SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

	public ClientConnectionThread(Socket socket, String logfileName) {
		this.socket = socket;
		this.logFileName = logfileName;
	}

	public Socket getSocket() {
		return socket;
	}

	private void writeToLog(String message) throws FileNotFoundException {
		Calendar now = Calendar.getInstance();

		String timeStamp = TIME_STAMP_FORMAT.format(now.getTime());

		FileOutputStream logFileOS = new FileOutputStream(this.logFileName, true);

		PrintWriter logFileWriter = new PrintWriter(logFileOS);

		logFileWriter.println(timeStamp + " " + java.lang.management.ManagementFactory.getRuntimeMXBean().getName()
				+ " : " + message);

		logFileWriter.flush();
		logFileWriter.close();

	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

			while (true) {

				String message = reader.readLine();

				if (message == null) {
					break;
				}
				writeToLog(message);
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
				// Nothing that we can do
			}
		}
	}
}
