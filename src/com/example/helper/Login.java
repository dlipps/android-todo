package com.example.helper;

import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;



public class Login implements ILogin{
	
	private ILogin loginClient;
	
	public Login(){
		loginClient=ProxyFactory.create(ILogin.class,
				"http://10.0.2.2:8080/TodoWebapp/",
				new ApacheHttpClient4Executor());
	}

	@Override
	public boolean login(List<String> login) {
		boolean log = loginClient.login(login);
		return log;
	}

}
