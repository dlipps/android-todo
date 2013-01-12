package com.example.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class TodoSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_TODO = "todo";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_FAELLIGKEIT = "faelligkeit";
	public static final String COLUMN_ERLEDIGT = "erledigt";
	public static final String COLUMN_FAVOURITE = "favourite";
	

	private static final String DATABASE_NAME = "todo.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
	      + TABLE_TODO + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_NAME
	      + " text not null" + COLUMN_DESCRIPTION
	      + " text"+ COLUMN_FAELLIGKEIT
	      + " text not null"+ COLUMN_ERLEDIGT
	      + " integer"+ COLUMN_FAVOURITE
	      + " integer);";

	public  TodoSQLiteOpenHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	 }

	 @Override
	public void onCreate(SQLiteDatabase database) {
		 database.execSQL(DATABASE_CREATE);
		 }

	 @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.w( TodoSQLiteOpenHelper.class.getName(),
		        "Die Datenbank wird upgradet von Version " + oldVersion + " zu Version "
		            + newVersion + " dabei gehen alle alten Daten verloren!!!");
		  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		  onCreate(db);
		  }

}
