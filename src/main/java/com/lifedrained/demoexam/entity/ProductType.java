package com.lifedrained.demoexam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "product_type", schema = "db")
public class ProductType extends AbstractEntity {
    @Column
    private String type;

    @Column
    private BigDecimal mult ;

    public ProductType() {
        super();
    }

    public ProductType(String type, BigDecimal mult) {
        super();
        this.type = type;
        this.mult = mult;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getMult() {
        return mult;
    }

    public void setMult(BigDecimal mult) {
        this.mult = mult;
    }
}
