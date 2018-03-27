package com.github.cstroe.metraschedule.parser;

import com.github.cstroe.metraschedule.Result;
import com.github.cstroe.metraschedule.domain.Time;

public class TimeParserResult extends Result<Time, TimeParserException> {
    public TimeParserResult(Time value) {
        super(value);
    }

    public TimeParserResult(TimeParserException error) {
        super(error);
    }
}
