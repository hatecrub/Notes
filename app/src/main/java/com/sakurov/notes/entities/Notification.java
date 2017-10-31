package com.sakurov.notes.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.sakurov.notes.data.DataSource;

import java.util.Date;

public class Notification extends Note implements Parcelable, Item {

    private long timeInMillis;

    public Notification(long userId, String text, long timeInMillis) {
        super(userId, text);
        this.timeInMillis = timeInMillis;
    }

    public Notification(long id, long userId, String text, String dateCreated, String dateEdited, long timeInMillis) {
        super(id, userId, text, dateCreated, dateEdited);
        this.timeInMillis = timeInMillis;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

//--------------------------------Parcelable implementation-----------------------------------------

    private Notification(Parcel in) {
        super(in);
        timeInMillis = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(timeInMillis);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

//-----------------------------------Item Implementation------------------------------------------

    @Override
    public int getItemType() {
        if (System.currentTimeMillis() > getTimeInMillis()) {
            return Item.NOTIFICATION_OUTDATED;
        } else return Item.NOTIFICATION;
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
        return SIMPLE_DATE.format(new Date(getTimeInMillis()));
    }
}