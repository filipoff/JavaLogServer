package bg.sofia.uni.fmi.core.java;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

public class Logger {

	private static final SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

	private String logFileName;

	private Queue<String> messages;

	private LoggerThread loggerThread;

	private class LoggerThread extends Thread {

		@Override
		public void run() {

			FileOutputStream logFileOS = null;
			try {
				logFileOS = new FileOutputStream(logFileName, true);
				PrintWriter logFileWriter = new PrintWriter(logFileOS);
				while (true) {

					synchronized (messages) {
						messages.wait();
						String message = null;
						while ((message = messages.poll()) != null) {
							logFileWriter.println(message);
						}
					}
				}

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (logFileOS != null) {
					try {
						logFileOS.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	Logger(String logFileName) {
		this.logFileName = logFileName;
		this.messages = new LinkedList<>();
		this.loggerThread = new LoggerThread();
		loggerThread.start();
	}

	public synchronized void writeToLog(String name, String message) {

		Calendar now = Calendar.getInstance();

		String timeStamp = TIME_STAMP_FORMAT.format(now.getTime());
		synchronized (messages) {
			messages.add(timeStamp + " " + name + ": " + message);
			messages.notify();
		}
	}

	public void close() {
		loggerThread.interrupt();
	}

}