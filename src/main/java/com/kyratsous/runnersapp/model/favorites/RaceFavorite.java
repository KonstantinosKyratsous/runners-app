package com.kyratsous.runnersapp.model.favorites;

import com.kyratsous.runnersapp.model.Race;
import com.kyratsous.runnersapp.model.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "race_favorites")
public class RaceFavorite extends Favorite {
    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    public RaceFavorite() {}

    public RaceFavorite(Race race, User user) {
        super(user);
        this.race = race;

    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
