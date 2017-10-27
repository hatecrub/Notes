package com.sakurov.notes.entities;

public interface Item {

    public final int NOTE = 1;
    public final int NOTIFICATION = 2;
    public final int NOTIFICATION_OUTDATED = 3;

    int getItemType();

    String getItemText();

    String getItemDateCreated();

    long getItemTimeMillis();
}
