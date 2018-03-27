package com.github.cstroe.metraschedule.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class Time {
    private static final Pattern timePattern = Pattern.compile("([1-9]?[0-9]):([0-9]{2}) (AM|PM)");

    public final int hour;
    public final int minute;
    public final boolean am;

    private Time(String hour, String minute, String ampm) {
        this.hour = Integer.parseInt(hour);
        this.minute = Integer.parseInt(minute);
        am = "am".equals(ampm.toLowerCase());
    }

    private Time(int hour, int minute, boolean am) {
        this.hour = hour;
        this.minute = minute;
        this.am = am;
    }

    public static Time of(String hour, String minute, String ampm) {
        return new Time(hour, minute, ampm);
    }

    public static Time of(int hour, int minute, boolean am) {
        return new Time(hour, minute, am);
    }

    public static Time of(String time) {
        Matcher m = timePattern.matcher(time);
        if(m.matches()) {
            return new Time(m.group(1), m.group(2), m.group(3));
        }
        throw new IllegalArgumentException("Could not parse time.");
    }

    @Override
    public String toString() {
        if(am) {
            return format("%d:%02d AM", hour, minute);
        } else {
            return format("%d:%02d PM", hour, minute);
        }
    }
}
