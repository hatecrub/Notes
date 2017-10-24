package com.sakurov.notes.entities;

/**
 * Created by sakurov on 23.10.17.
 */

public class Note {

    private long id;
    private long userId;
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
        this.dateCreated = String.valueOf(System.currentTimeMillis());
        setDateEdited(System.currentTimeMillis());
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
        setDateEdited(System.currentTimeMillis());
    }

    public long getUserId() {
        return userId;
    }

    public void setDateEdited(long dateEdited) {
        this.dateEdited = String.valueOf(dateEdited);
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateEdited() {
        return dateEdited;
    }
}