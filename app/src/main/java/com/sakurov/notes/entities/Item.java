package com.sakurov.notes.entities;

public interface Item {

    int NOTE = 1;
    int NOTIFICATION = 2;
    int NOTIFICATION_OUTDATED = 3;

    int getItemType();

    String getItemText();

    String getItemDateCreated();

    String getItemTimeMillis();
}