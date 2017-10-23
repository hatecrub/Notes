package com.sakurov.notes.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sakurov on 23.10.17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String USERS = "users";
    public static final String RELATIONS = "relations";
    public static final String NOTES = "notes";
    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 1;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";

    public static final String USER_ID = "user_id";
    public static final String NOTE_ID = "note_id";

    public static final String TEXT = "text";
    public static final String DATE_CREATED = "date_created";
    public static final String DATE_EDITED = "date_edited";

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + USERS + " ("
                + ID + " integer primary key autoincrement,"
                + NAME + " text,"
                + PASSWORD + " text);");

        sqLiteDatabase.execSQL("create table " + RELATIONS + " ("
                + ID + " integer primary key autoincrement,"
                + USER_ID + " integer,"
                + NOTE_ID + " integer);");

        sqLiteDatabase.execSQL("create table " + NOTES + " ("
                + ID + " integer primary key autoincrement,"
                + USER_ID + " integer,"
                + TEXT + " text,"
                + DATE_CREATED + " text,"
                + DATE_EDITED + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RELATIONS);
        onCreate(sqLiteDatabase);
    }
}
