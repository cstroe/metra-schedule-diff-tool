import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimeParserTest {
    private void assertResult(String expected, String input) {
        TimeParserResult result = new TimeParser().parse(input);
        assertTrue(result.hasValue());
        assertEquals(expected, result.getValue());
    }

    private void assertResult(String expected, String previousInput, String currentInput) {
        TimeParserResult result = new TimeParser().parse(previousInput, currentInput);
        assertTrue(result.hasValue());
        assertEquals(expected, result.getValue());
    }

    @Test
    public void parse001() {
        assertResult("9:01 AM", "901");
        assertResult("9:01 PM", "901p");
        assertResult("10:01 AM", "1001");
        assertResult("10:01 PM", "1001p");
    }

    @Test
    public void parse002() {
        assertResult("9:01 AM", "9:01 AM", "901");
        assertResult("9:01 PM", "901p");
        assertResult("10:01 AM", "1001");
        assertResult("10:01 PM", "1001p");
    }
}