package com.example.model;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/todo")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ITodoCRUDAccessor {
	
	@GET
	public List<TodoModel> getTodos();
	
	@POST
	public TodoModel addTodo(TodoModel todo);

	@DELETE
	@Path("/{todoId}")
	public boolean deleteTodo(@PathParam("todoId") long todoId);

	@PUT
	public TodoModel updateTodo(TodoModel todo);	

}
