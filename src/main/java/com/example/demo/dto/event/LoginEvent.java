package com.example.demo.dto.event;

public class LoginEvent {
    private String userName;
    private String timestamp;

    public LoginEvent(String username, String timestamp) {
        this.userName = username;
        this.timestamp = timestamp;
    }

    public void setUserName(String userName) {this.userName = userName;}
    public String getUserName() {return userName;}
    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}
    public String getTimestamp() {return timestamp;}
}