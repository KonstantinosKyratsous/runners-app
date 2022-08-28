package com.kyratsous.runnersapp.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @NotNull
    private String name;
    @NotNull
    private String category;
    @NotNull
    private String type;
    @NotNull
    private byte[] image;
    private double price;
    @NotNull
    private String description;
    @ElementCollection
    private Set<String> pros;
    @ElementCollection
    private Set<String> cons;

    private Date date;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "coach_id")
    private User coach;

    public Product() {}

    public Product(String name, String category, String type, byte[] image, double price, String description, Set<String> pros, Set<String> cons, Date date) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.image = image;
        this.price = price;
        this.description = description;
        this.pros = pros;
        this.cons = cons;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getPros() {
        return pros;
    }

    public void setPros(Set<String> pros) {
        this.pros = pros;
    }

    public Set<String> getCons() {
        return cons;
    }

    public void setCons(Set<String> cons) {
        this.cons = cons;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

}
