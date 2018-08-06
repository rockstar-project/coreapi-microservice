package com.rockstar.microservice.service;

import java.util.Collection;

import com.rockstar.microservice.domain.Tool;

public interface ToolService {

	public Collection<Tool> getTools();
	public Tool getTool(String identifier);
	public Tool createTool(Tool tool);
	public void updateTool(Tool tool);
	public void deleteTool(String identifier);
}
