package com.example.accessor;

import com.example.android_todo.TodoAllActivity;
import com.example.model.TodoModel;

import android.app.Activity;
import android.content.Intent;

public class IntentTodoAccessor implements ITodoAccessor {
	
	private TodoModel todo;
	private Activity activity;
	
	protected Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public TodoModel readTodo() {
		if (this.todo == null) {
			this.todo = (TodoModel) getActivity().getIntent()
					.getSerializableExtra(TodoAllActivity.ARG_TODO_OBJECT);
		}

		return this.todo;
	}

	public void writeTodo() {
		// and return to the calling activity
		Intent returnIntent = new Intent();

		// set the item
		returnIntent.putExtra(TodoAllActivity.ARG_TODO_OBJECT, this.todo);

		// set the result code
		getActivity().setResult(TodoAllActivity.RESPONSE_TODO_EDITED,
				returnIntent);
	}

	public boolean hasTodo() {
		return readTodo() != null;
	}

	public void createTodo() {
		this.todo = new TodoModel(-1,"","",null,0,0);
	}

	public void deleteTodo() {
		// and return to the calling activity
		Intent returnIntent = new Intent();

		// set the item
		returnIntent.putExtra(TodoAllActivity.ARG_TODO_OBJECT, this.todo);

		// set the result code
		getActivity().setResult(TodoAllActivity.RESPONSE_TODO_DELETED,
				returnIntent);
	}

}
