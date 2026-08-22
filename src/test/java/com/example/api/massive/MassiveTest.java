package com.example.api.massive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.massive.records.AggregateBars;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

public class MassiveTest {

    private static final String KEY_PATH = "com/example/resources/massiveTestKey.txt";
    private static final String TEST_RESULTS_PATH = "com/example/resources/TestMassiveResults.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setup() throws IOException {
        var key = new String(
                MassiveTest.class
                        .getClassLoader()
                        .getResourceAsStream(KEY_PATH)
                        .readAllBytes());

        // assign api key
    }

    @Test
    public void testGetAggregate() throws Exception {
        var start = LocalDate.parse("2026-01-12");
        var end = LocalDate.parse("2026-02-01");
        var actualRecord = Massive.getAggregate(start, end, "AAPL");

        var expectedResponse = new String(
                MassiveTest.class
                        .getClassLoader()
                        .getResourceAsStream(TEST_RESULTS_PATH)
                        .readAllBytes());

        var expectedRecord = mapper.readValue(
                expectedResponse,
                AggregateBars.class);

        // ignore how inaccurate this is, maybe I'll write a proper comparison later
        var expected = expectedRecord.toString();
        var actual = actualRecord.toString();
        assertEquals(expected, actual);
    }
}
