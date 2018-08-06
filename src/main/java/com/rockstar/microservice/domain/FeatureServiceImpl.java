package com.rockstar.microservice.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.rockstar.microservice.service.FeatureService;
import com.rockstar.microservice.service.NotFoundException;
import com.rockstar.microservice.service.NotUniqueException;

@Service
@Transactional(readOnly=true)
public class FeatureServiceImpl implements FeatureService {
	
	@Autowired private FeatureRepository featureRepository;
	
	public Collection<Feature> getFeatures() {
		return this.featureRepository.findAll();
	}
	
	public Feature getFeature(String featureId) {
		return this.retrieveFeatureById(featureId);
	}
	
	@Transactional(readOnly=false)
	public Feature createFeature(Feature feature) {
		Feature updatedFeature = null;
		if (feature != null) {
			this.validateUniqueFeature(feature);
			updatedFeature = this.featureRepository.save(feature);
		}
		return updatedFeature;
	}
	
	@Transactional(readOnly=false)
	public void updateFeature(Feature feature) {
		Feature currentFeature = null;
		Boolean modifyFlag = false;
		currentFeature = this.retrieveFeatureById(feature.getId());

		if (StringUtils.hasText(feature.getTitle())) {
			currentFeature.setTitle(feature.getTitle());
			modifyFlag = true;
		}
		if (StringUtils.hasText(feature.getDescription())) {
			currentFeature.setDescription(feature.getDescription());
			modifyFlag = true;
		}
		if (StringUtils.hasText(feature.getThumbnail())) {
			currentFeature.setThumbnail(feature.getThumbnail());
			modifyFlag = true;
		}
		if (feature.getDisplayOrder() != null) {
			currentFeature.setDisplayOrder(feature.getDisplayOrder());
		}
		if (feature.getEnabled() != null) {
			currentFeature.setEnabled(feature.getEnabled());
		}
		if (feature.getVersions() != null && !feature.getVersions().isEmpty()) {
			currentFeature.setVersions(feature.getVersions());
		}
		if (modifyFlag) {
			this.featureRepository.save(currentFeature);
		}
	}
	
	@Transactional(readOnly=false)
	public void deleteFeature(String featureId) {
		this.featureRepository.delete(this.retrieveFeatureById(featureId));
	}
	
	private void validateUniqueFeature(Feature feature) {
		Optional<Feature> persistedFeatureOptional = null;
		
		if (feature != null) {
			persistedFeatureOptional = this.featureRepository.findById(feature.getId());
			if (persistedFeatureOptional.isPresent()) {
				throw new NotUniqueException("feature");
			}
		}
	}
	
	private Feature retrieveFeatureById(String featureId) {
		Optional<Feature> featureOptional = this.featureRepository.findById(featureId);
		if (!featureOptional.isPresent()) {
			throw new NotFoundException("feature");
		}
		return featureOptional.get();
	}
}