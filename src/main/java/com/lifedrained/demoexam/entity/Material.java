package com.lifedrained.demoexam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "materials" , schema = "db")
public class Material extends AbstractEntity {
    @Column
    private String materialName;

    @Column
    private String materialType;

    @Column
    private BigDecimal materialPrice;

    @Column
    private int count;

    @Column
    private int minCount;

    @Column
    private BigDecimal countInPack;

    @Column
    private String dimUnit;

    public Material() {
        super();
    }

    public Material(String materialName, String materialType, BigDecimal materialPrice, int count, int minCount, BigDecimal countInPack, String dimUnit) {
        super();
        this.materialName = materialName;
        this.materialType = materialType;
        this.materialPrice = materialPrice;
        this.count = count;
        this.minCount = minCount;
        this.countInPack = countInPack;
        this.dimUnit = dimUnit;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public BigDecimal getCountInPack() {
        return countInPack;
    }

    public void setCountInPack(BigDecimal countInPack) {
        this.countInPack = countInPack;
    }

    public String getDimUnit() {
        return dimUnit;
    }

    public void setDimUnit(String dimUnit) {
        this.dimUnit = dimUnit;
    }
}
