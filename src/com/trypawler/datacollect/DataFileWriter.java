package com.trypawler.datacollect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import android.util.Log;
import android.widget.Toast;

public class DataFileWriter {
	
	private String preFileName = "/sdcard/download/PawlerData-";
	private String postFileName = ".txt";
	private String header;
	
	private File outpFile;
	private FileOutputStream fOut;
	private FileWriter fWriter;
	private OutputStreamWriter osw;
	
	boolean fileOpen;
	
	public DataFileWriter(String name, String header) {
		this.header = header;
		preFileName = preFileName + name + "-";
		openFile();
	}
	
	public void writeLine(String line) {
		try {
			osw.append(line+"\n");
			osw.flush();
		} catch (IOException ex) {
			Log.i(getClass().getSimpleName(), "Exception writing file: " + ex.getMessage());
			fileOpen = false;
		}
	}

	public boolean isOpen() {
		return fileOpen;
	}
	
	public void openFile() {
		Calendar dt = Calendar.getInstance();
		String fn = preFileName;
		fn = fn + (dt.get(Calendar.MONTH)+1) + "-"+ dt.get(Calendar.DAY_OF_MONTH);
		fn = fn + "-" + dt.get(Calendar.HOUR_OF_DAY) + "." + dt.get(Calendar.MINUTE) + "." + dt.get(Calendar.SECOND);
		fn = fn + postFileName;

		try {
			outpFile = new File(fn);
			fWriter = new FileWriter(outpFile);
		
			fOut = new FileOutputStream(outpFile);
			osw = new OutputStreamWriter(fOut);
			osw.write(header+"\n");
			osw.flush();
			
		} catch (IOException e) {
			Log.i(getClass().getSimpleName(), "Exception creating file: " + e.getMessage());
			fileOpen = false;
		}
		fileOpen = true;
	}
	
	public void closeFile() {
		try {
			fWriter.flush();
			fWriter.close();
		} catch (IOException e) {
			Log.i(getClass().getSimpleName(), "Exception closing file: " + e.getMessage());
		}
		fileOpen = false;
	}
}
