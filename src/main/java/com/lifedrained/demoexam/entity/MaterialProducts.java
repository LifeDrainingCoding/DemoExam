package com.lifedrained.demoexam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "material_products", schema = "db")
public class MaterialProducts extends AbstractEntity {
    @Column
    private String name;

    @Column
    private String product;

    @Column
    private BigDecimal count;

    public MaterialProducts() {
        super();
    }

    public MaterialProducts(String name, String product, BigDecimal count) {
        super();
        this.name = name;
        this.product = product;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }
}
