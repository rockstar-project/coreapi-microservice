package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArchitectureRepository extends MongoRepository<Architecture, String>  {
}
