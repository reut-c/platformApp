package com.platform.supersonic.platformapplication;

/**
 * Created by chen.l on 22/06/16.
 */
public class User {
    public User(String user, String password) {
        this.user = user;
        this.password = password;
    }

    private String user;
    private String password;

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
