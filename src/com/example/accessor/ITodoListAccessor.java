package com.example.accessor;

import android.widget.ListAdapter;

import com.example.model.TodoModel;

public interface ITodoListAccessor {
	
	public void addTodo(TodoModel todo);
	
	public ListAdapter getAdapter();
	
	public void updateTodo(TodoModel todo);
	
	public void deleteTodo(TodoModel todo);
	
	public TodoModel getTodo(int todoPosition, long todoId);
	
	public void finalise();

}
