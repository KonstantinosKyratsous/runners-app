package com.kyratsous.runnersapp.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "training_plans")
public class TrainingPlan extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String body;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    @Column(nullable = false)
    private String experience;

    @ElementCollection
    //@Column(nullable = false)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<String> distanceOptions;

    @ElementCollection
    //@Column(nullable = false)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<String> fieldOptions;

    public TrainingPlan() {

    }

    public TrainingPlan(String title, String body, Date date, User coach, String experience, Set<String> distanceOptions, Set<String> fieldOptions) {
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
