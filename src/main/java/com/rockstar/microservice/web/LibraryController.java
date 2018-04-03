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

import com.rockstar.microservice.domain.Library;
import com.rockstar.microservice.domain.LibraryRepository;

@RestController
@RequestMapping("/libraries")
public class LibraryController {
	
    @Autowired
    private LibraryRepository libraryRepository;
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Library>>> findAll() {
    		List<Resource<Library>> libraries = StreamSupport.stream(libraryRepository.findAll().spliterator(), false)
    			.map(library -> new Resource<>(library,
    				linkTo(methodOn(LibraryController.class).findOne(library.getId())).withSelfRel()))
    			.collect(Collectors.toList());

    		return ResponseEntity.ok(
    			new Resources<>(libraries,linkTo(methodOn(LibraryController.class).findAll()).withSelfRel()));
    }
    
    @PostMapping
	public ResponseEntity<?> newLibrary(@Valid @RequestBody Library library) {
    		Library savedLibrary = null;
    		
		try {
			savedLibrary = this.libraryRepository.save(library);
			Link newlyCreatedLink = linkTo(methodOn(LibraryController.class).findOne(savedLibrary.getId())).withSelfRel();

			Resource<Library> libraryResource = new Resource<>(savedLibrary,
				linkTo(methodOn(LibraryController.class).findOne(savedLibrary.getId())).withSelfRel());

			return ResponseEntity
				.created(new URI(newlyCreatedLink.getHref()))
				.body(libraryResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + library);
		}
    }
    
    @GetMapping(value = "/{slug}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Library>> findOne(@PathVariable String slug) {

    		return this.libraryRepository.findById(slug)
    			.map(library -> new Resource<>(library,
    				linkTo(methodOn(LibraryController.class).findOne(library.getId())).withSelfRel()))
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{slug}")
	public ResponseEntity<?> updateLibrary(@RequestBody Library library, @PathVariable String slug) {
		Library libraryToUpdate = library;
		libraryToUpdate.setId(slug);
		this.libraryRepository.save(libraryToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(LibraryController.class).findOne(slug)).withSelfRel();

		try {
			return ResponseEntity.noContent()
				.location(new URI(newlyCreatedLink.getHref()))
				.build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + libraryToUpdate);
		}
    }
    
    @DeleteMapping("/{slug}")
	public ResponseEntity<?> deleteLibrary(@PathVariable String slug) {
		this.libraryRepository.deleteById(slug);
		return ResponseEntity.noContent().build();
    }
    
}