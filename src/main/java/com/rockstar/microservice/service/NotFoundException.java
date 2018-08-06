package com.rockstar.microservice.service;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotFoundException(String resource) {
		super(String.format("%s not found", resource));
	}

}
