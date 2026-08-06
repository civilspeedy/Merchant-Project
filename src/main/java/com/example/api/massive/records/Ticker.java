package com.example.api.massive.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Ticker(
    @JsonProperty("request_id") String id,
    Results results,
    String status
) {
    private record Results(
        String ticker,
        String name,
        Market market,
        Locale locale,
        @JsonProperty("primary_exchange") String primaryExchange,
        String type,
        boolean active,
        @JsonProperty("currency_name") String currency,
        @JsonProperty("market_cap") int marketCap,
        Address address,
        String description,
        @JsonProperty("sic_code") String sicCode,
        @JsonProperty("sic_description") String sicDescription,
        @JsonProperty("homepage_url") String url,
        @JsonProperty("total_employees") int totalEmployees,
        @JsonProperty("list_date") LocalDate listDate
    ) {
        private static enum Market {
            CRYPTO,
            FX,
            INDICES,
            OTC,
            STOCKS,
        }

        private static enum Locale {
            US,
            GLOBAL,
        }

        private record Address(
            @JsonProperty("address1") String addressLineOne,
            @JsonProperty("address2") String addressLineTwo,
            String city,
            String state,
            String postalCode
        ) {}
    }
}
