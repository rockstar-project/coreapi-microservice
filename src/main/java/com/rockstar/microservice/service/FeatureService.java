package com.rockstar.microservice.service;

import java.util.Collection;

import com.rockstar.microservice.domain.Feature;

public interface FeatureService {
	
	public Collection<Feature> getFeatures();
	public Feature getFeature(String identifier);
	public Feature createFeature(Feature feature);
	public void updateFeature(Feature feature);
	public void deleteFeature(String identifier);

}
