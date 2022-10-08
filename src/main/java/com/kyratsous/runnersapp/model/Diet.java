package com.kyratsous.runnersapp.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "diets")
public class Diet extends BaseEntity{
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String body;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "nutritionist_id", nullable = false)
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
