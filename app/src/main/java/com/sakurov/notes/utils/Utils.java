package com.sakurov.notes.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat SIMPLE_DATE =
            new SimpleDateFormat("dd.MM.yyyy HH.mm");

    public static String getDate(long timeInMillis) {
        Date date = new Date();
        date.setTime(timeInMillis);
        return SIMPLE_DATE.format(date);
    }
}
