package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="schemas")
public class Schema extends MetaItem {
	
	private String sampleUrl;
	private String format;
	
	public Schema() {
	}

	public String getSampleUrl() {
		return sampleUrl;
	}

	public void setSampleUrl(String sampleUrl) {
		this.sampleUrl = sampleUrl;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
