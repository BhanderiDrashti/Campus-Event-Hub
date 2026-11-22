package com.campuseventhub;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String[] URLS = {
        "jdbc:mysql://127.0.0.1:3306/campus_event_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
        "jdbc:mysql://localhost:3306/campus_event_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
        "jdbc:mysql://127.0.0.1:3307/campus_event_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
        "jdbc:mysql://localhost:3307/campus_event_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
    };
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Exception lastException = null;
        for (String url : URLS) {
            try {
                return DriverManager.getConnection(url, USER, PASSWORD);
            } catch (Exception e) {
                lastException = e;
            }
        }
        throw lastException != null ? lastException : new Exception("Unable to connect to database");
    }
}
