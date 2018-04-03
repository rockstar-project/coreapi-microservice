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

import com.rockstar.microservice.domain.Feature;
import com.rockstar.microservice.domain.FeatureRepository;

@RestController
@RequestMapping("/features")
public class FeatureController {
	
    @Autowired
    private FeatureRepository featureRepository;
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Feature>>> findAll() {
    		List<Resource<Feature>> features = StreamSupport.stream(featureRepository.findAll().spliterator(), false)
    			.map(feature -> new Resource<>(feature,
    				linkTo(methodOn(FeatureController.class).findOne(feature.getId())).withSelfRel()))
    			.collect(Collectors.toList());

    		return ResponseEntity.ok(
    			new Resources<>(features,linkTo(methodOn(FeatureController.class).findAll()).withSelfRel()));
    }
    
    @PostMapping
	public ResponseEntity<?> newFeature(@Valid @RequestBody Feature feature) {
    		Feature savedFeature = null;
    		
		try {
			savedFeature = this.featureRepository.save(feature);
			Link newlyCreatedLink = linkTo(methodOn(FeatureController.class).findOne(savedFeature.getId())).withSelfRel();

			Resource<Feature> featureResource = new Resource<>(savedFeature,
				linkTo(methodOn(FeatureController.class).findOne(savedFeature.getId())).withSelfRel());

			return ResponseEntity
				.created(new URI(newlyCreatedLink.getHref()))
				.body(featureResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + feature);
		}
    }
    
    @GetMapping(value = "/{slug}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Feature>> findOne(@PathVariable String slug) {

    		return this.featureRepository.findById(slug)
    			.map(feature -> new Resource<>(feature,
    				linkTo(methodOn(FeatureController.class).findOne(feature.getId())).withSelfRel()))
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{slug}")
	public ResponseEntity<?> updateFeature(@RequestBody Feature feature, @PathVariable String slug) {
		Feature featureToUpdate = feature;
		featureToUpdate.setId(slug);
		this.featureRepository.save(featureToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(FeatureController.class).findOne(slug)).withSelfRel();

		try {
			return ResponseEntity.noContent()
				.location(new URI(newlyCreatedLink.getHref()))
				.build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + featureToUpdate);
		}
    }
    
    @DeleteMapping("/{slug}")
	public ResponseEntity<?> deleteFeature(@PathVariable String slug) {
		this.featureRepository.deleteById(slug);
		return ResponseEntity.noContent().build();
    }
    
}