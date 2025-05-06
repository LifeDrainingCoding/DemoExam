package com.lifedrained.demoexam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "products" , schema = "db")
public class Product extends AbstractEntity {

    @Column
    private String productType;

    @Column
    private String productName;

    @Column
    private String article;

    @Column
    private BigDecimal minPrice;

    public Product() {
        super();
    }

    public Product(String productType, String productName, String article, BigDecimal minPrice) {
        super();
        this.productType = productType;
        this.productName = productName;
        this.article = article;
        this.minPrice = minPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
}
