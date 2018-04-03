package com.rockstar.microservice.domain;

import java.util.Collection;

public class Capability extends MetaItem {

	private Collection<MetaItem> options;
  
    public Capability() {
    }

	public Collection<MetaItem> getOptions() {
		return options;
	}

	public void setOptions(Collection<MetaItem> options) {
		this.options = options;
	}
    
}
