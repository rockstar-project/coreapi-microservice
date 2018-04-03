package com.rockstar.microservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="tools")
public class Tool extends MetaItem {
}
