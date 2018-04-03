package com.rockstar.microservice.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rockstar.microservice.domain.Schema;
import com.rockstar.microservice.domain.SchemaRepository;

@RestController
@RequestMapping("/schemas")
public class SchemaController {
	
    @Autowired
    private SchemaRepository schemaRepository;
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Schema>>> findAll() {
    		List<Resource<Schema>> schemas = StreamSupport.stream(schemaRepository.findAll().spliterator(), false)
    			.map(schema -> new Resource<>(schema,
    				linkTo(methodOn(SchemaController.class).findOne(schema.getId())).withSelfRel()))
    			.collect(Collectors.toList());

    		return ResponseEntity.ok(
    			new Resources<>(schemas,linkTo(methodOn(SchemaController.class).findAll()).withSelfRel()));
    }
    
    @PostMapping
	public ResponseEntity<?> newSchema(@Valid @RequestBody Schema schema) {
    		Schema savedSchema = null;
    		
		try {
			savedSchema = this.schemaRepository.save(schema);
			Link newlyCreatedLink = linkTo(methodOn(SchemaController.class).findOne(savedSchema.getId())).withSelfRel();

			Resource<Schema> schemaResource = new Resource<>(savedSchema,
				linkTo(methodOn(SchemaController.class).findOne(savedSchema.getId())).withSelfRel());

			return ResponseEntity
				.created(new URI(newlyCreatedLink.getHref()))
				.body(schemaResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + schema);
		}
    }
    
    @GetMapping(value = "/{slug}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Schema>> findOne(@PathVariable String slug) {

    		return this.schemaRepository.findById(slug)
    			.map(schema -> new Resource<>(schema,
    				linkTo(methodOn(SchemaController.class).findOne(schema.getId())).withSelfRel()))
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{slug}")
	public ResponseEntity<?> updateSchema(@RequestBody Schema schema, @PathVariable String slug) {
		Schema schemaToUpdate = schema;
		schemaToUpdate.setId(slug);
		this.schemaRepository.save(schemaToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(SchemaController.class).findOne(slug)).withSelfRel();

		try {
			return ResponseEntity.noContent()
				.location(new URI(newlyCreatedLink.getHref()))
				.build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + schemaToUpdate);
		}
    }
    
    @DeleteMapping("/{slug}")
	public ResponseEntity<?> deleteSchema(@PathVariable String slug) {
		this.schemaRepository.deleteById(slug);
		return ResponseEntity.noContent().build();
    }
    
}