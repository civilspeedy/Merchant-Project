package com.example.api.massive;

import com.example.api.massive.records.AggregateBars;
import java.time.LocalDate;

public class Massive {

    private static final String URL_BASE = "https://api.massive.com/v2/";
    private static final byte MAX_REQUESTS = 5;
    private static byte requestCount = 0;

    String expected =
        "https://api.massive.com/v2/aggs/ticker/AAPL/range/1/day/2025-11-20/2025-12-28?adjusted=true&sort=asc&limit=100&apiKey=CRTS3dz7N0_CoQjby2p2csSDfa1lZsUl";

    private static String localToString(LocalDate date) {
        return new StringBuilder()
            .append(date.getYear())
            .append('-')
            .append(date.getMonth())
            .append('-')
            .append(date.getDayOfMonth())
            .toString();
    }

    public static AggregateBars getAggregate(
        LocalDate start,
        LocalDate end,
        String code
    ) {
        String startString = localToString(start);
        String endString = localToString(end);
        String apiKey = "";
        String url = new StringBuilder(URL_BASE)
            .append("aggs/ticker/")
            .append(code)
            .append("range/1/day/")
            .append(startString)
            .append('/')
            .append(endString)
            .append("?adjusted=true&sort=asc&limit=120&apiKey=")
            .append(apiKey)
            .toString();
    }
}
