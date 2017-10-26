package com.sakurov.notes.entities;

/**
 * Created by sakurov on 26.10.17.
 */

public class Notification extends Note {

    private String date;
    private String time;

    Notification(long userId, String text, String date, String time) {
        super(userId, text);
        this.date = date;
        this.time = time;
    }

    Notification(long id, long userId, String text, String dateCreated, String dateEdited,String date, String time){
        super(id, userId, text, dateCreated, dateEdited);
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
