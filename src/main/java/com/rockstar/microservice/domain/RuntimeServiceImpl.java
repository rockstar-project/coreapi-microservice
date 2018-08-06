package com.rockstar.microservice.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rockstar.microservice.service.NotFoundException;
import com.rockstar.microservice.service.NotUniqueException;
import com.rockstar.microservice.service.RuntimeService;

@Service
public class RuntimeServiceImpl implements RuntimeService {
	
	@Autowired private RuntimeRepository runtimeRepository;
	
	public Collection<Runtime> getRuntimes() {
		return this.runtimeRepository.findAll();
	}
	
	public Runtime getRuntime(String runtimeId) {
		return this.retrieveRuntimeById(runtimeId);
	}
	
	public Runtime createRuntime(Runtime runtime) {
		Runtime updatedRuntime = null;
		if (runtime != null) {
			this.validateUniqueRuntime(runtime);
			updatedRuntime = this.runtimeRepository.save(runtime);
		}
		return updatedRuntime;
	}
	
	public void updateRuntime(Runtime runtime) {
		Runtime currentRuntime = null;
		Boolean modifyFlag = false;
		currentRuntime = this.retrieveRuntimeById(runtime.getId());

		if (StringUtils.hasText(runtime.getTitle())) {
			currentRuntime.setTitle(runtime.getTitle());
			modifyFlag = true;
		}
		if (StringUtils.hasText(runtime.getDescription())) {
			currentRuntime.setDescription(runtime.getDescription());
			modifyFlag = true;
		}
		if (StringUtils.hasText(runtime.getThumbnail())) {
			currentRuntime.setThumbnail(runtime.getThumbnail());
			modifyFlag = true;
		}
		if (runtime.getDisplayOrder() != null) {
			currentRuntime.setDisplayOrder(runtime.getDisplayOrder());
		}
		if (runtime.getEnabled() != null) {
			currentRuntime.setEnabled(runtime.getEnabled());
		}
		if (runtime.getVersions() != null && !runtime.getVersions().isEmpty()) {
			currentRuntime.setVersions(runtime.getVersions());
		}
		if (modifyFlag) {
			this.runtimeRepository.save(currentRuntime);
		}
	}
	
	public void deleteRuntime(String runtimeId) {
		this.runtimeRepository.delete(this.retrieveRuntimeById(runtimeId));
	}
	
	private void validateUniqueRuntime(Runtime runtime) {
		Optional<Runtime> persistedRuntimeOptional = null;
		
		if (runtime != null) {
			persistedRuntimeOptional = this.runtimeRepository.findById(runtime.getId());
			if (persistedRuntimeOptional.isPresent()) {
				throw new NotUniqueException("runtime");
			}
		}
	}
	
	private Runtime retrieveRuntimeById(String runtimeId) {
		Optional<Runtime> runtimeOptional = this.runtimeRepository.findById(runtimeId);
		if (!runtimeOptional.isPresent()) {
			throw new NotFoundException("runtime");
		}
		return runtimeOptional.get();
	}
}