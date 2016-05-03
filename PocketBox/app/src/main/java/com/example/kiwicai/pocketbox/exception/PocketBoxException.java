package com.example.kiwicai.pocketbox.exception;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hongbao on 2016/3/22.
 */
public class PocketBoxException extends Exception implements FixException {

	private int errno; // error number
	private String errorMsg; // error message

	// Constructor
	public PocketBoxException(int errno) {
		this.errno = errno;
	}
	
	public int getErrno() {
		return errno;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	// Fix method
	@Override
	public void fix(int errno) {
		PBExceptionHandler exceptionHandler = new PBExceptionHandler();
		switch (errno) {
		case 1:
			errorMsg = "Cannot start login activity.";
			exceptionHandler.fixLogin(1);
			break;
		case 2:
			errorMsg = "Cannot start register activity.";
			exceptionHandler.fixRegister(2);
			break;
		case 3:
			errorMsg = "Cannot return to main activity.";
			exceptionHandler.fixReturn(3);
			break;
		case 4:
			errorMsg = "Cannot start chat activity.";
			exceptionHandler.fixChat(4);
			break;
		case 5:
			errorMsg = "Cannot connect to server.";
			exceptionHandler.fixReturn(5);
			break;
		}

	}
	public void createExceptionLog(Context context) {
		try {
			String FILENAME = "ExceptionLog.txt";
			File path = context.getFilesDir();

			File file = new File(path, FILENAME);
			FileWriter out = new FileWriter(file, true);

			StringBuilder message = new StringBuilder().append(errno).append("\t").append(errorMsg).append("\t").append("\n");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			out.write(df.format(new Date()));
			out.write(message.toString());

			System.out.println("writing log");

			out.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
