package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ToolRepository extends MongoRepository<Tool, String> {

}
