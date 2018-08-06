package com.rockstar.microservice.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rockstar.microservice.service.NotFoundException;
import com.rockstar.microservice.service.NotUniqueException;
import com.rockstar.microservice.service.SchemaService;

@Service
public class SchemaServiceImpl implements SchemaService {
	
	@Autowired private SchemaRepository schemaRepository;
	
	public Collection<Schema> getSchemas() {
		return this.schemaRepository.findAll();
	}
	
	public Schema getSchema(String schemaId) {
		return this.retrieveSchemaById(schemaId);
	}
	
	public Schema createSchema(Schema schema) {
		Schema updatedSchema = null;
		if (schema != null) {
			this.validateUniqueSchema(schema);
			updatedSchema = this.schemaRepository.save(schema);
		}
		return updatedSchema;
	}
	
	public void updateSchema(Schema schema) {
		Schema currentSchema = null;
		Boolean modifyFlag = false;
		currentSchema = this.retrieveSchemaById(schema.getId());

		if (StringUtils.hasText(schema.getTitle())) {
			currentSchema.setTitle(schema.getTitle());
			modifyFlag = true;
		}
		if (StringUtils.hasText(schema.getDescription())) {
			currentSchema.setDescription(schema.getDescription());
			modifyFlag = true;
		}
		if (StringUtils.hasText(schema.getThumbnail())) {
			currentSchema.setThumbnail(schema.getThumbnail());
			modifyFlag = true;
		}
		if (schema.getDisplayOrder() != null) {
			currentSchema.setDisplayOrder(schema.getDisplayOrder());
		}
		if (schema.getEnabled() != null) {
			currentSchema.setEnabled(schema.getEnabled());
		}
		if (schema.getVersions() != null && !schema.getVersions().isEmpty()) {
			currentSchema.setVersions(schema.getVersions());
		}
		if (modifyFlag) {
			this.schemaRepository.save(currentSchema);
		}
	}
	
	public void deleteSchema(String schemaId) {
		this.schemaRepository.delete(this.retrieveSchemaById(schemaId));
	}
	
	private void validateUniqueSchema(Schema schema) {
		Optional<Schema> persistedSchemaOptional = null;
		
		if (schema != null) {
			persistedSchemaOptional = this.schemaRepository.findById(schema.getId());
			if (persistedSchemaOptional.isPresent()) {
				throw new NotUniqueException("schema");
			}
		}
	}
	
	private Schema retrieveSchemaById(String schemaId) {
		Optional<Schema> schemaOptional = this.schemaRepository.findById(schemaId);
		if (!schemaOptional.isPresent()) {
			throw new NotFoundException("schema");
		}
		return schemaOptional.get();
	}
}