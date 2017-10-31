package com.sakurov.notes.entities;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Parcelable, Item {

    private final static String NO_TIME = "";

    @SuppressLint("SimpleDateFormat")
    protected final static SimpleDateFormat SIMPLE_DATE =
            new SimpleDateFormat("dd.MM.yyyy HH.mm");

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
        this.dateCreated = SIMPLE_DATE.format(new Date());
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
        this.dateEdited = SIMPLE_DATE.format(new Date());
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

//--------------------------------Parcelable implementation-----------------------------------------

    Note(Parcel in) {
        id = in.readLong();
        userId = in.readLong();
        text = in.readString();
        dateCreated = in.readString();
        dateEdited = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(userId);
        dest.writeString(text);
        dest.writeString(dateCreated);
        dest.writeString(dateEdited);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

//-----------------------------------Item Implementation------------------------------------------

    @Override
    public int getItemType() {
        return Item.NOTE;
    }

    @Override
    public String getItemText() {
        return getText();
    }

    @Override
    public String getItemDateCreated() {
        return getDateCreated();
    }

    @Override
    public String getItemTimeMillis() {
        return NO_TIME;
    }
}