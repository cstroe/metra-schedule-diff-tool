public class TimeParserResult extends Result<Time, TimeParserException> {
    public TimeParserResult(Time value) {
        super(value);
    }

    public TimeParserResult(TimeParserException error) {
        super(error);
    }
}
