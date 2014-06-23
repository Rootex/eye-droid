package logger;

import java.util.Date;

import database.LogHelper;

//import io.Output;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class LoggerService extends Service{

	private static final String start = "start";
	private static final String capture = "capture";
	private static final String face = "face";
	private String msg;
	//private Output output = new Output();
	
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(LoggerService.class.getName(), "Service created!");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		//Log.d(LoggerService.class.getName(), "Logger started");
		//Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		
		//Extras messages sent together with intent, and need to be retireved
		Bundle extras = intent.getExtras();
		if(extras.containsKey(start))
			msg = extras.getString(start);
		else if(extras.containsKey(capture))
			msg = extras.getString(capture);
		else if(extras.containsKey(face))
			msg = extras.getString(face);
		else
			msg = "Null";
		
		LogHelper database = new LogHelper(this, getDate(), msg);
		database.insertComment();
		
		//output.writeToFile(msg);
		Log.d(LoggerService.class.getName(), msg);
		
		//Not sticky allows user to control when service is started.
		return Service.START_NOT_STICKY;
	}
	
	//stoping the logger
	@Override
	public void onDestroy(){
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(LoggerService.class.getName(), "Logger destroyed");
		super.onDestroy();	
	}
	
	public String getDate(){
		Date date = new Date();
		CharSequence sequence = DateFormat.format("MM-dd-yy hh-mm-ss", date.getTime());
		return sequence.toString();
	}

}
