package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="features")
public class Feature extends MetaItem {
	
	public Feature() {
	}
	
}
