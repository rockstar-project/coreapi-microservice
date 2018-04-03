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

import com.rockstar.microservice.domain.Runtime;
import com.rockstar.microservice.domain.RuntimeRepository;

@RestController
@RequestMapping("/runtimes")
public class RuntimeController {
	
    @Autowired
    private RuntimeRepository runtimeRepository;
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Runtime>>> findAll() {
    		List<Resource<Runtime>> runtimes = StreamSupport.stream(runtimeRepository.findAll().spliterator(), false)
    			.map(runtime -> new Resource<>(runtime,
    				linkTo(methodOn(RuntimeController.class).findOne(runtime.getId())).withSelfRel()))
    			.collect(Collectors.toList());

    		return ResponseEntity.ok(
    			new Resources<>(runtimes,linkTo(methodOn(RuntimeController.class).findAll()).withSelfRel()));
    }
    
    @PostMapping
	public ResponseEntity<?> newRuntime(@Valid @RequestBody Runtime runtime) {
    		Runtime savedRuntime = null;
    		
		try {
			savedRuntime = this.runtimeRepository.save(runtime);
			Link newlyCreatedLink = linkTo(methodOn(RuntimeController.class).findOne(savedRuntime.getId())).withSelfRel();

			Resource<Runtime> runtimeResource = new Resource<>(savedRuntime,
				linkTo(methodOn(RuntimeController.class).findOne(savedRuntime.getId())).withSelfRel());

			return ResponseEntity
				.created(new URI(newlyCreatedLink.getHref()))
				.body(runtimeResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + runtime);
		}
    }
    
    @GetMapping(value = "/{slug}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Runtime>> findOne(@PathVariable String slug) {

    		return this.runtimeRepository.findById(slug)
    			.map(runtime -> new Resource<>(runtime,
    				linkTo(methodOn(RuntimeController.class).findOne(runtime.getId())).withSelfRel()))
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{slug}")
	public ResponseEntity<?> updateRuntime(@RequestBody Runtime runtime, @PathVariable String slug) {
		Runtime runtimeToUpdate = runtime;
		runtimeToUpdate.setId(slug);
		this.runtimeRepository.save(runtimeToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(RuntimeController.class).findOne(slug)).withSelfRel();

		try {
			return ResponseEntity.noContent()
				.location(new URI(newlyCreatedLink.getHref()))
				.build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + runtimeToUpdate);
		}
    }
    
    @DeleteMapping("/{slug}")
	public ResponseEntity<?> deleteRuntime(@PathVariable String slug) {
		this.runtimeRepository.deleteById(slug);
		return ResponseEntity.noContent().build();
    }
    
}