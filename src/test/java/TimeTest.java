import com.github.cstroe.metraschedule.domain.Time;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {
    @Test
    public void toStringTest() {
        assertEquals("1:20 AM", Time.of("1","20", "AM").toString());
        assertEquals("11:20 AM", Time.of("11","20", "AM").toString());
        assertEquals("11:02 AM", Time.of("11","2", "AM").toString());
        assertEquals("11:02 PM", Time.of("11","2", "PM").toString());
        assertEquals("11:02 AM", Time.of(11,2,true).toString());
        assertEquals("11:02 PM", Time.of(11,2,false).toString());
    }
}