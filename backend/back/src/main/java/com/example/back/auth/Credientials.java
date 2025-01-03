package com.example.back.auth;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class Credientials {
    private String login;
    private String password;
    private boolean is_admin;


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

}
