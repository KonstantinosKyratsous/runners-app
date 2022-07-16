package com.kyratsous.runnersapp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "diets")
public class Diet extends BaseEntity{

    private String title;
    private String body;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "nutritionist_id")
    private User nutritionist;

    public Diet() {

    }

    public Diet(String title, String body, Date date, User nutritionist) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.nutritionist = nutritionist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getNutritionist() {
        return nutritionist;
    }

    public void setNutritionist(User nutritionist) {
        this.nutritionist = nutritionist;
    }
}
