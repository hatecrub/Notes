package com.sakurov.notes;

import com.sakurov.notes.helpers.*;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class NotesContentProvider extends ContentProvider {

    public final static int USERS = 100;
    public final static int NOTES = 200;
    public final static int NOTIFICATIONS = 300;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String TAG = NotesContentProvider.class.getSimpleName();

    private DBHelper mDBHelper;

    static {
        sUriMatcher.addURI("com.sakurov.notes", "users", USERS);
        sUriMatcher.addURI("com.sakurov.notes", "notes", NOTES);
        sUriMatcher.addURI("com.sakurov.notes", "notifications", NOTIFICATIONS);
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
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS: {
                cursor = database.query(DBHelper.USERS,
                        projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            }
            case NOTES: {
                cursor = database.query(DBHelper.NOTES,
                        projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            }
            case NOTIFICATIONS: {
                cursor = database.query(DBHelper.NOTIFICATIONS,
                        projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            }
            default: {
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
            }
        }

        if (cursor == null) {
            cursor = new MatrixCursor(new String[]{});
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS: {
                return insertUser(uri, values);
            }
            case NOTES: {
                return insertNote(uri, values);
            }
            case NOTIFICATIONS: {
                return insertNotification(uri, values);
            }
            default: {
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
//        switch (match) {
//            case NOTES: {
//                return insertNote(uri, values);
//            }
//            case NOTIFICATIONS: {
//                return insertNotification(uri, values);
//            }
//            default: {
//                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
//            }
//        }
        return 0;
    }

    private Uri insertUser(Uri uri, ContentValues values) {
        String name = values.getAsString(DBHelper.NAME);
        if (name == null) {
            Toast.makeText(getContext(), "User requires a name", Toast.LENGTH_SHORT).show();
            return null;
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        long id = database.insert(DBHelper.USERS, null, values);

        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertNote(Uri uri, ContentValues values) {
        String name = values.getAsString(DBHelper.TEXT);
        if (name == null) {
            Toast.makeText(getContext(), "Note can\'t be empty!", Toast.LENGTH_SHORT).show();
            return null;
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        long id = database.insert(DBHelper.NOTES, null, values);

        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertNotification(Uri uri, ContentValues values) {
        String name = values.getAsString(DBHelper.TEXT);
        if (name == null) {
            Toast.makeText(getContext(), "Notification can\'t be empty!", Toast.LENGTH_SHORT).show();
            return null;
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        long id = database.insert(DBHelper.NOTIFICATIONS, null, values);

        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }

}