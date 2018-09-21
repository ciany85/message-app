package com.danilocugia.messageuuidgenerator.controller;

import com.danilocugia.messageuuidgenerator.service.UuidGenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@RestController
public class UuidGenController {
    Logger logger = LoggerFactory.getLogger(UuidGenController.class);

    @Autowired
    UuidGenService uuidGen;

    @RequestMapping(value = "/message-uuid", method = RequestMethod.GET)
    public String get() {
        String uuid = uuidGen.get();
        logger.info("Generated a new UUID: " + uuid);
        return uuid;
    }

}
