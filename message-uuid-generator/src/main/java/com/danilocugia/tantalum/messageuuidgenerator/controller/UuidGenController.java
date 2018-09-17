package com.danilocugia.tantalum.messageuuidgenerator.controller;

import com.danilocugia.tantalum.messageuuidgenerator.service.UuidGenService;
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

    @Autowired
    UuidGenService uuidGen;

    @RequestMapping(value = "/message-uuid", method = RequestMethod.GET)
    public String get() {
        return uuidGen.get();
    }

}
