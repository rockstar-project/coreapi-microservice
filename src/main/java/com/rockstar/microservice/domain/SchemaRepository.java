package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SchemaRepository extends MongoRepository<Schema, String>  {
}
