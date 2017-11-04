package com.sakurov.notes.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    //опять же странный нейминг. как класса, так и метода.
    //"dd.MM.yyyy HH:mm" в константу с более-менее говорящим названием. DATE_TIME_FORMAT?
    //класс во что-то типа DateUtils, метод камел кейсом, во что-то адекватное. DateUtils.getFormattedDate(pattern)
    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat SIMPLE_DATE =
            new SimpleDateFormat("dd.MM.yyyy HH.mm");

    public static String getDate(long timeInMillis) {
        Date date = new Date();
        date.setTime(timeInMillis);
        return SIMPLE_DATE.format(date);
    }
}
