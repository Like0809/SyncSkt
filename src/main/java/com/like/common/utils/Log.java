package com.like.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Log {
    public static void withTime(String message) {
        String time = longToTime(System.currentTimeMillis());
        System.out.println(time + ": " + message);
    }

    private static String longToTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS",
                Locale.getDefault());
        return dateFormat.format(new Date(time));
    }
}
