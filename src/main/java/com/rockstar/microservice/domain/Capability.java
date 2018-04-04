package com.rockstar.microservice.domain;

import java.util.Collection;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection="capabilities")
public class Capability extends MetaItem {

	@JsonIgnore
	private String parent;
	private Collection<String> options;
  
    public Capability() {
    }
    
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Collection<String> getOptions() {
		return options;
	}

	public void setOptions(Collection<String> options) {
		this.options = options;
	}
    
}
