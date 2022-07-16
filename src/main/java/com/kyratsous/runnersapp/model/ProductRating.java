package com.kyratsous.runnersapp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_ratings")
public class ProductRating extends Rating{

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductRating() {

    }

    public ProductRating(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
