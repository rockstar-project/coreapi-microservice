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

import com.rockstar.microservice.domain.Capability;
import com.rockstar.microservice.service.CapabilityService;

@CrossOrigin
@RestController
public class CapabilityController {
	
	@Autowired
    private CapabilityService capabilityService;
    
    @PostMapping("/capabilities")
    public ResponseEntity<Capability> create(@Valid @RequestBody Capability capability, UriComponentsBuilder uriBuilder) throws Exception {
    		Capability newCapability = capabilityService.createCapability(capability);
        return ResponseEntity.created(uriBuilder.path("/capabilities/{id}").buildAndExpand(newCapability.getId()).toUri()).body(newCapability);
    }

    @GetMapping("/capabilities")
    public ResponseEntity<Collection<Capability>> findAll(){
        return ResponseEntity.ok(capabilityService.getCapabilities());
    }

    @GetMapping("/capabilities/{id}")
    public ResponseEntity<Capability> show(@PathVariable String id){
        return ResponseEntity.ok(capabilityService.getCapability(id));
    }

    @PatchMapping("/capabilities/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Capability capability) {
    		capability.setId(id);
        capabilityService.updateCapability(capability);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/capabilities/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
    		capabilityService.deleteCapability(id);
        return ResponseEntity.noContent().build();
    }
  
}