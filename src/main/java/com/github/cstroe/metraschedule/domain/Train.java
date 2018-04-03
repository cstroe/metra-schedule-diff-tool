package com.github.cstroe.metraschedule.domain;

public class Train {
    public final int number;
    public final TrainDirection direction;

    public Train(int number, TrainDirection direction) {
        this.number = number;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Train " + number + " (" + direction.name() + ")";
    }
}
