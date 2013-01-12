package com.example.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.model.TodoModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TodoContentProvider{
	
	  private SQLiteDatabase database;
	  private TodoSQLiteOpenHelper dbHelper;
	  private static final String WHERE_IDENTIFY_ITEM = TodoSQLiteOpenHelper.COLUMN_ID + "=?";
	  private String[] spalten = {TodoSQLiteOpenHelper.COLUMN_ID,
			  TodoSQLiteOpenHelper.COLUMN_NAME,TodoSQLiteOpenHelper.COLUMN_DESCRIPTION,TodoSQLiteOpenHelper.COLUMN_FAELLIGKEIT,TodoSQLiteOpenHelper.COLUMN_ERLEDIGT,TodoSQLiteOpenHelper.COLUMN_FAVOURITE };

	  public TodoContentProvider(Context context) {
	    dbHelper = new TodoSQLiteOpenHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public TodoModel createTodo(TodoModel todo) {
		    long insertId = database.insert(TodoSQLiteOpenHelper.TABLE_TODO, null,
		    		createContentValues(todo));
		    Cursor cursor = database.query(TodoSQLiteOpenHelper.TABLE_TODO,
		        spalten, TodoSQLiteOpenHelper.COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    TodoModel todomodel = cursorToDo(cursor);
		    cursor.close();
		    return todomodel;
	  }

	  public void deleteTodo(TodoModel todo) {
		    long id = todo.getId();
		    database.delete(TodoSQLiteOpenHelper.TABLE_TODO, TodoSQLiteOpenHelper.COLUMN_ID
		        + " = " + id, null);
	  }
	  
	  public void updateTodo(TodoModel todo){
		  database.update(TodoSQLiteOpenHelper.TABLE_TODO, createContentValues(todo), WHERE_IDENTIFY_ITEM,
					new String[] { String.valueOf(todo.getId()) });
	  }

	  public List<TodoModel> getAllTodos() {
	    List<TodoModel> todos = new ArrayList<TodoModel>();

	    Cursor cursor = database.query(TodoSQLiteOpenHelper.TABLE_TODO,
	        spalten, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	TodoModel todo = cursorToDo(cursor);
	    	todos.add(todo);
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    return todos;
	  }

	  private TodoModel cursorToDo(Cursor cursor) {
		TodoModel todo = new TodoModel();
	    todo.setId(cursor.getLong(0));
	    todo.setName(cursor.getString(1));
	    todo.setDescription(cursor.getString(2));
	    todo.setDate(new Date(Date.parse(cursor.getString(3))));
	    todo.setErledigt(cursor.getInt(4));
	    todo.setFavourite(cursor.getInt(5));
	    return todo;
	  }
	  
	  private ContentValues createContentValues(TodoModel todo){
		  	ContentValues values = new ContentValues();
		    values.put(TodoSQLiteOpenHelper.COLUMN_NAME, todo.getName());
		    values.put(TodoSQLiteOpenHelper.COLUMN_DESCRIPTION, todo.getDescription());
		    values.put(TodoSQLiteOpenHelper.COLUMN_FAELLIGKEIT, todo.getDate().toGMTString());
		    values.put(TodoSQLiteOpenHelper.COLUMN_ERLEDIGT, todo.getErledigt());
		    values.put(TodoSQLiteOpenHelper.COLUMN_FAVOURITE, todo.getFavourite());
		    return values;
	  }

}
