package com.kyratsous.runnersapp.model;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority extends BaseEntity{

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            @JoinColumn(name = "username", referencedColumnName = "username")
    })
    private User user;

    @Column(name = "authority")
    private String type;

    public Authority() {

    }

    public Authority(User user, String type) {
        this.user = user;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
