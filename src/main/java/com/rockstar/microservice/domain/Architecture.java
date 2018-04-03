package com.rockstar.microservice.domain;

import java.util.Collection;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="architectures")
public class Architecture extends MetaItem {
	
	@DBRef
	private Collection<MetaItem> patterns;
	
	public Architecture() {
	}
	
	public Collection<MetaItem> getPatterns() {
		return patterns;
	}

	public void setPatterns(Collection<MetaItem> patterns) {
		this.patterns = patterns;
	}
	
}
