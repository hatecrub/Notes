package com.sakurov.notes.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sakurov.notes.R;
import com.sakurov.notes.data.NotesContract.Entry;

public class NotesContentProvider extends ContentProvider {

    public final static int USERS = 100;
    public final static int NOTES = 200;
    public final static int NOTE_ID = 201;
    public final static int NOTIFICATIONS = 300;
    public final static int NOTIFICATION_ID = 301;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private DBHelper mDBHelper;

    static {
        sUriMatcher.addURI(Entry.AUTHORITY, Entry.USERS_PATH, USERS);
        sUriMatcher.addURI(Entry.AUTHORITY, Entry.NOTES_PATH, NOTES);
        sUriMatcher.addURI(Entry.AUTHORITY, Entry.NOTIFICATIONS_PATH, NOTIFICATIONS);
        sUriMatcher.addURI(Entry.AUTHORITY, Entry.NOTES_PATH + "/#", NOTE_ID);
        sUriMatcher.addURI(Entry.AUTHORITY, Entry.NOTIFICATIONS_PATH + "/#", NOTIFICATION_ID);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                return database.query(DBHelper.USERS,
                        projection, selection,
                        selectionArgs, null, null, sortOrder);

            case NOTES:
                return database.query(DBHelper.NOTES,
                        projection, selection,
                        selectionArgs, null, null, sortOrder);

            case NOTIFICATIONS:
                return database.query(DBHelper.NOTIFICATIONS,
                        projection, selection,
                        selectionArgs, null, null, sortOrder);

            default:
                throw new IllegalArgumentException(getContext().getString(R.string.error_unknown_uri) + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS: {
                return insertToTable(DBHelper.USERS, uri, values);
            }
            case NOTES: {
                return insertToTable(DBHelper.NOTES, uri, values);
            }
            case NOTIFICATIONS: {
                return insertToTable(DBHelper.NOTIFICATIONS, uri, values);
            }
            default: {
                throw new IllegalArgumentException(getContext().getString(R.string.error_unknown_uri) + uri);
            }
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTE_ID: {
                selection = DBHelper.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(DBHelper.NOTES, values, selection, selectionArgs);
            }
            case NOTIFICATION_ID: {
                selection = DBHelper.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(DBHelper.NOTIFICATIONS, values, selection, selectionArgs);
            }
            default: {
                throw new IllegalArgumentException(getContext().getString(R.string.error_unknown_uri) + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private Uri insertToTable(String table, Uri uri, ContentValues values) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        long id = database.insertOrThrow(table, null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    private int updateItem(String table, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        return database.update(table, values, selection, selectionArgs);
    }
}