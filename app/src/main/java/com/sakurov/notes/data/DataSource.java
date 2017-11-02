package com.sakurov.notes.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sakurov.notes.entities.Item;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.Notification;
import com.sakurov.notes.entities.User;

import java.util.ArrayList;
import java.util.List;

import com.sakurov.notes.data.NotesContract.Entry;
import com.sakurov.notes.data.DBHelper.Schema;

public class DataSource {

    private final static String Q = "=?";

    private static final long DO_NOT_EXIST = -1;

    private ContentResolver mContentResolver;

    public DataSource(Context context) {
        this.mContentResolver = context.getContentResolver();
    }

    public long addUser(User user) {
        Uri rowUri = mContentResolver.insert(Entry.USERS_URI,
                getContentValuesForUser(user));
        return ContentUris.parseId(rowUri);
    }

    public boolean isUserExist(User user) {
        Cursor cursor = getUserColumnByName(Schema.NAME, user.getName());
        try {
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public boolean checkUserPass(User user) {
        Cursor cursor = getUserColumnByName(Schema.PASSWORD, user.getName());
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String password = cursor.getString(cursor.getColumnIndex(Schema.PASSWORD));
                return user.getPassword().equals(password);
            } else return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public long getUserId(User user) {
        Cursor cursor = getUserColumnByName(Schema.ID, user.getName());
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex(Schema.ID));
            } else return DO_NOT_EXIST;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    private Cursor getUserColumnByName(String column, String userName) {
        return mContentResolver
                .query(Entry.USERS_URI,
                        new String[]{column},
                        Schema.NAME + Q,
                        new String[]{userName},
                        null);
    }

    public Note getNoteById(long noteId) {
        Note note = null;
        Cursor cursor = mContentResolver
                .query(Entry.NOTES_URI,
                        null,
                        Schema.ID + Q,
                        new String[]{String.valueOf(noteId)},
                        null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                note = new Note(cursor.getInt(cursor.getColumnIndex(Schema.ID)),
                        cursor.getLong(cursor.getColumnIndex(Schema.USER_ID)),
                        cursor.getString(cursor.getColumnIndex(Schema.TEXT)),
                        cursor.getString(cursor.getColumnIndex(Schema.DATE_CREATED)),
                        cursor.getString(cursor.getColumnIndex(Schema.DATE_EDITED)));
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return note;
    }

    private List<Note> getNotes(long userId) {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = mContentResolver
                .query(Entry.NOTES_URI,
                        null,
                        Schema.USER_ID + Q,
                        new String[]{String.valueOf(userId)},
                        null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    notes.add(new Note(cursor.getInt(cursor.getColumnIndex(Schema.ID)),
                            cursor.getLong(cursor.getColumnIndex(Schema.USER_ID)),
                            cursor.getString(cursor.getColumnIndex(Schema.TEXT)),
                            cursor.getString(cursor.getColumnIndex(Schema.DATE_CREATED)),
                            cursor.getString(cursor.getColumnIndex(Schema.DATE_EDITED))));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return notes;
    }

    public long addNote(Note note) {
        Uri rowUri = mContentResolver
                .insert(Entry.NOTES_URI,
                        getContentValuesForNote(note));
        return ContentUris.parseId(rowUri);
    }

    public void updateNote(Note note) {
        mContentResolver
                .update(Uri.parse(Entry.NOTE_ID_PATH + note.getId()),
                        getContentValuesForNote(note),
                        null,
                        null);
    }

    public Notification getNotificationById(long notificationId) {
        Notification notification = null;
        Cursor cursor = mContentResolver
                .query(Entry.NOTIFICATIONS_URI,
                        null,
                        Schema.ID + Q,
                        new String[]{String.valueOf(notificationId)},
                        null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                notification = new Notification(cursor.getInt(cursor.getColumnIndex(Schema.ID)),
                        cursor.getLong(cursor.getColumnIndex(Schema.USER_ID)),
                        cursor.getString(cursor.getColumnIndex(Schema.TEXT)),
                        cursor.getString(cursor.getColumnIndex(Schema.DATE_CREATED)),
                        cursor.getString(cursor.getColumnIndex(Schema.DATE_EDITED)),
                        cursor.getLong(cursor.getColumnIndex(Schema.TIME_NOTIFICATION)));
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return notification;
    }

    private List<Notification> getNotifications(long userId) {
        List<Notification> notifications = new ArrayList<>();
        Cursor cursor = mContentResolver
                .query(Entry.NOTIFICATIONS_URI,
                        null,
                        Schema.USER_ID + Q,
                        new String[]{String.valueOf(userId)},
                        null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    notifications.add(new Notification(cursor.getInt(cursor.getColumnIndex(Schema.ID)),
                            cursor.getLong(cursor.getColumnIndex(Schema.USER_ID)),
                            cursor.getString(cursor.getColumnIndex(Schema.TEXT)),
                            cursor.getString(cursor.getColumnIndex(Schema.DATE_CREATED)),
                            cursor.getString(cursor.getColumnIndex(Schema.DATE_EDITED)),
                            cursor.getLong(cursor.getColumnIndex(Schema.TIME_NOTIFICATION))));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return notifications;
    }

    public long addNotification(Notification notification) {
        Uri rowUri = mContentResolver.insert(Entry.NOTIFICATIONS_URI,
                getContentValuesForNotification(notification));
        return ContentUris.parseId(rowUri);
    }

    public void updateNotification(Notification notification) {
        mContentResolver.update(Uri.parse(Entry.NOTIFICATION_ID_PATH + notification.getId()),
                getContentValuesForNotification(notification),
                null,
                null);
    }

    public List<Item> getAllRecords(long userId) {
        List<Item> items = new ArrayList<>();
        items.addAll(getNotes(userId));
        items.addAll(getNotifications(userId));
        return items;
    }

    private static ContentValues getContentValuesForUser(User user) {
        ContentValues cvUser = new ContentValues();
        cvUser.put(Schema.NAME, user.getName());
        cvUser.put(Schema.PASSWORD, user.getPassword());
        return cvUser;
    }

    private static ContentValues getContentValuesForNote(Note note) {
        ContentValues cvNote = new ContentValues();
        cvNote.put(Schema.USER_ID, note.getUserId());
        cvNote.put(Schema.TEXT, note.getText());
        cvNote.put(Schema.DATE_CREATED, note.getDateCreated());
        cvNote.put(Schema.DATE_EDITED, note.getDateEdited());
        return cvNote;
    }

    private static ContentValues getContentValuesForNotification(Notification notification) {
        ContentValues cvNotification = new ContentValues();
        cvNotification.put(Schema.USER_ID, notification.getUserId());
        cvNotification.put(Schema.TEXT, notification.getText());
        cvNotification.put(Schema.DATE_CREATED, notification.getDateCreated());
        cvNotification.put(Schema.DATE_EDITED, notification.getDateEdited());
        cvNotification.put(Schema.TIME_NOTIFICATION, notification.getTimeInMillis());
        return cvNotification;
    }
}