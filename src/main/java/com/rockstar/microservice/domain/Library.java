package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="libraries")
public class Library extends MetaItem {

}
