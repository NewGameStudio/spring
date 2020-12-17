package com.csu.app.spring.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
