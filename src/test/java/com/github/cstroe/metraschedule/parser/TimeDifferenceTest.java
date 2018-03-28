package com.github.cstroe.metraschedule.parser;

import com.github.cstroe.metraschedule.domain.Time;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeDifferenceTest {
    @Test
    public void compute() {
        Time t1 = Time.of(1, 30, true);
        Time t2 = Time.of(1, 45, true);
        assertEquals("+15", TimeDifference.diff(t1, t2));
        assertEquals("-15", TimeDifference.diff(t2, t1));
    }
}