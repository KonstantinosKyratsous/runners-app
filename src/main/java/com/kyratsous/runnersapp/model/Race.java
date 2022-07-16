package com.kyratsous.runnersapp.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "races")
public class Race extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private User organizer;
    private String title;
    private String description;
    private String location;
    private Date date;
    private double price;
    private String linkToBuyTickets;

    public Race() {

    }

    public Race(User organizer, String title, String description, String location, Date date, double price, String linkToBuyTickets) {
        this.organizer = organizer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.price = price;
        this.linkToBuyTickets = linkToBuyTickets;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLinkToBuyTickets() {
        return linkToBuyTickets;
    }

    public void setLinkToBuyTickets(String linkToBuyTickets) {
        this.linkToBuyTickets = linkToBuyTickets;
    }
}
