package com.example.accessor;

import java.util.List;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import com.example.model.ITodoCRUDAccessor;
import com.example.model.TodoModel;

import android.app.Activity;
import android.util.Log;

public class ResteasyTodoCRUDAccessor implements ITodoCRUDAccessor {
	
	protected static String logger = ResteasyTodoCRUDAccessor.class.getSimpleName();
	protected Activity activity;
	
	/**
	 * the client via which we access the rest web interface provided by the server
	 */
	private ITodoCRUDAccessor restClient;
	
	public ResteasyTodoCRUDAccessor(String baseUrl) {

		Log.i(logger,"initialising restClient for baseUrl: " + baseUrl);

		this.restClient = ProxyFactory.create(ITodoCRUDAccessor.class,
				baseUrl,
				new ApacheHttpClient4Executor());
		
		Log.i(logger,"initialised restClient: " + restClient + " of class " + restClient.getClass());
	}

	@Override
	public List<TodoModel> getTodos() {
		Log.i(logger, "readAllItems()");

		List<TodoModel> todoliste = restClient.getTodos();
		
		Log.i(logger, "readAllItems(): got: " + todoliste);
	
		return todoliste;
	}

	@Override
	public TodoModel addTodo(TodoModel todo) {
		Log.i(logger, "createItem(): send: " + todo);

		restClient.addTodo(todo);
		
		Log.i(logger, "createItem(): got: " + todo);
	
		return todo;
	}

	@Override
	public boolean deleteTodo(long todoId) {
		Log.i(logger, "deleteItem(): send: " + todoId);

		boolean deleted = restClient.deleteTodo(todoId);
		
		Log.i(logger, "deleteItem(): got: " + deleted);
	
		return deleted;
	}

	@Override
	public TodoModel updateTodo(TodoModel todo) {
		Log.i(logger, "updateItem(): send: " + todo);

		restClient.updateTodo(todo);
		
		Log.i(logger, "updateItem(): got: " + todo);
	
		return todo;
	}
}
