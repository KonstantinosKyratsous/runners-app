package com.kyratsous.runnersapp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "exercises")
public class Exercise extends BaseEntity {

    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;

    public Exercise() {

    }

    public Exercise(String title, String body, User coach) {
        this.title = title;
        this.body = body;
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

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }
}
