package com.example.accessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.model.ITodoCRUDAccessor;
import com.example.model.TodoModel;

public class SQLiteTodoCRUDAccessor implements ITodoCRUDAccessor {
	
	private static final String DBNAME = "todo.db";
	private static final int INITIAL_DBVERSION = 0;

	public static final String TABLE_TODO = "todo";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_FAELLIGKEIT = "faelligkeit";
	public static final String COLUMN_ERLEDIGT = "erledigt";
	public static final String COLUMN_FAVOURITE = "favourite";
	
	private static final String DATABASE_CREATE = "create table "
		      + TABLE_TODO + " (" + COLUMN_ID
		      + " integer primary key autoincrement, " + COLUMN_NAME
		      + " text not null, " + COLUMN_DESCRIPTION
		      + " real, "+ COLUMN_FAELLIGKEIT
		      + " text not null, "+ COLUMN_ERLEDIGT
		      + " integer, "+ COLUMN_FAVOURITE
		      + " integer);";
	
	private static final String WHERE_IDENTIFY_ITEM = COLUMN_ID + "=?";
	
	protected SQLiteDatabase db;
	private List<TodoModel> todos = new ArrayList<TodoModel>();
	private Activity activity;
	protected static final String logger = SQLiteTodoCRUDAccessor.class
			.getName();
	
	public SQLiteTodoCRUDAccessor(Activity activity){
		this.activity=activity;
		prepareSQLiteDatabase();
		readOutTodosFromDatabase();
	}

	@Override
	public List<TodoModel> getTodos() {
		return todos;
	}

	@Override
	public TodoModel addTodo(TodoModel todo) {
		addTodoToDb(todo);
		return todo;
	}

	@Override
	public boolean deleteTodo(long todoId) {
		removeTodoFromDb(todoId);
		return true;
	}

	@Override
	public TodoModel updateTodo(TodoModel todo) {
		updateTodoInDb(todo);
//		lookupItem(todo).updateFrom(todo);
		return todo;
	}
	
	public void setActivity(Activity activity){
		this.activity=activity;
	}
	
	private Activity getActivity(){
		return this.activity;
	}
	
	protected void addTodoToDb(TodoModel todo) {
		Log.i(logger, "addItemToDb(): " + todo);
		ContentValues insertTodo = createDBTodo(todo);
		long todoId = this.db.insert(TABLE_TODO, null, insertTodo);
		Log.i(logger, "addItemToDb(): got new item id after insertion: "
				+ todoId);
		todo.setId(todoId);
	}
	
	protected void updateTodoInDb(TodoModel todo) {
		Log.i(logger, "updateItemInDb(): " + todo);
		this.db.update(TABLE_TODO, createDBTodo(todo), WHERE_IDENTIFY_ITEM,
				new String[] { String.valueOf(todo.getId()) });
		Log.i(logger, "updateItemInDb(): update has been carried out");
	}
	protected void removeTodoFromDb(long todoId) {
//		Log.i(logger, "removeItemFromDb(): " + todo);
		this.db.delete(TABLE_TODO, WHERE_IDENTIFY_ITEM,
				new String[] { String.valueOf(todoId) });
		Log.i(logger, "removeItemFromDb(): deletion in db done");
	}
	
	private ContentValues createDBTodo(TodoModel todo) {
		ContentValues values = new ContentValues();
	    values.put(COLUMN_NAME, todo.getName());
	    values.put(COLUMN_DESCRIPTION, todo.getDescription());
	    values.put(COLUMN_FAELLIGKEIT, todo.getDate().getTime());
	    values.put(COLUMN_ERLEDIGT, todo.getErledigt());
	    values.put(COLUMN_FAVOURITE, todo.getFavourite());
	    return values;
	}
	
	protected void prepareSQLiteDatabase() {

		this.db = getActivity().openOrCreateDatabase(DBNAME,
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		Log.d(logger, "db version is: " + db.getVersion());
		if (this.db.getVersion() == INITIAL_DBVERSION) {
			Log.i(logger,
					"the db has just been created. Need to create the table...");
			db.setLocale(Locale.getDefault());
			db.setLockingEnabled(true);
			db.setVersion(INITIAL_DBVERSION + 1);
			db.execSQL(DATABASE_CREATE);
		} else {
			Log.i(logger, "the db exists already. No need for table creation.");
		}

	}
	
	protected void readOutTodosFromDatabase() {
		SQLiteQueryBuilder querybuilder = new SQLiteQueryBuilder();
		querybuilder.setTables(TABLE_TODO);
		String[] asColumsToReturn = { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
				COLUMN_FAELLIGKEIT, COLUMN_ERLEDIGT,COLUMN_FAVOURITE };
		String ordering = COLUMN_ID + " ASC";

		Cursor c = querybuilder.query(this.db, asColumsToReturn, null, null,
				null, null, ordering);

		Log.i(logger, "getAdapter(): got a cursor: " + c);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			this.todos.add(createTodoFromCursor(c));
			c.moveToNext();
		}

		Log.i(logger, "readOutItemsFromDatabase(): read out items: " + this.todos);
	}
	
	protected TodoModel createTodoFromCursor(Cursor c) {
		TodoModel currentTodo = new TodoModel();
		currentTodo.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
		currentTodo.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
		currentTodo.setDescription(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
		currentTodo.setDate(new Date(c.getLong(c.getColumnIndex(COLUMN_FAELLIGKEIT))));
		currentTodo.setErledigt(c.getInt(c.getColumnIndex(COLUMN_ERLEDIGT)));
		currentTodo.setFavourite(c.getInt(c.getColumnIndex(COLUMN_FAVOURITE)));
		return currentTodo;
	}
	
//	private TodoModel lookupItem(TodoModel todo) {
//		for (TodoModel current : this.todos) {
//			if (current.getId() == todo.getId()) {
//				return current;
//			}
//		}
//		return null;
//	}
	
	public void finalise() {
		this.db.close();
		Log.i(logger, "db has been closed");
	}

}
