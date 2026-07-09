package com.example.api.massive;

import com.example.api.massive.records.AggregateBars;
import java.time.LocalDate;

public class Massive {

    private static final String URL_BASE = "https://api.massive.com/v2/";

    public static final AggregateBars getAggregate(
        LocalDate start,
        LocalDate end,
        String code
    ) {}
}
