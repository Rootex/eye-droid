package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import logger.LoggerService;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

public class Output {
	
	private String path;
	//Saving image to SD card in the click of the capture button 
	//in CaptureActivity
	public void saveFrame(Bitmap image) {
		try {
			String root = Environment.getExternalStorageDirectory().toString();
			File dir = new File(root + "/surv_images");
			dir.mkdirs();
			Date date = new Date();
			CharSequence sequence = DateFormat.format("MM-dd-yy-hh-mm-ss",
					date.getTime());
			String imageName = "Image-" + sequence + ".jpg";
			File file = new File(dir, imageName);
			FileOutputStream fos;
			fos = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//Logging activities to the log file. Called in the LoggerService
	public void writeToFile(String msg){
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "log.txt");
		if(msg.length() > 0){
			try{
				if(root.canWrite()){
					FileWriter fileWriter = new FileWriter(file, false);
					BufferedWriter outWrite = new BufferedWriter(fileWriter);
					outWrite.write(msg);
					Log.d(LoggerService.class.getName(), "Log created");
					outWrite.close();
				}
				
			}catch(IOException x){
				Log.e("Tag", "Could not write file "+x.getMessage());
			}
		}
	}
	
	//save detected face to file
	public void saveFace(Bitmap image) {
		try {
			String root = Environment.getExternalStorageDirectory().toString();
			File dir = new File(root + "/kivy/Viewer/Images");
			dir.mkdirs();
			Date date = new Date();
			CharSequence sequence = DateFormat.format("MM-dd-yy-h-mm-ss",
					date.getTime());
			String imageName = "Image-" + sequence + ".jpg";
			path = root +"/org.plaix.viewer/Images/"+imageName;
			File file = new File(dir, imageName);
			FileOutputStream fos;
			fos = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public String getPath(){
		return path;
	}
}
