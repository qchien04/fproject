package com.example.fproject.response;

public class RegisterRespone {

    private String username;
    private String email;
    private String password;


    public RegisterRespone(){};

    public RegisterRespone(String username,String mail, String password) {
        this.username = username;
        this.password = password;
        this.email=email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginVM{" +
                "username='" + username + '\'' +
                '}';
    }

}
