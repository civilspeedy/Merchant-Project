package com.example.api.massive.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AggregateBars(
    String ticker,
    int queryCount,
    int resultsCount,
    boolean adjusted,
    Result[] results
) {
    public record Result(
        @JsonProperty("v") double tradingVolume,
        @JsonProperty("vw") double volumeWeightedAveragePrice,
        @JsonProperty("o") double openPrice,
        @JsonProperty("c") double closePrice,
        @JsonProperty("h") double highestPrice,
        @JsonProperty("l") double lowestPrice,
        @JsonProperty("t") long timestamp,
        @JsonProperty("n") int transactions
    ) {}

    @Override
    public String toString() {
        return String.format(
            "AggregateBars[ticker=%s, queryCount=%d, resultsCount=%d, adjusted=%b, results=%s]",
            ticker,
            queryCount,
            resultsCount,
            adjusted,
            Arrays.toString(results)
        );
    }
}
