package com.lifedrained.demoexam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "material_type", schema = "db")
public class MaterialType extends AbstractEntity {
    @Column
    private String type;

    @Column
    private String lossPercent;

    public MaterialType() {
        super();
    }

    public MaterialType(String type, String lossPercent) {
        super();
        this.type = type;
        this.lossPercent = lossPercent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLossPercent() {
        return lossPercent;
    }

    public void setLossPercent(String lossPercent) {
        this.lossPercent = lossPercent;
    }
}
