package com.github.cstroe.metraschedule.util;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

public class ReadUtilTest {
    @Test
    public void test_reading() throws FileNotFoundException {
        List<String> lines = ReadUtil.readFile("src/test/resources/testfile.txt");
        assertEquals(3, lines.size());
        assertEquals("line1", lines.get(0));
        assertEquals("line 2", lines.get(1));
        assertEquals("3", lines.get(2));
    }
}