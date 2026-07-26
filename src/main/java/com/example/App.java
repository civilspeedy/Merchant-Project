package com.example;

import com.example.ui.MainWindow;

public class App {

    private static String dbUrl =
        "jdbc:h2:file:./data/productionDatabase;CIPHER=AES";

    public static void main(String[] args) {
        MainWindow.start();
    }
}
