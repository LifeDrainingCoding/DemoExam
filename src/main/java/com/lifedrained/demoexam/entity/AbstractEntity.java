package com.lifedrained.demoexam.entity;

import jakarta.persistence.*;


@MappedSuperclass
public abstract  class AbstractEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private Long id;


    public AbstractEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
