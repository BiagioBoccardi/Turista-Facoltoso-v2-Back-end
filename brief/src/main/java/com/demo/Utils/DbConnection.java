package com.demo.Utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    



public class DbConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/turista_facoltoso";
    private static final String USER = "postgres"; // username          
    private static final String PASSWORD = "1$i7Aal7"; // password 
;  // cambia con la tua password

    private DbConnection() {
        // costruttore privato per evitare istanziazioni
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


