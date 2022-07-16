package com.kyratsous.runnersapp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "exercises")
public class Exercise extends BaseEntity {

    @NotNull
    private String title;
    @NotNull
    private String body;
    @NotNull
    private Date date;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;

    public Exercise() {

    }

    public Exercise(String title, String body, Date date, User coach) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.coach = coach;
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

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }
}
