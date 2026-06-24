package com.example;

import com.example.records.User;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    private static void storeUser(String username, String password) {
        var encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        var encodedPassword = encoder.encode(password);
        var user = new User(username, encodedPassword);
        // storage mechanisms
    }
}
