import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {
    @Test
    public void toStringTest() {
        assertEquals("1:20 AM", new Time("1","20", "AM").toString());
        assertEquals("11:20 AM", new Time("11","20", "AM").toString());
        assertEquals("11:02 AM", new Time("11","2", "AM").toString());
        assertEquals("11:02 PM", new Time("11","2", "PM").toString());
        assertEquals("11:02 AM", new Time(11,2,true).toString());
        assertEquals("11:02 PM", new Time(11,2,false).toString());
    }
}