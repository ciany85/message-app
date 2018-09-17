package com.danilocugia.tantalum.messageprocessor.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Message {
    @Id @GeneratedValue
    private Long id;
    @CreationTimestamp
    private Date createdAt;
    private String message;
    private String uuid;
}
