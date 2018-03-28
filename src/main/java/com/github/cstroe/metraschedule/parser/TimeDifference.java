package com.github.cstroe.metraschedule.parser;

import com.github.cstroe.metraschedule.domain.Time;

import static java.lang.String.format;

public class TimeDifference {
    public static String diff(Time t1, Time t2) {
        int hourDiff = t2.hour - t1.hour;
        int minDiff = t2.minute - t1.minute;

        StringBuilder builder = new StringBuilder();

        if(t2.minute < t1.minute && hourDiff > 0) {
            hourDiff = (t2.hour - 1) - t1.hour;
            minDiff = (t2.minute + 60) - t1.minute;
        }

        if(t2.minute > t1.minute && hourDiff < 0) {
            hourDiff = t2.hour - (t1.hour - 1);
            minDiff = t2.minute - (t1.minute + 60);
        }

        if(hourDiff != 0) {
            builder.append(format("%+d", hourDiff));
            builder.append(":");
            builder.append(format("%02d", Math.abs(minDiff)));
        } else {
            builder.append(format("%+02d", minDiff));
        }
        return builder.toString();
    }
}
