package com.sakurov.notes.entities;

import java.util.Date;

public class Note {

    private long id;
    private final long userId;
    private String text;
    private final String dateCreated;
    private String dateEdited;

    public Note(long id, long userId, String text, String dateCreated, String dateEdited) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
    }

    public Note(long userId, String text) {
        this.userId = userId;
        this.text = text;
        this.dateCreated = new Date().toString();
        this.dateEdited = this.dateCreated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
        this.dateEdited = new Date().toString();
    }

    public String getText() {
        return text;
    }

    public long getUserId() {
        return userId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateEdited() {
        return dateEdited;
    }
}