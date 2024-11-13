package me.yirf.mapRefresh.utils;

public class TimeUtil {

    public static String millisToString(long millis) {
        int seconds = Math.round((millis - System.currentTimeMillis()) / 1000);
        int minutes = seconds / 60;
        int hours = seconds / 3600;
        if (seconds < 60) {
            return seconds + "s";
        }
        if (minutes >= 1) {
            return minutes + "m";
        }
        if (hours >= 1) {
            return hours + "h";
        }

        return seconds + "s";
    }

    public static long compare(long millis) {
        return millis - System.currentTimeMillis();
    }
}
