package com.sakurov.notes.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;

import java.util.ArrayList;
import java.util.List;

public class DBSource {

    private static final long DO_NOT_EXIST = -1;

    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBSource(Context context) {
        this.mHelper = new DBHelper(context);
    }

    private void write() {
        mDatabase = mHelper.getWritableDatabase();
    }

    private void read() {
        mDatabase = mHelper.getReadableDatabase();
    }

    private void close() {
        mHelper.close();
    }

    public long addUser(User user) {
        write();
        ContentValues cvUser = new ContentValues();
        cvUser.put(DBHelper.NAME, user.getName());
        cvUser.put(DBHelper.PASSWORD, user.getPassword());
        long rowId = mDatabase.insertOrThrow(DBHelper.USERS, null, cvUser);
        close();
        return rowId;
    }

    public long getUserId(User user) {
        read();
        Cursor cursor = mDatabase
                .query(DBHelper.USERS,
                        new String[]{DBHelper.ID},
                        DBHelper.NAME + "=?",
                        new String[]{user.getName()},
                        null,
                        null,
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
        read();
        Cursor cursor = mDatabase
                .query(DBHelper.USERS,
                        new String[]{DBHelper.PASSWORD},
                        DBHelper.NAME + "=?",
                        new String[]{user.getName()},
                        null,
                        null,
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

    public List<Note> getNotes(long userId) {
        List<Note> notes = new ArrayList<>();
        read();
        Cursor cursor = mDatabase
                .query(DBHelper.NOTES,
                        null,
                        DBHelper.USER_ID + "=?",
                        new String[]{"" + userId},
                        null,
                        null,
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
        write();
        ContentValues cvNote = new ContentValues();
        cvNote.put(DBHelper.USER_ID, note.getUserId());
        cvNote.put(DBHelper.TEXT, note.getText());
        cvNote.put(DBHelper.DATE_CREATED, note.getDateCreated());
        cvNote.put(DBHelper.DATE_EDITED, note.getDateEdited());
        long rowId = mDatabase
                .insertOrThrow(DBHelper.NOTES,
                        null,
                        cvNote);
        close();
        return rowId;
    }

    public void updateNote(Note note) {
        write();
        ContentValues cvNote = new ContentValues();
        cvNote.put(DBHelper.USER_ID, note.getUserId());
        cvNote.put(DBHelper.TEXT, note.getText());
        cvNote.put(DBHelper.DATE_CREATED, note.getDateCreated());
        cvNote.put(DBHelper.DATE_EDITED, note.getDateEdited());
        mDatabase
                .update(DBHelper.NOTES,
                        cvNote,
                        DBHelper.ID + "=?",
                        new String[]{"" + note.getId()});
        close();
    }
}
