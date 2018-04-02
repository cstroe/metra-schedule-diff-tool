package com.github.cstroe.metraschedule.domain;

public enum TrainDirection {
    INBOUND, OUTBOUND;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
