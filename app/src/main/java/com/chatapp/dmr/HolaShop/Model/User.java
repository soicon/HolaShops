package com.chatapp.dmr.HolaShop.Model;

import java.util.List;

/**
 * Created by dmr on 9/8/2017.
 */

public class User {

    String id;
    String name;
    String email;
    String password;
    String profileUrl;


    public User() {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String id, String name, String email, String profileUrl) {

        this.id = id;
        this.name = name;
        this.email = email;

        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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
}
