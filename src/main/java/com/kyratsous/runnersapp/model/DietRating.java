package com.kyratsous.runnersapp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "diet_ratings")
public class DietRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private Diet diet;

    public DietRating() {}

    public DietRating(Diet diet) {
        this.diet = diet;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }
}
