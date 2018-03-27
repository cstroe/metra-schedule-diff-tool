package com.github.cstroe.metraschedule;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResultTest {
    @Test
    public void test_value() {
        Result<String, RuntimeException> r = new Result<>("somevalue");
        assertEquals("somevalue", r.getValue());
    }

    @Test
    public void test_error() {
        Result<String, RuntimeException> r = new Result<>(new RuntimeException("message"));
        assertEquals("message", r.getError().getMessage());
    }

    @Test(expected = RuntimeException.class)
    public void test_value_check() {
        Result<String, RuntimeException> r = new Result<>(new RuntimeException("message"));
        r.getValue();
    }

    @Test(expected = RuntimeException.class)
    public void test_error_check() {
        Result<String, RuntimeException> r = new Result<>("value");
        r.getError();
    }
}