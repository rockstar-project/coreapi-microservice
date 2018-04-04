package com.rockstar.microservice.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rockstar.microservice.domain.Capability;
import com.rockstar.microservice.domain.CapabilityRepository;

@RestController
@RequestMapping("/capabilities")
public class CapabilityController {
	
    @Autowired
    private CapabilityRepository capabilityRepository;
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Capability>>> getCapabilities() {
    		List<Resource<Capability>> capabilities = StreamSupport.stream(capabilityRepository.findByParentNull().spliterator(), false)
    			.map(capability -> new Resource<>(capability,
    				linkTo(methodOn(CapabilityController.class).getCapability(capability.getId())).withSelfRel()))
    			.collect(Collectors.toList());

    		return ResponseEntity.ok(
    			new Resources<>(capabilities,linkTo(methodOn(CapabilityController.class).getCapabilities()).withSelfRel()));
    }
    
    @PostMapping
	public ResponseEntity<?> newCapability(@Valid @RequestBody Capability capability) {
    		Capability savedCapability = null;
    		
		try {
			savedCapability = this.capabilityRepository.save(capability);
			Link newlyCreatedLink = linkTo(methodOn(CapabilityController.class).getCapability(savedCapability.getId())).withSelfRel();

			Resource<Capability> capabilityResource = new Resource<>(savedCapability,
				linkTo(methodOn(CapabilityController.class).getCapability(savedCapability.getId())).withSelfRel());

			return ResponseEntity
				.created(new URI(newlyCreatedLink.getHref()))
				.body(capabilityResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + capability);
		}
    }
    
    @DeleteMapping
	public ResponseEntity<Void> deleteCapabilities() {
		this.capabilityRepository.deleteAll();
		return ResponseEntity.noContent().build();
    }
    
    @GetMapping(value = "/{slug}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Capability>> getCapability(@PathVariable String slug) {

    		return this.capabilityRepository.findById(slug)
    			.map(capability -> new Resource<>(capability,
    				linkTo(methodOn(CapabilityController.class).getCapability(capability.getId())).withSelfRel()))
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{slug}")
	public ResponseEntity<?> updateCapability(@RequestBody Capability capability, @PathVariable String slug) {
		Capability capabilityToUpdate = capability;
		capabilityToUpdate.setId(slug);
		this.capabilityRepository.save(capabilityToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(CapabilityController.class).getCapability(slug)).withSelfRel();

		try {
			return ResponseEntity.noContent()
				.location(new URI(newlyCreatedLink.getHref()))
				.build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + capabilityToUpdate);
		}
    }
    
    @DeleteMapping("/{slug}")
	public ResponseEntity<Void> deleteCapability(@PathVariable String slug) {
		this.capabilityRepository.deleteById(slug);
		return ResponseEntity.noContent().build();
    }
    
    @GetMapping(value="/{slug}/items", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Capability>>> getCapabilityItems(@PathVariable("slug") String capabilitySlug) {
    		List<Resource<Capability>> capabilities = StreamSupport.stream(capabilityRepository.findByParent(capabilitySlug).spliterator(), false)
    			.map(capability -> new Resource<>(capability,
    				linkTo(methodOn(CapabilityController.class).getCapability(capability.getId())).withSelfRel()))
    			.collect(Collectors.toList());

    		return ResponseEntity.ok(
    			new Resources<>(capabilities,linkTo(methodOn(CapabilityController.class).getCapabilityItems(capabilitySlug)).withSelfRel()));
    }
    
    @PostMapping(value="/{slug}/items")
	public HttpEntity<Void> createCapabilityItem(@PathVariable("slug") String capabilitySlug, @Valid @RequestBody Capability itemCapability) {
		HttpHeaders headers = null;
		
		itemCapability.setParent(capabilitySlug);
		this.capabilityRepository.save(itemCapability);
		headers = new HttpHeaders();
		headers.setLocation(linkTo(CapabilityController.class).slash(capabilitySlug).slash("items").slash(itemCapability.getId().toString()).toUri());
		
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
    
    @DeleteMapping(value="/{slug}/items")
	public HttpEntity<Void> deleteCapabilityItems(@PathVariable("slug") String capabilitySlug) {
		this.capabilityRepository.deleteAll(this.capabilityRepository.findByParent(capabilitySlug));
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value = "/{slug}/items/{item_slug}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Capability>> getCapabilityItem(@PathVariable("slug") String capabilitySlug, @PathVariable("item_slug") String itemId) {

    		return this.capabilityRepository.findById(itemId)
    			.map(capability -> new Resource<>(capability,
    				linkTo(methodOn(CapabilityController.class).getCapabilityItem(capabilitySlug, capability.getId())).withSelfRel()))
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
	
	@DeleteMapping(value="/{slug}/items/{item_slug}")
	public ResponseEntity<Void> deleteCapabilityItem(@PathVariable("slug") String capabilitySlug, @PathVariable("item_slug") String itemId) {
		this.capabilityRepository.deleteById(itemId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value="/{slug}/items/{item_slug}")
	public HttpEntity<Void> addCapabilityItemOption(@PathVariable("slug") String capabilitySlug, @PathVariable("item_slug") String itemSlug, @RequestBody Capability capabilityItem) {
		Optional<Capability> parentCapability = this.capabilityRepository.findById(capabilitySlug);
		Optional<Capability> persistedCapabilityItem = this.capabilityRepository.findById(itemSlug);
												
		if (parentCapability.isPresent() && persistedCapabilityItem.isPresent()) {
			capabilityItem.setId(itemSlug);
			this.capabilityRepository.save(capabilityItem);
		} else {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.noContent().build();
	}

}