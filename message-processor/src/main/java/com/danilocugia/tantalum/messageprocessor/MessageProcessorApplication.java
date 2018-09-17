package com.danilocugia.tantalum.messageprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MessageProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageProcessorApplication.class, args);
	}
}
