package com.example.helper;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;



@Path("/login")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ILogin {
	
	@POST
	public boolean login(List<String> login);


}
