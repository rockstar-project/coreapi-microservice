package com.rockstar.microservice.service;

import java.util.Collection;

import com.rockstar.microservice.domain.Capability;

public interface CapabilityService {
	
	public Collection<Capability> getCapabilities(String capabilityId);
	public Collection<Capability> getCapabilities();
	public Capability getCapability(String identifier);
	public Capability createCapability(Capability capability);
	public void updateCapability(Capability capability);
	public void deleteCapability(String identifier);
}
