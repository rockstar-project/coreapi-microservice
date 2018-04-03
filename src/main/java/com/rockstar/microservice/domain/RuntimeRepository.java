package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuntimeRepository extends MongoRepository<Runtime, String>  {
}
