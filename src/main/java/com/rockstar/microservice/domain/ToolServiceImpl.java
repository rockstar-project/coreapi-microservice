package com.rockstar.microservice.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rockstar.microservice.service.NotFoundException;
import com.rockstar.microservice.service.NotUniqueException;
import com.rockstar.microservice.service.ToolService;

@Service
public class ToolServiceImpl implements ToolService {
	
	@Autowired private ToolRepository toolRepository;
	
	public Collection<Tool> getTools() {
		return this.toolRepository.findAll();
	}
	
	public Tool getTool(String toolId) {
		return this.retrieveToolById(toolId);
	}
	
	public Tool createTool(Tool tool) {
		Tool updatedTool = null;
		if (tool != null) {
			this.validateUniqueTool(tool);
			updatedTool = this.toolRepository.save(tool);
		}
		return updatedTool;
	}
	
	public void updateTool(Tool tool) {
		Tool currentTool = null;
		Boolean modifyFlag = false;
		currentTool = this.retrieveToolById(tool.getId());

		if (StringUtils.hasText(tool.getTitle())) {
			currentTool.setTitle(tool.getTitle());
			modifyFlag = true;
		}
		if (StringUtils.hasText(tool.getDescription())) {
			currentTool.setDescription(tool.getDescription());
			modifyFlag = true;
		}
		if (StringUtils.hasText(tool.getThumbnail())) {
			currentTool.setThumbnail(tool.getThumbnail());
			modifyFlag = true;
		}
		if (tool.getDisplayOrder() != null) {
			currentTool.setDisplayOrder(tool.getDisplayOrder());
		}
		if (tool.getEnabled() != null) {
			currentTool.setEnabled(tool.getEnabled());
		}
		if (tool.getVersions() != null && !tool.getVersions().isEmpty()) {
			currentTool.setVersions(tool.getVersions());
		}
		if (modifyFlag) {
			this.toolRepository.save(currentTool);
		}
	}
	
	public void deleteTool(String toolId) {
		this.toolRepository.delete(this.retrieveToolById(toolId));
	}
	
	private void validateUniqueTool(Tool tool) {
		Optional<Tool> persistedToolOptional = null;
		
		if (tool != null) {
			persistedToolOptional = this.toolRepository.findById(tool.getId());
			if (persistedToolOptional.isPresent()) {
				throw new NotUniqueException("tool");
			}
		}
	}
	
	private Tool retrieveToolById(String toolId) {
		Optional<Tool> toolOptional = this.toolRepository.findById(toolId);
		if (!toolOptional.isPresent()) {
			throw new NotFoundException("tool");
		}
		return toolOptional.get();
	}
}