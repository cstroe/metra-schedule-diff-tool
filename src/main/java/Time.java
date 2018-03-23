import static java.lang.String.format;

public class Time {
    public final int hour;
    public final int minute;
    public final boolean am;

    public Time(String hour, String minute, String ampm) {
        this.hour = Integer.parseInt(hour);
        this.minute = Integer.parseInt(minute);
        am = "am".equals(ampm.toLowerCase());
    }

    public Time(int hour, int minute, boolean am) {
        this.hour = hour;
        this.minute = minute;
        this.am = am;
    }

    @Override
    public String toString() {
        if(am) {
            return format("%d:%02d AM", hour, minute);
        } else {
            return format("%d:%02d PM", hour, minute);
        }
    }
}
