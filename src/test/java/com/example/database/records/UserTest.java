package com.example.database.records;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.Test;

public class UserTest {

    private static final Random rnd = new Random();
    private static final int MAX_USR_SIZE = 51;
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

    @Test
    public void testConstruct() {
        new User("normal username", "normal password");
    }

    @Test
    public void usernameTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new User(randomLargeString(), SAFE_STRING);
        });

        assertEquals("username length cannot exceed 50", ex.getMessage());
    }

    @Test
    public void usernameTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new User("", SAFE_STRING);
        });

        assertEquals("username cannot be empty or null", ex.getMessage());
    }

    @Test
    public void passwordTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new User(SAFE_STRING, randomLargeString());
        });

        assertEquals("password length cannot exceed 90", ex.getMessage());
    }

    @Test
    public void passwordTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new User(SAFE_STRING, "");
        });

        assertEquals("password cannot be empty or null", ex.getMessage());
    }

    @Test
    public void testGetFieldArray() {
        User testUser = new User(SAFE_STRING, SAFE_STRING);
        String[] expectedArray = new String[] { SAFE_STRING, SAFE_STRING };
        assertArrayEquals(expectedArray, testUser.getFieldArray());
    }
}
