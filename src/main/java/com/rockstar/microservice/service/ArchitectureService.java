package com.rockstar.microservice.service;

import java.util.Collection;

import com.rockstar.microservice.domain.Architecture;

public interface ArchitectureService {
	
	public Collection<Architecture> getArchitectures();
	public Architecture getArchitecture(String identifier);
	public Architecture createArchitecture(Architecture architecture);
	public void updateArchitecture(Architecture architecture);
	public void deleteArchitecture(String identifier);

}
