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

		private PrintWriter logFileWriter;
		private boolean shouldFlush = false;

		public void flush() {
			synchronized (messages) {
				shouldFlush = true;
				messages.notify();
			}
		}

		@Override
		public void run() {

			logFileWriter = null;
			try {
				logFileWriter = new PrintWriter(new FileOutputStream(logFileName, true));

				while (true) {

					String message = null;

					synchronized (messages) {

						message = messages.poll();

						if (message == null) {

							if (shouldFlush == true) {
								logFileWriter.flush();
								shouldFlush = false;
							}
							messages.wait();
							message = messages.poll();
						}
					}

					if (message != null) {
						System.out.println("Logger thread is writing \"" + message + "\" to file.");
						logFileWriter.println(message);
					}
				}

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (logFileWriter != null) {
					logFileWriter.close();
				}
			}
		}
	}

	Logger(String logFileName) {
		this.logFileName = logFileName;
		this.messages = new LinkedList<>();
		this.loggerThread = new LoggerThread();
		this.loggerThread.start();
	}

	public void writeToLog(String name, String message) {

		Calendar now = Calendar.getInstance();

		String timeStamp = TIME_STAMP_FORMAT.format(now.getTime());

		synchronized (messages) {
			messages.add(timeStamp + " " + name + ": " + message);
			messages.notify();
		}
	}

	public void flush() {
		loggerThread.flush();
	}

	public void stop() {
		synchronized (messages) {
			loggerThread.interrupt();
		}
	}

}
