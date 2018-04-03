package com.rockstar.microservice.domain;

import java.util.Collection;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="runtimes")
public class Runtime extends MetaItem {
	
	private String stack;
	private Collection<MetaItem> frameworks;
	
	public Runtime() {
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public Collection<MetaItem> getFrameworks() {
		return frameworks;
	}

	public void setFrameworks(Collection<MetaItem> frameworks) {
		this.frameworks = frameworks;
	}
}
