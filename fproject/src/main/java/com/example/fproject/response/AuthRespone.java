package com.example.fproject.response;

public class AuthRespone {
    private String jwt;
    private boolean isAuth;

    public AuthRespone(String jwt,boolean isAuth) {
        this.isAuth = isAuth;
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }
}
