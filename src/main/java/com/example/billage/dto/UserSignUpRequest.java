package com.example.billage.dto;

public class UserSignUpRequest {
    private String email;
    private String nickname;
    private String password;

    public UserSignUpRequest(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
