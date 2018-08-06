package com.rockstar.microservice.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rockstar.microservice.service.ArchitectureService;
import com.rockstar.microservice.service.NotFoundException;
import com.rockstar.microservice.service.NotUniqueException;

@Service
public class ArchitectureServiceImpl implements ArchitectureService {
	
	@Autowired private ArchitectureRepository architectureRepository;
	
	public Collection<Architecture> getArchitectures() {
		return this.architectureRepository.findAll();
	}
	
	public Architecture getArchitecture(String architectureId) {
		return this.retrieveArchitectureById(architectureId);
	}
	
	public Architecture createArchitecture(Architecture architecture) {
		Architecture updatedArchitecture = null;
		if (architecture != null) {
			this.validateUniqueArchitecture(architecture);
			updatedArchitecture = this.architectureRepository.save(architecture);
		}
		return updatedArchitecture;
	}
	
	public void updateArchitecture(Architecture architecture) {
		Architecture currentArchitecture = null;
		Boolean modifyFlag = false;
		currentArchitecture = this.retrieveArchitectureById(architecture.getId());

		if (StringUtils.hasText(architecture.getTitle())) {
			currentArchitecture.setTitle(architecture.getTitle());
			modifyFlag = true;
		}
		if (StringUtils.hasText(architecture.getDescription())) {
			currentArchitecture.setDescription(architecture.getDescription());
			modifyFlag = true;
		}
		if (StringUtils.hasText(architecture.getThumbnail())) {
			currentArchitecture.setThumbnail(architecture.getThumbnail());
			modifyFlag = true;
		}
		if (architecture.getDisplayOrder() != null) {
			currentArchitecture.setDisplayOrder(architecture.getDisplayOrder());
		}
		if (architecture.getEnabled() != null) {
			currentArchitecture.setEnabled(architecture.getEnabled());
		}
		if (architecture.getVersions() != null && !architecture.getVersions().isEmpty()) {
			currentArchitecture.setVersions(architecture.getVersions());
		}
		if (modifyFlag) {
			this.architectureRepository.save(currentArchitecture);
		}
	}
	
	public void deleteArchitecture(String architectureId) {
		this.architectureRepository.delete(this.retrieveArchitectureById(architectureId));
	}
	
	private void validateUniqueArchitecture(Architecture architecture) {
		Optional<Architecture> persistedArchitectureOptional = null;
		
		if (architecture != null) {
			persistedArchitectureOptional = this.architectureRepository.findById(architecture.getId());
			if (persistedArchitectureOptional.isPresent()) {
				throw new NotUniqueException("architecture");
			}
		}
	}
	
	private Architecture retrieveArchitectureById(String architectureId) {
		Optional<Architecture> architectureOptional = this.architectureRepository.findById(architectureId);
		if (!architectureOptional.isPresent()) {
			throw new NotFoundException("architecture");
		}
		return architectureOptional.get();
	}
}