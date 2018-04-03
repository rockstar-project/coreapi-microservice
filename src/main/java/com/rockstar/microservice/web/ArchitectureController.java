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

import com.rockstar.microservice.domain.Architecture;
import com.rockstar.microservice.domain.ArchitectureRepository;

@RestController
@RequestMapping("/architectures")
public class ArchitectureController {
	
    @Autowired
    private ArchitectureRepository architectureRepository;
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Architecture>>> findAll() {
    		List<Resource<Architecture>> architectures = StreamSupport.stream(architectureRepository.findAll().spliterator(), false)
    			.map(architecture -> new Resource<>(architecture))
    			.collect(Collectors.toList());

    		return ResponseEntity.ok(
    			new Resources<>(architectures,linkTo(methodOn(ArchitectureController.class).findAll()).withSelfRel()));
    }
    
    @PostMapping
	public ResponseEntity<?> newArchitecture(@Valid @RequestBody Architecture architecture) {
    		Architecture savedArchitecture = null;
    		
		try {
			savedArchitecture = this.architectureRepository.save(architecture);
			Link newlyCreatedLink = linkTo(methodOn(ArchitectureController.class).findOne(savedArchitecture.getId())).withSelfRel();

			Resource<Architecture> architectureResource = new Resource<>(savedArchitecture,
				linkTo(methodOn(ArchitectureController.class).findOne(savedArchitecture.getId())).withSelfRel());

			return ResponseEntity
				.created(new URI(newlyCreatedLink.getHref()))
				.body(architectureResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + architecture);
		}
    }
    
    @GetMapping(value = "/{slug}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Architecture>> findOne(@PathVariable String slug) {

    		return this.architectureRepository.findById(slug)
    			.map(architecture -> new Resource<>(architecture))
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{slug}")
	public ResponseEntity<?> updateArchitecture(@RequestBody Architecture architecture, @PathVariable String slug) {
		Architecture architectureToUpdate = architecture;
		architectureToUpdate.setId(slug);
		this.architectureRepository.save(architectureToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(ArchitectureController.class).findOne(slug)).withSelfRel();

		try {
			return ResponseEntity.noContent()
				.location(new URI(newlyCreatedLink.getHref()))
				.build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + architectureToUpdate);
		}
    }
    
    @DeleteMapping("/{slug}")
	public ResponseEntity<?> deleteArchitecture(@PathVariable String slug) {
		this.architectureRepository.deleteById(slug);
		return ResponseEntity.noContent().build();
    }
    
}