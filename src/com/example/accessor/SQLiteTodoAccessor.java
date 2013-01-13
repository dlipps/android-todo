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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.android_todo.R;
import com.example.model.TodoModel;

public class SQLiteTodoAccessor implements ITodoListAccessor {
	
	private Activity activity;
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
		      + " text, "+ COLUMN_FAELLIGKEIT
		      + " text not null, "+ COLUMN_ERLEDIGT
		      + " integer, "+ COLUMN_FAVOURITE
		      + " integer);";
	
	private static final String WHERE_IDENTIFY_ITEM = COLUMN_ID + "=?";
	
	private SQLiteDatabase db;
	protected static final String logger = SQLiteTodoAccessor.class
			.getName();
	
	private List<TodoModel> todos = new ArrayList<TodoModel>();
	private ArrayAdapter<TodoModel> adapter;
//	private SimpleCursorAdapter adapter;
	private Cursor cursor;
	
//	public SQLiteTodoAccessor(Activity activity){
//		this.activity = activity;
//		prepareSQLiteDatabase();
//		readOutTodosFromDatabase();
//	}
	

	protected Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void addTodo(TodoModel todo) {
		Log.i(logger, "TodoModel: "+todo);
		addItemToDb(todo);
		this.adapter.add(todo);
//		updateCursor();

		

	}

	@Override
	public ListAdapter getAdapter() {
		// TODO andere Bestandteile
		
		prepareSQLiteDatabase();
		readOutTodosFromDatabase();
		

		this.adapter = new ArrayAdapter<TodoModel>(getActivity(),
				R.layout.todoall_item, this.todos) {
			@Override
			public View getView(int position, View listItemView,
					ViewGroup parent) {
				Log.i(logger,
						"getView() has been invoked for item: "
								+ todos.get(position) + ", listItemView is: "
								+ listItemView);
				View layout = getActivity().getLayoutInflater().inflate(
						R.layout.todoall_item, null);
				
				TextView todoName =(TextView)layout.findViewById(R.id.todoall_itemName);
				todoName.setText(todos.get(position).getName());
				TextView todoFaelligkeit=(TextView)layout.findViewById(R.id.todoall_itemFaelligkeit);
				todoFaelligkeit.setText(todos.get(position).getDate().toGMTString());
				return layout;
			}

		};
		this.adapter.setNotifyOnChange(true);

		return this.adapter;
		
//		SQLiteQueryBuilder querybuilder = new SQLiteQueryBuilder();
//		querybuilder.setTables(TABLE_TODO);
//		String[] asColumsToReturn = { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
//				COLUMN_FAELLIGKEIT, COLUMN_ERLEDIGT,COLUMN_FAVOURITE };
//		String ordering = COLUMN_ID + " ASC";
//
//		this.cursor = querybuilder.query(this.db, asColumsToReturn, null, null,
//				null, null, ordering);
//
//		getActivity().startManagingCursor(this.cursor);

		/*
		 * create a cursor adapter that maps the "name" column in the db to the
		 * itemName element in the view
		 * 
		 * (i.e. using this adapter there is no need to create DataItem objects
		 * for all items that are contained in the db)
		 */
//		this.adapter = new SimpleCursorAdapter(getActivity(), R.layout.todoall_item,
//				this.cursor,
//				new String[] {COLUMN_NAME, COLUMN_DESCRIPTION,
//			COLUMN_FAELLIGKEIT, COLUMN_ERLEDIGT,COLUMN_FAVOURITE },
//				new int[] {R.id.nameEditText, R.id.beschreibung, R.id.datumpicker, R.id.erledigtCheckBox,R.id.wichtigkeitBar});
//		this.adapter = new SimpleCursorAdapter(getActivity(), R.layout.todoall_item,
//				this.cursor,
//				new String[] {COLUMN_NAME, COLUMN_DESCRIPTION,
//			COLUMN_FAELLIGKEIT, COLUMN_ERLEDIGT,COLUMN_FAVOURITE },
//				new int[] {R.id.nameEditText, R.id.beschreibung, R.id.datumpicker, R.id.erledigtCheckBox,R.id.wichtigkeitBar});
//
//		return this.adapter;
	}

	@Override
	public void updateTodo(TodoModel todo) {
		updateItemInDb(todo);
		lookupItem(todo).updateFrom(todo);
//		updateCursor();
		this.adapter.notifyDataSetChanged();

	}

	@Override
	public void deleteTodo(TodoModel todo) {
		removeTodoFromDb(todo);
//		updateCursor();
		this.adapter.remove(lookupItem(todo));
	}

	@Override
	public TodoModel getTodo(int todoPosition, long todoId) {
		return adapter.getItem(todoPosition);
//		return createTodoFromCursor((Cursor) this.adapter.getItem(todoPosition));

	}

	@Override
	public void finalise() {
		this.db.close();
		Log.i(logger, "db has been closed");

	}
	
	private ContentValues createDBTodo(TodoModel todo) {
		ContentValues values = new ContentValues();
	    values.put(COLUMN_NAME, todo.getName());
	    values.put(COLUMN_DESCRIPTION, todo.getDescription());
	    values.put(COLUMN_FAELLIGKEIT, todo.getDate().toGMTString());
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
		currentTodo.setDate(new Date(c.getString(c.getColumnIndex(COLUMN_FAELLIGKEIT))));
		currentTodo.setErledigt(c.getInt(c.getColumnIndex(COLUMN_ERLEDIGT)));
		currentTodo.setFavourite(c.getInt(c.getColumnIndex(COLUMN_FAVOURITE)));


		return currentTodo;
	}
	
	protected void addItemToDb(TodoModel todo) {
		Log.i(logger, "addItemToDb(): " + todo);

		/**
		 * add the item to the db
		 */
		ContentValues insertTodo = createDBTodo(todo);
		long todoId = this.db.insert(TABLE_TODO, null, insertTodo);
		Log.i(logger, "addItemToDb(): got new item id after insertion: "
				+ todoId);
		todo.setId(todoId);
	}
	
	protected void updateItemInDb(TodoModel todo) {
		Log.i(logger, "updateItemInDb(): " + todo);
		this.db.update(TABLE_TODO, createDBTodo(todo), WHERE_IDENTIFY_ITEM,
				new String[] { String.valueOf(todo.getId()) });
		Log.i(logger, "updateItemInDb(): update has been carried out");
	}
	protected void removeTodoFromDb(TodoModel todo) {
		Log.i(logger, "removeItemFromDb(): " + todo);

		// we first delete the item
		this.db.delete(TABLE_TODO, WHERE_IDENTIFY_ITEM,
				new String[] { String.valueOf(todo.getId()) });
		Log.i(logger, "removeItemFromDb(): deletion in db done");
	}
	
	public List<TodoModel> getTodos() {
		return this.todos;
	}
	
	private void updateCursor(){
		if(cursor!=null){
			cursor.requery();
		}
	}
	
	private TodoModel lookupItem(TodoModel todo) {
		for (TodoModel current : this.todos) {
			if (current.getId() == todo.getId()) {
				return current;
			}
		}
		return null;
	}

}
