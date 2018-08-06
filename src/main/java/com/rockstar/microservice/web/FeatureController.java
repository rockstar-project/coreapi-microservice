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

import com.rockstar.microservice.domain.Feature;
import com.rockstar.microservice.service.FeatureService;

@RestController
public class FeatureController {
	
    @Autowired
    private FeatureService featureService;
    
    @PostMapping("/features")
    public ResponseEntity<Feature> create(@Valid @RequestBody Feature feature, UriComponentsBuilder uriBuilder) throws Exception {
    		Feature newFeature = featureService.createFeature(feature);
        return ResponseEntity.created(uriBuilder.path("/features/{id}").buildAndExpand(newFeature.getId()).toUri()).body(newFeature);
    }

    @GetMapping("/features")
    public ResponseEntity<Collection<Feature>> findAll(){
        return ResponseEntity.ok(featureService.getFeatures());
    }

    @GetMapping("/features/{id}")
    public ResponseEntity<Feature> show(@PathVariable String id){
        return ResponseEntity.ok(featureService.getFeature(id));
    }

    @PatchMapping("/features/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Feature feature) {
    		feature.setId(id);
        featureService.updateFeature(feature);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/features/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
    		featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }
    
}