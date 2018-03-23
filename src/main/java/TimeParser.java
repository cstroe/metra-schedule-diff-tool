import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeParser {
    private static final Pattern simplePattern = Pattern.compile("^(1?[0-2]|[1-9])([0-9][0-9])$");
    private static final Pattern pmPattern = Pattern.compile("^(1?[0-2]|[1-9])([0-9][0-9])p$");

    private static final Pattern minutePattern = Pattern.compile("^([1-5]?[0-9]|[0-9]$)");

    private Time match(String input) {
        Matcher matcher = simplePattern.matcher(input);
        if(matcher.find()) {
            return Time.of(matcher.group(1), matcher.group(2), "am");
        } else {
            return null;
        }
    }

    private Time matchPm(String input) {
        Matcher matcher = pmPattern.matcher(input);
        if(matcher.find()) {
            return Time.of(matcher.group(1), matcher.group(2), "pm");
        } else {
            return null;
        }
    }

    public TimeParserResult parse(String time) {
        Time match = match(time);
        if(match != null) {
            return new TimeParserResult(match);
        }

        Time match2 = matchPm(time);
        if(match2 != null) {
            return new TimeParserResult(match2);
        }

        return new TimeParserResult(new TimeParserException("No match."));
    }

    public TimeParserResult parse(Time previousTime, String currentInput) {
        TimeParserResult normalMatch = parse(currentInput);
        if(normalMatch.hasValue()) {
            return normalMatch;
        }


        Matcher minute = minutePattern.matcher(currentInput);
        if(minute.matches()) {
            Time time = Time.of(previousTime.hour, Integer.parseInt(minute.group(1)), previousTime.am);
            return new TimeParserResult(time);
        }
        return new TimeParserResult(new TimeParserException("No match."));
    }
}
