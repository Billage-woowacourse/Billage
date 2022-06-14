package com.example.billage.domain;

public class User {

    private final Email email;
    private final Nickname nickname;
    private final String password;

    public User(String email, String nickname, String password) {
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.password = password;
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public String getPassword() {
        return password;
    }
}
