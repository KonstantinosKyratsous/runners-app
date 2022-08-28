package com.kyratsous.runnersapp.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "coach_id")
    private User coach;

    @NotNull
    private String experience;
    @ElementCollection
    @NotNull
    private Set<String> distanceOptions;

    @ElementCollection
    @NotNull
    private Set<String> fieldOptions;

    public Exercise() {

    }

    public Exercise(String title, String body, Date date, User coach, String experience, Set<String> distanceOptions, Set<String> fieldOptions) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.coach = coach;
        this.experience = experience;
        this.distanceOptions = distanceOptions;
        this.fieldOptions = fieldOptions;
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Set<String> getDistanceOptions() {
        return distanceOptions;
    }

    public void setDistanceOptions(Set<String> raceTypes) {
        this.distanceOptions = raceTypes;
    }

    public Set<String> getFieldOptions() {
        return fieldOptions;
    }

    public void setFieldOptions(Set<String> fieldOptions) {
        this.fieldOptions = fieldOptions;
    }
}
