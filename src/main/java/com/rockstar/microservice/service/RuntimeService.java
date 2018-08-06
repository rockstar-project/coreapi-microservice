package com.rockstar.microservice.service;

import java.util.Collection;

import com.rockstar.microservice.domain.Runtime;

public interface RuntimeService {
	
	public Collection<Runtime> getRuntimes();
	public Runtime getRuntime(String identifier);
	public Runtime createRuntime(Runtime runtime);
	public void updateRuntime(Runtime runtime);
	public void deleteRuntime(String identifier);

}
