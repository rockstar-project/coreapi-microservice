package com.rockstar.microservice.domain;

import java.util.Collection;

public class MetaObject extends MetaItem {
	
	private Collection<MetaItem> items;
	
	public MetaObject() {
	}

	public Collection<MetaItem> getItems() {
		return items;
	}

	public void setItems(Collection<MetaItem> items) {
		this.items = items;
	}

}
