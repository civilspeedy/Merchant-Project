package com.example.api.massive;

import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MassiveTest {

    private static final String KEY_PATH =
        "com/example/resources/massiveTestKey.txt";

    @BeforeAll
    public static void setup() throws IOException {
        var key = new String(
            MassiveTest.class
                .getClassLoader()
                .getResourceAsStream(KEY_PATH)
                .readAllBytes()
        );

        Massive.setKey(key.trim());
    }

    @Test
    public void testGetAggregate() throws Exception {
        var start = LocalDate.parse("2026-01-12");
        var end = LocalDate.parse("2026-02-01");
        var ags = Massive.getAggregate(start, end, "AAPL");
        System.out.println(ags.toString());
    }
}
