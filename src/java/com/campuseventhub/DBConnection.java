package com.campuseventhub;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL_3306 = "jdbc:mysql://localhost:3306/campus_event_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String URL_3307 = "jdbc:mysql://localhost:3307/campus_event_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try {
            return DriverManager.getConnection(URL_3306, USER, PASSWORD);
        } catch (Exception e) {
            return DriverManager.getConnection(URL_3307, USER, PASSWORD);
        }
    }
}
