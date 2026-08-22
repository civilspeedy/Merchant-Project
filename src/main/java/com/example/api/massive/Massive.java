package com.example.api.massive;

import static com.example.util.Log.start;
import static com.example.util.Log.stop;

import com.example.api.massive.records.AggregateBars;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.NonNull;
import lombok.val;
import tools.jackson.databind.ObjectMapper;

/**
 * Class for managing API request to the Massive financial service, along with
 * managing data related to and from these requests. All methods are static.
 */
public final class Massive {

    private static final String URL_BASE = "https://api.massive.com/v2/%s/%s/range/%s/%s/%s?adjusted=true&sort=asc&limit=100&apiKey=%s";
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final String USER_AGENT = "Mozilla/5.0 (Java-HttpClient)";
    private static final String ACCEPT = "application/json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int MAX_REQUESTS = 5;
    private static final int OK = 200;
    private static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ISO_DATE;

    private static int requestCount = 0;
    private static LocalTime lastRequest = null;
    private static String key;

    // example:
    // https://api.massive.com/v2/aggs/ticker/AAPL/range/1/day/2025-11-20/2025-12-28?adjusted=true&sort=asc&limit=100&apiKey=

    private Massive() {
    }

    private static boolean checkString(String str) {
        return str.isBlank() || str.isEmpty();
    }

    public static AggregateBars getAggregate(
            @NonNull LocalDate start,
            @NonNull LocalDate end,
            @NonNull String code) throws Exception {
        start("get aggregate");
        if (start.compareTo(end) >= 0) {
            throw new IllegalArgumentException("end cannot be before start");
        }

        if (checkString(code)) {
            throw new IllegalArgumentException(
                    "code cannot be empty, blank or null");
        }
        if (checkString(key)) {
            throw new IllegalStateException("key is unassigned");
        }

        if (lastRequest != null) {
            val timeDifference = Duration.between(lastRequest, LocalTime.now());
            // I don't trust this to work as I expect.
            // A separate thread or ticker to reset count every minute would probably be
            // more reliable but could prove overkill.
            if (timeDifference.toMinutes() > 1) {
                requestCount = 0;
            }
        }

        if (requestCount > MAX_REQUESTS) {
            throw new IllegalStateException(
                    "no more than five request can be made per minute");
        }

        val url = String.format(URL_BASE, "aggs/ticker", code, "1/day", start.format(ISO_FORMAT),
                end.format(ISO_FORMAT), key);

        val request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .headers("user-agent", USER_AGENT)
                .header("accept", ACCEPT)
                .build();

        val response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        requestCount++;
        lastRequest = LocalTime.now();

        int status = response.statusCode();
        if (status != OK) {
            throw new IOException("http request failure: " + status);
        }
        stop("get aggregate");
        return mapper.readValue(response.body(), AggregateBars.class);
    }

}
