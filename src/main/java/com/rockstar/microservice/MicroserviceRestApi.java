package com.rockstar.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceRestApi {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceRestApi.class, args);
	}
}
