package com.rockstar.microservice.domain;

import java.util.Collection;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="capabilities")
public class Capability extends MetaItem {

	private String parent;
	
	@Transient
	private Collection<Capability> subcapabilities;
	private Collection<Option> options;
  
    public Capability() {
    }
    
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Collection<Capability> getSubcapabilities() {
		return subcapabilities;
	}

	public void setSubcapabilities(Collection<Capability> subcapabilities) {
		this.subcapabilities = subcapabilities;
	}

	public Collection<Option> getOptions() {
		return options;
	}

	public void setOptions(Collection<Option> options) {
		this.options = options;
	}
    
}
