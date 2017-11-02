package com.sakurov.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 1;

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + Schema.USERS + " ("
                + Schema.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Schema.NAME + " TEXT,"
                + Schema.PASSWORD + " TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE " + Schema.NOTES + " ("
                + Schema.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Schema.USER_ID + " INTEGER,"
                + Schema.TEXT + " TEXT,"
                + Schema.DATE_CREATED + " INTEGER,"
                + Schema.DATE_EDITED + " INTEGER);");

        sqLiteDatabase.execSQL("CREATE TABLE " + Schema.NOTIFICATIONS + " ("
                + Schema.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Schema.USER_ID + " INTEGER,"
                + Schema.TEXT + " TEXT,"
                + Schema.DATE_CREATED + " INTEGER,"
                + Schema.DATE_EDITED + " INTEGER,"
                + Schema.TIME_NOTIFICATION + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Schema.USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Schema.NOTES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Schema.NOTIFICATIONS);
        onCreate(sqLiteDatabase);
    }

    static final class Schema {
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
    }
}