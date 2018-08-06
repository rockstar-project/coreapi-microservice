package com.rockstar.microservice.domain;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CapabilityRepository extends MongoRepository<Capability, String>  {
	
	public Collection<Capability> findByParent(String parent);
	public Collection<Capability> findByParentNull();
	public Capability findByParentAndId(String parent, String id);
}
