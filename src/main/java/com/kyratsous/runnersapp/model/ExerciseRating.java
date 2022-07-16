package com.kyratsous.runnersapp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "exercise_ratings")
public class ExerciseRating extends Rating{

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    public ExerciseRating() {

    }

    public ExerciseRating(Exercise exercise) {
        this.exercise = exercise;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
