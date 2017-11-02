package com.sakurov.notes.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable, Item {

    private long id;
    private final long userId;
    private String text;
    private final long dateCreated;
    private long dateEdited;

    public Note(long id, long userId, String text, long dateCreated, long dateEdited) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
    }

    public Note(long userId, String text) {
        this.userId = userId;
        this.text = text;
        this.dateCreated = System.currentTimeMillis();
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
        this.dateEdited = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public long getUserId() {
        return userId;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public long getDateEdited() {
        return dateEdited;
    }

//--------------------------------Parcelable implementation-----------------------------------------

    Note(Parcel in) {
        id = in.readLong();
        userId = in.readLong();
        text = in.readString();
        dateCreated = in.readLong();
        dateEdited = in.readLong();
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
        dest.writeLong(dateCreated);
        dest.writeLong(dateEdited);
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
}