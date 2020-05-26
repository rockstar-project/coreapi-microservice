package com.rockstar.microservice.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rockstar.microservice.domain.Schema;
import com.rockstar.microservice.service.SchemaService;

@CrossOrigin
@RestController
public class SchemaController {
	
	@Autowired
    private SchemaService schemaService;
    
    @PostMapping("/schemas")
    public ResponseEntity<Schema> create(@Valid @RequestBody Schema schema, UriComponentsBuilder uriBuilder) throws Exception {
    		Schema newSchema = schemaService.createSchema(schema);
        return ResponseEntity.created(uriBuilder.path("/schemas/{id}").buildAndExpand(newSchema.getId()).toUri()).body(newSchema);
    }

    @GetMapping("/schemas")
    public ResponseEntity<Collection<Schema>> findAll(){
        return ResponseEntity.ok(schemaService.getSchemas());
    }

    @GetMapping("/schemas/{id}")
    public ResponseEntity<Schema> show(@PathVariable String id){
        return ResponseEntity.ok(schemaService.getSchema(id));
    }

    @PatchMapping("/schemas/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Schema schema) {
    		schema.setId(id);
        schemaService.updateSchema(schema);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/schemas/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
    		schemaService.deleteSchema(id);
        return ResponseEntity.noContent().build();
    }
    
}