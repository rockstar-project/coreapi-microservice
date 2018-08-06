package com.rockstar.microservice.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rockstar.microservice.domain.Architecture;
import com.rockstar.microservice.service.ArchitectureService;

@RestController
public class ArchitectureController {
	
    @Autowired
    private ArchitectureService architectureService;
    
    @PostMapping("/architectures")
    public ResponseEntity<Architecture> create(@Valid @RequestBody Architecture architecture, UriComponentsBuilder uriBuilder) throws Exception {
    		Architecture newArchitecture = architectureService.createArchitecture(architecture);
        return ResponseEntity.created(uriBuilder.path("/architectures/{id}").buildAndExpand(newArchitecture.getId()).toUri()).body(newArchitecture);
    }

    @GetMapping("/architectures")
    public ResponseEntity<Collection<Architecture>> findAll(){
        return ResponseEntity.ok(architectureService.getArchitectures());
    }

    @GetMapping("/architectures/{id}")
    public ResponseEntity<Architecture> show(@PathVariable String id){
        return ResponseEntity.ok(architectureService.getArchitecture(id));
    }

    @PatchMapping("/architectures/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Architecture architecture) {
    		architecture.setId(id);
        architectureService.updateArchitecture(architecture);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/architectures/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
    		architectureService.deleteArchitecture(id);
        return ResponseEntity.noContent().build();
    }
    
}