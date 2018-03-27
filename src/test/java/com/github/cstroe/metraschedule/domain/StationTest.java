package com.github.cstroe.metraschedule.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class StationTest {
    private final Station a = Station.of(1, "a");
    private final Station aprime = Station.of("1", "a'");
    private final Station b = Station.of(2, "b");

    @Test
    public void hash_code() {
        assertEquals(a.hashCode(), aprime.hashCode());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals() {
        assertTrue(a.equals(aprime));
        assertTrue(aprime.equals(a));
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
        assertFalse(aprime.equals(b));
        assertFalse(b.equals(aprime));
    }
}