package com.github.cstroe.metraschedule.parser;

import com.github.cstroe.metraschedule.domain.Time;

public class TimeDifference {
    public static String diff(Time t1, Time t2) {
        int hourDiff = t2.hour - t1.hour;
        int minDiff = t2.minute - t1.minute;

        if(minDiff > 0) {
            return "+" + minDiff;
        } else {
            return "-" + Math.abs(minDiff);
        }
    }
}
