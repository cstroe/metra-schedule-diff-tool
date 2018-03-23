public class TimeParserResult extends Result<String, TimeParserException> {
    public TimeParserResult(String value) {
        super(value);
    }

    public TimeParserResult(TimeParserException error) {
        super(error);
    }
}
