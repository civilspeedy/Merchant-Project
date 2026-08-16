package com.example.api.massive;

import static com.example.util.Log.out;

import com.example.api.massive.records.AggregateBars;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.NonNull;
import lombok.val;
import tools.jackson.databind.ObjectMapper;

public class Massive {

    private static final String URL_BASE = "https://api.massive.com/v2/";
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final String USER_AGENT = "Mozilla/5.0 (Java-HttpClient)";
    private static final String ACCEPT = "application/json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final byte MAX_REQUESTS = 5;
    private static byte requestCount = 0;
    private static LocalTime lastRequest = null;
    private static String key;

    String expected =
        "https://api.massive.com/v2/aggs/ticker/AAPL/range/1/day/2025-11-20/2025-12-28?adjusted=true&sort=asc&limit=100&apiKey=";

    // can't believe I committed with the key still there...

    private static String smallNumFmt(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    private static String localToString(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return new StringBuilder()
            .append(date.getYear())
            .append('-')
            .append(smallNumFmt(month))
            .append('-')
            .append(smallNumFmt(day))
            .toString();
    }

    private static boolean stringCheck(@NonNull String str) {
        return str.isBlank() || str.isEmpty();
    }

    public static AggregateBars getAggregate(
        LocalDate start,
        LocalDate end,
        String code
    ) throws Exception {
        out("fetching aggregate bar values from massive");
        if (start.compareTo(end) >= 0) {
            throw new IllegalArgumentException("end cannot be before start");
        }
        if (stringCheck(code)) {
            throw new IllegalArgumentException(
                "code cannot be empty, blank or null"
            );
        }
        if (stringCheck(key)) {
            throw new IllegalStateException("key is unassigned");
        }

        if (lastRequest != null) {
            val timeDifference = Duration.between(lastRequest, LocalTime.now());
            // I don't trust this to work as I expect.
            // A separate thread or ticker to reset count every minute would probably be more reliable but could prove overkill.
            if (timeDifference.toMinutes() > 1) {
                requestCount = 0;
            }
        }

        if (requestCount > MAX_REQUESTS) {
            throw new IllegalStateException(
                "no more than five request can be made per minute"
            );
        }

        val startString = localToString(start);
        val endString = localToString(end);
        val url = new StringBuilder(URL_BASE)
            .append("aggs/ticker/")
            .append(code)
            .append("/range/1/day/")
            .append(startString)
            .append('/')
            .append(endString)
            .append("?adjusted=true&sort=asc&limit=120&apiKey=")
            .append(key)
            .toString();

        val request = HttpRequest.newBuilder()
            .uri(new URI(url))
            .GET()
            .headers("user-agent", USER_AGENT)
            .header("accept", ACCEPT)
            .build();

        val response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        requestCount++;
        lastRequest = LocalTime.now();

        int status = response.statusCode();
        if (status != 200) {
            throw new IOException("http request failure: " + status);
        }
        return mapper.readValue(response.body(), AggregateBars.class);
    }

    public static void setKey(@NonNull String k)
        throws IllegalArgumentException {
        if (k.isBlank() || k.isEmpty()) {
            throw new IllegalArgumentException(
                "massive api key cannot be empty or blank"
            );
        }

        key = k;
    }
}
