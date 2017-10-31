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

public class DataSource {

    private static final long DO_NOT_EXIST = -1;

    private DBHelper mHelper;
    private ContentResolver mContentResolver;

    public DataSource(Context context) {
        this.mHelper = new DBHelper(context);
        this.mContentResolver = context.getContentResolver();
    }

    private void close() {
        mHelper.close();
    }

    public long addUser(User user) {
        ContentValues cvUser = new ContentValues();
        cvUser.put(DBHelper.NAME, user.getName());
        cvUser.put(DBHelper.PASSWORD, user.getPassword());
        Uri rowUri = mContentResolver.insert(Entry.USERS_URI, cvUser);
        close();
        return ContentUris.parseId(rowUri);
    }

    public long getUserId(User user) {
        Cursor cursor = mContentResolver
                .query(Entry.USERS_URI,
                        new String[]{DBHelper.ID},
                        DBHelper.NAME + "=?",
                        new String[]{user.getName()},
                        null);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(DBHelper.ID));
            cursor.close();
            close();
            return id;
        } else {
            cursor.close();
            return DO_NOT_EXIST;
        }
    }

    public boolean checkUser(User user) {
        Cursor cursor = mContentResolver
                .query(Entry.USERS_URI,
                        new String[]{DBHelper.PASSWORD},
                        DBHelper.NAME + "=?",
                        new String[]{user.getName()},
                        null);
        if (cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndex(DBHelper.PASSWORD));
            cursor.close();
            close();
            return user.getPassword().equals(password);
        } else {
            cursor.close();
            return false;
        }
    }

//------------------------------------------NOTE-----------------------------------------------

    public List<Note> getNotes(long userId) {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = mContentResolver
                .query(Entry.NOTES_URI,
                        null,
                        DBHelper.USER_ID + "=?",
                        new String[]{"" + userId},
                        null);
        if (cursor.moveToFirst()) {
            do {
                notes.add(
                        new Note(
                                cursor.getInt(cursor.getColumnIndex(DBHelper.ID)),
                                cursor.getLong(cursor.getColumnIndex(DBHelper.USER_ID)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.TEXT)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.DATE_CREATED)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.DATE_EDITED))));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            cursor.close();
        }
        close();
        return notes;
    }

    public long addNote(Note note) {
        ContentValues cvNote = new ContentValues();
        cvNote.put(DBHelper.USER_ID, note.getUserId());
        cvNote.put(DBHelper.TEXT, note.getText());
        cvNote.put(DBHelper.DATE_CREATED, note.getDateCreated());
        cvNote.put(DBHelper.DATE_EDITED, note.getDateEdited());
        Uri rowUri = mContentResolver
                .insert(Entry.NOTES_URI, cvNote);
        close();
        return ContentUris.parseId(rowUri);
    }

    public void updateNote(Note note) {
        ContentValues cvNote = new ContentValues();
        cvNote.put(DBHelper.USER_ID, note.getUserId());
        cvNote.put(DBHelper.TEXT, note.getText());
        cvNote.put(DBHelper.DATE_CREATED, note.getDateCreated());
        cvNote.put(DBHelper.DATE_EDITED, note.getDateEdited());
        mContentResolver
                .update(Uri.parse(Entry.NOTE_ID_PATH + note.getId()),
                        cvNote,
                        null, null);
        close();
    }

//--------------------------------------NOTIFICATION---------------------------------------------

    public List<Notification> getNotifications(long userId) {
        List<Notification> notifications = new ArrayList<>();
        Cursor cursor = mContentResolver
                .query(Entry.NOTIFICATIONS_URI,
                        null,
                        DBHelper.USER_ID + "=?",
                        new String[]{"" + userId},
                        null);
        if (cursor.moveToFirst()) {
            do {
                notifications.add(
                        new Notification(
                                cursor.getInt(cursor.getColumnIndex(DBHelper.ID)),
                                cursor.getLong(cursor.getColumnIndex(DBHelper.USER_ID)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.TEXT)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.DATE_CREATED)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.DATE_EDITED)),
                                cursor.getLong(cursor.getColumnIndex(DBHelper.TIME_NOTIFICATION))));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            cursor.close();
        }
        close();
        return notifications;
    }

    public long addNotification(Notification notification) {
        ContentValues cvNotification = new ContentValues();
        cvNotification.put(DBHelper.USER_ID, notification.getUserId());
        cvNotification.put(DBHelper.TEXT, notification.getText());
        cvNotification.put(DBHelper.DATE_CREATED, notification.getDateCreated());
        cvNotification.put(DBHelper.DATE_EDITED, notification.getDateEdited());
        cvNotification.put(DBHelper.TIME_NOTIFICATION, notification.getTimeInMillis());
        Uri rowUri = mContentResolver.insert(Entry.NOTIFICATIONS_URI, cvNotification);
        close();
        return ContentUris.parseId(rowUri);
    }

    public void updateNotification(Notification notification) {
        ContentValues cvNotification = new ContentValues();
        cvNotification.put(DBHelper.USER_ID, notification.getUserId());
        cvNotification.put(DBHelper.TEXT, notification.getText());
        cvNotification.put(DBHelper.DATE_CREATED, notification.getDateCreated());
        cvNotification.put(DBHelper.DATE_EDITED, notification.getDateEdited());
        cvNotification.put(DBHelper.TIME_NOTIFICATION, notification.getTimeInMillis());
        mContentResolver.update(Uri.parse(Entry.NOTIFICATION_ID_PATH + notification.getId()),
                cvNotification, null, null);
        close();
    }

    public List<Item> getAllRecords(long userId) {
        List<Item> items = new ArrayList<>();
        items.addAll(getNotes(userId));
        items.addAll(getNotifications(userId));
        return items;
    }
}
