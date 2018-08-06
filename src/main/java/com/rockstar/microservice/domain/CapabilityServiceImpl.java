package com.rockstar.microservice.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rockstar.microservice.service.CapabilityService;
import com.rockstar.microservice.service.NotFoundException;
import com.rockstar.microservice.service.NotUniqueException;

@Service
public class CapabilityServiceImpl implements CapabilityService {
	
	@Autowired private CapabilityRepository capabilityRepository;
	
	public Collection<Capability> getCapabilities() {
		return this.capabilityRepository.findByParentNull();
	}
	
	public Collection<Capability> getCapabilities(String capabilityId) {
		return this.capabilityRepository.findByParent(capabilityId);
	}
	
	public Capability getCapability(String identifier) {
		Capability capability = this.retrieveCapabilityById(identifier);
		capability.setSubcapabilities(this.capabilityRepository.findByParent(capability.getId()));
		return capability;
	}
	
	public Capability createCapability(Capability capability) {
		this.validateUniqueCapability(capability);
		return this.capabilityRepository.save(capability);
	}
	
	public void updateCapability(Capability capability) {
		Capability currentCapability = null;
		Boolean modifyFlag = false;
		currentCapability = this.retrieveCapabilityById(capability.getId());

		if (StringUtils.hasText(capability.getTitle())) {
			currentCapability.setTitle(capability.getTitle());
			modifyFlag = true;
		}
		if (StringUtils.hasText(capability.getDescription())) {
			currentCapability.setDescription(capability.getDescription());
			modifyFlag = true;
		}
		if (StringUtils.hasText(capability.getThumbnail())) {
			currentCapability.setThumbnail(capability.getThumbnail());
			modifyFlag = true;
		}
		if (capability.getDisplayOrder() != null) {
			currentCapability.setDisplayOrder(capability.getDisplayOrder());
		}
		if (capability.getEnabled() != null) {
			currentCapability.setEnabled(capability.getEnabled());
		}
		if (capability.getVersions() != null && !capability.getVersions().isEmpty()) {
			currentCapability.setVersions(capability.getVersions());
		}
		if (modifyFlag) {
			this.capabilityRepository.save(currentCapability);
		}
	}
	
	public void deleteCapability(String identifier) {
		this.capabilityRepository.delete(this.retrieveCapabilityById(identifier));
	}
	
	private void validateUniqueCapability(Capability capability) {
		Optional<Capability> persistedCapabilityOptional = null;
		
		if (capability != null) {
			persistedCapabilityOptional = this.capabilityRepository.findById(capability.getId());
			if (persistedCapabilityOptional.isPresent()) {
				throw new NotUniqueException("capability");
			}
		}
	}
	
	private Capability retrieveCapabilityById(String identifier) {
		Optional<Capability> capabilityOptional = this.capabilityRepository.findById(identifier);
		if (!capabilityOptional.isPresent()) {
			throw new NotFoundException("capability");
		}
		return capabilityOptional.get();
	}
}