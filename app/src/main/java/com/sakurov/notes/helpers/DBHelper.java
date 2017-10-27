package com.sakurov.notes.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 1;

    static final String USERS = "users";
    static final String ID = "id";
    static final String NAME = "name";
    static final String PASSWORD = "password";

    static final String NOTES = "notes";
    static final String USER_ID = "user_id";
    static final String TEXT = "text";
    static final String DATE_CREATED = "date_created";
    static final String DATE_EDITED = "date_edited";

    static final String NOTIFICATIONS = "notifications";
    static final String TIME_NOTIFICATION = "time_notification";

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + USERS + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT,"
                + PASSWORD + " TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE " + NOTES + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " INTEGER,"
                + TEXT + " TEXT,"
                + DATE_CREATED + " TEXT,"
                + DATE_EDITED + " TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE " + NOTIFICATIONS + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_ID + " INTEGER,"
                + TEXT + " TEXT,"
                + DATE_CREATED + " TEXT,"
                + DATE_EDITED + " TEXT,"
                + TIME_NOTIFICATION + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTIFICATIONS);
        onCreate(sqLiteDatabase);
    }
}
