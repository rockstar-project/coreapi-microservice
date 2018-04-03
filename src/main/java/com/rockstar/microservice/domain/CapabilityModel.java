package com.rockstar.microservice.domain;

import java.util.Collection;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="capabilities")
public class CapabilityModel extends MetaItem {
	
	private Collection<Capability> core;
	private Collection<Capability> supporting;
	private Collection<Capability> infrastructure;
	private Collection<Capability> processGovernance;
	
	public CapabilityModel() {
	}

	public Collection<Capability> getCore() {
		return core;
	}

	public void setCore(Collection<Capability> core) {
		this.core = core;
	}

	public Collection<Capability> getSupporting() {
		return supporting;
	}

	public void setSupporting(Collection<Capability> supporting) {
		this.supporting = supporting;
	}

	public Collection<Capability> getInfrastructure() {
		return infrastructure;
	}

	public void setInfrastructure(Collection<Capability> infrastructure) {
		this.infrastructure = infrastructure;
	}

	public Collection<Capability> getProcessGovernance() {
		return processGovernance;
	}

	public void setProcessGovernance(Collection<Capability> processGovernance) {
		this.processGovernance = processGovernance;
	}
}
