package com.example.agencija.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver {

    private static String DB_user = "root";
    private static String DB_password = "";
    private static String connectionUrl;
    private static int port = 3306;
    private static String DB_name = "agencija";
    private static Connection connection;

    public Driver() {
        startConnection();
    }

    public Connection getConn() {
        return connection;
    }

    public void startConnection() {
        try {
            connectionUrl = "jdbc:mysql://localhost" + ":" + port + "/" + DB_name;
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
            System.out.println("Connection est");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void endConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
