package com.example.accessor;

import com.example.model.TodoModel;


public interface ITodoAccessor {
	
	public TodoModel readTodo();
	
	public void writeTodo();
	
	public boolean hasTodo(); 
	
	public void createTodo();
	
	public void deleteTodo();

}
