package com.inovaworkscc.pilots.c3s;

import java.util.Date;

public class UnixTime {
    private final long value;

    public UnixTime(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new Date(value * 1000L).toString();
    }
}