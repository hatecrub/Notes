package com.sakurov.notes.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakurov on 23.10.17.
 */

public class DBSource {

    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public DBSource(Context context) {
        this.mContext = context;
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


    public boolean checkUser(User user) {
        read();
        Cursor cursor = null;
        String password;
        try {
            cursor = mDatabase.
                    rawQuery("SELECT " +
                                    DBHelper.PASSWORD +
                                    " FROM " +
                                    DBHelper.USERS +
                                    " WHERE " +
                                    DBHelper.NAME + "=?",
                            new String[]{user.getName()});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                password = cursor.getString(cursor.getColumnIndex(DBHelper.PASSWORD));
                return user.getPassword().equals(password);
            } else
                return false;
        } catch (NullPointerException e) {
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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

    public List<Note> getNotes(User user) {
        List<Note> notes = new ArrayList<>();
        read();
        Cursor c = mDatabase.query(DBHelper.NOTES, null,
                DBHelper.USER_ID + "=?",
                new String[]{"" + user.getId()},
                null, null, null);
        if (c.moveToFirst()) {
            do {
                int idColIndex = c.getColumnIndex(DBHelper.ID);
                int userIdColIndex = c.getColumnIndex(DBHelper.USER_ID);
                int textColIndex = c.getColumnIndex(DBHelper.TEXT);
                int createdColIndex = c.getColumnIndex(DBHelper.DATE_CREATED);
                int editedColIndex = c.getColumnIndex(DBHelper.DATE_EDITED);

                notes.add(new Note(c.getInt(idColIndex),
                        c.getLong(userIdColIndex),
                        c.getString(textColIndex),
                        c.getString(createdColIndex),
                        c.getString(editedColIndex)));
            } while (c.moveToNext());
        } else {
            c.close();
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

        long rowId = mDatabase.insertOrThrow(DBHelper.NOTES, null, cvNote);

        close();

        return rowId;
    }

}
