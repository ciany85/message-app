package com.danilocugia.tantalum.messageuuidgenerator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidGenService {

    @Value( "${prefix}" )
    private String prefix;

    @Value( "${suffix}" )
    private String suffix;

    public String get() {
        return prefix + "-" + UUID.randomUUID().toString() + "-" + suffix;
    }
}
