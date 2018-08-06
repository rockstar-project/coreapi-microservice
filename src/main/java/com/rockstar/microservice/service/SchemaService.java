package com.rockstar.microservice.service;

import java.util.Collection;

import com.rockstar.microservice.domain.Schema;

public interface SchemaService {

	public Collection<Schema> getSchemas();
	public Schema getSchema(String identifier);
	public Schema createSchema(Schema schema);
	public void updateSchema(Schema schema);
	public void deleteSchema(String identifier);
}
