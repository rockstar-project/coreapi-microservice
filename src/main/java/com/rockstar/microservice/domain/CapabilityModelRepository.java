package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CapabilityModelRepository extends MongoRepository<CapabilityModel, String>  {
}
