import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeParser {
    private static final Pattern simplePattern = Pattern.compile("^([0-9]?[0-9])([0-9][0-9])$");
    private static final Pattern pmPattern = Pattern.compile("^([0-9]?[0-9])([0-9][0-9])p$");

    private String match(String input) {
        Matcher matcher = simplePattern.matcher(input);
        if(matcher.find()) {
            return String.format("%s:%s AM", matcher.group(1), matcher.group(2));
        } else {
            return null;
        }
    }

    private String matchPm(String input) {
        Matcher matcher = pmPattern.matcher(input);
        if(matcher.find()) {
            return String.format("%s:%s PM", matcher.group(1), matcher.group(2));
        } else {
            return null;
        }
    }

    public TimeParserResult parse(String time) {
        String match = match(time);
        if(match != null) {
            return new TimeParserResult(match);
        }

        String match2 = matchPm(time);
        if(match2 != null) {
            return new TimeParserResult(match2);
        }

        return new TimeParserResult(new TimeParserException("No match."));
    }
}
