package com.example.farooq.notificationpanel.fragments;

public class Notification {
    String from;
    String message;

    public Notification() { }
    public Notification(String from, String message) {
        setFrom(from);
        setMessage(message);
    }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
