package com.example.database.records;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.Test;

public class UserTest {

    private static final Random rnd = new Random();
    private static final int MAX_USR_SIZE = 50;
    private static final int UNICODE_MAX = 128;
    private static final String SAFE_STRING = "this is a safe string";

    private static String randomLargeString() {
        int size = rnd.nextInt(1000 - MAX_USR_SIZE) + MAX_USR_SIZE;

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < size; i++) {
            char rndChar = (char) rnd.nextInt(UNICODE_MAX + 0 - 1);
            stringBuilder.append(rndChar);
        }

        return stringBuilder.toString();
    }

    // these should probably check messages

    @Test
    public void testConstruct() {
        new User("normal username", "normal password");
    }

    @Test
    public void usernameTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(randomLargeString(), SAFE_STRING);
        });
    }
}
