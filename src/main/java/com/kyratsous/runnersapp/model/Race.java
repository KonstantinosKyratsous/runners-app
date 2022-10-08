package com.kyratsous.runnersapp.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "races")
public class Race extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String description;

    @ElementCollection
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@Column(nullable = false)
    private Set<String> distanceOptions;

    @ElementCollection
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@Column(nullable = false)
    private Set<String> fieldOptions;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private double price;
    private String registrationLink;
    private byte[] file;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "organizer_id", referencedColumnName = "id", nullable = false)
    private User organizer;

    public Race() {}

    public Race(User organizer, String title, String description, Set<String> distanceOptions, Set<String> fieldOptions, double latitude, double longitude, String location, Date date, double price, String registrationLink, byte[] file) {
        this.organizer = organizer;
        this.title = title;
        this.description = description;
        this.fieldOptions = fieldOptions;
        this.distanceOptions = distanceOptions;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.date = date;
        this.price = price;
        this.registrationLink = registrationLink;
        this.file = file;
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

    public Set<String> getDistanceOptions() {
        return distanceOptions;
    }

    public void setDistanceOptions(Set<String> distanceTypes) {
        this.distanceOptions = distanceTypes;
    }

    public Set<String> getFieldOptions() {
        return fieldOptions;
    }

    public void setFieldOptions(Set<String> fieldTypes) {
        this.fieldOptions = fieldTypes;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getRegistrationLink() {
        return registrationLink;
    }

    public void setRegistrationLink(String registrationLink) {
        this.registrationLink = registrationLink;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
