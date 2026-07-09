package com.example.api.massive.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AggregateBars(
    String ticker,
    int queryCount,
    int resultsCount,
    boolean adjusted,
    Result[] results
) {
    private record Result(
        @JsonProperty("v") double tradingVolume,
        @JsonProperty("vw") double volumeWeightedAveragePrice,
        @JsonProperty("o") double openPrice,
        @JsonProperty("c") double closePrice,
        @JsonProperty("h") double highestPrice,
        @JsonProperty("l") double lowestPrice,
        @JsonProperty("t") int timestamp,
        @JsonProperty("n") int transactions
    ) {}
}
