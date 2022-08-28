package com.kyratsous.runnersapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotEmpty
    private String password;

    @ElementCollection
    @NotNull
    @CollectionTable(name = "authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id"),
                           @JoinColumn(name = "username", referencedColumnName = "username")})
    @Column(name = "authority")
    private Set<String> authorities;
    @ElementCollection
    private Set<String> distancePrefs;
    private String experience;
    @ElementCollection
    private Set<String> fieldTypes;
    private int enabled = 1;

    public User() {
    }

    public User(String firstName, String lastName, String username, String email, String password, Set<String> authorities, Set<String> distancePrefs, String experience, Set<String> fieldTypes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.distancePrefs = distancePrefs;
        this.experience = experience;
        this.fieldTypes = fieldTypes;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<String> getDistancePrefs() {
        return distancePrefs;
    }

    public void setDistancePrefs(Set<String> distancePrefs) {
        this.distancePrefs = distancePrefs;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Set<String> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(Set<String> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    public String getPreferencesToString(boolean needFields, boolean needDistances, boolean needExperience) {
        String customUrl = "";

        if (!distancePrefs.isEmpty() && needDistances) {
            customUrl += "distance=";
            for (String pref: distancePrefs)
                customUrl += (customUrl.endsWith("distance=")) ? pref : "," + pref;
        }

        if (!fieldTypes.isEmpty() && needFields) {
            customUrl += (customUrl.contains("distance"))? "&field=": "field=";
            for (String field: fieldTypes)
                customUrl += (customUrl.endsWith("field="))? field: "," + field;
        }

        if (experience != null && needExperience)
            customUrl += (customUrl.contains("distance")? "&experience=": "experience=") + experience;

        return customUrl;
    }
}
