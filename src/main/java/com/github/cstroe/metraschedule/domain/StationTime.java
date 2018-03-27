package com.github.cstroe.metraschedule.domain;

public class StationTime {
    public final Station station;
    public final Time time;

    private StationTime(Station station, Time time) {
        this.station = station;
        this.time = time;
    }

    public static StationTime of(Station station, Time time) {
        return new StationTime(station, time);
    }

    @Override
    public String toString() {
        return station + ": " + time;
    }
}
