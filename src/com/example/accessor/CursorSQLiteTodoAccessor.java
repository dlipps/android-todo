package com.example.accessor;


import com.example.android_todo.R;
import com.example.model.TodoModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class CursorSQLiteTodoAccessor extends SQLiteTodoAccessor {
	
	protected static final String logger = CursorSQLiteTodoAccessor.class
			.getName();

	private SimpleCursorAdapter adapter;

	private Cursor cursor;

	@Override
	public ListAdapter getAdapter() {

		prepareSQLiteDatabase();

		SQLiteQueryBuilder querybuilder = new SQLiteQueryBuilder();
		querybuilder.setTables(SQLiteTodoAccessor.TABLE_TODO);
		// we specify all columns
		String[] asColumsToReturn = { SQLiteTodoAccessor.COLUMN_ID,
				SQLiteTodoAccessor.COLUMN_NAME};
		// we specify an ordering
		String ordering = COLUMN_ID + " ASC";

		this.cursor = querybuilder.query(this.db, asColumsToReturn, null, null,
				null, null, ordering);

		getActivity().startManagingCursor(this.cursor);

		/*
		 * create a cursor adapter that maps the "name" column in the db to the
		 * itemName element in the view
		 * 
		 * (i.e. using this adapter there is no need to create DataItem objects
		 * for all items that are contained in the db)
		 */
		this.adapter = new SimpleCursorAdapter(getActivity(), R.layout.todoall_item,
				this.cursor,
				new String[] { SQLiteTodoAccessor.COLUMN_NAME },
				new int[] { R.id.todoall_itemName });

		return this.adapter;
	}

	/**
	 * manipulate the db. In all cases, the cursor needs to be requeried in
	 * order for the view to be updated
	 */

	@Override
	public void addTodo(TodoModel todo) {
		super.addTodoToDb(todo);
		this.cursor.requery();
	}

	@Override
	public void updateTodo(TodoModel todo) {
		super.updateTodoInDb(todo);
		this.cursor.requery();
	}

	@Override
	public void deleteTodo(TodoModel todo) {
		super.removeTodoFromDb(todo);
		this.cursor.requery();
	}

	@Override
	public TodoModel getTodo(int itemPosition, long itemId) {
		return createTodoFromCursor((Cursor) this.adapter.getItem(itemPosition));
	}

}
