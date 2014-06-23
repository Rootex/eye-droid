package database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LogHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "log.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_LOG = "log";
	private static final String ID_COLUMN = "id";
	private static final String DATE_COLUMN = "date";
	private static final String COMMENT_COLUMN = "comment";
	private static final String CREATE_DATABASE = "create table "
			+ TABLE_LOG + "(" + ID_COLUMN 
			+ " integer primary key autoincrement, "
			+ DATE_COLUMN + " text not null, "
			+ COMMENT_COLUMN + " text)";
	
	private String date;
	private String comment;
	
	public LogHelper(Context context, String date, String comment){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.date = date;
		this.comment = comment;
	}
	
	public LogHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_DATABASE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
		onCreate(database);
	}
	
	public void insertComment(){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE_COLUMN, date);
		values.put(COMMENT_COLUMN, comment);
		db.insert(TABLE_LOG, null, values);
		db.close();
	}
	
	//will be implemented when needed.
	public String fetchRow(int id){
		return null;
	}
	
	public List<LogModel> getAllEvents() {
	    List<LogModel> logList = new ArrayList<LogModel>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_LOG;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            LogModel log = new LogModel();
	            log.setId(Integer.parseInt(cursor.getString(0)));
	            log.setDate(cursor.getString(1));
	            log.setComment(cursor.getString(2));
	            // Adding contact to list
	            logList.add(log);
	        } while (cursor.moveToNext());
	    }
	    db.close();
	    // return contact list
	    return logList;
	}
	
	public void CleanDB(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOG, null, null);
		db.close();
	}

}
