package com.demo.Repository.ddl;

import java.sql.Connection;
import java.sql.SQLException;

import com.demo.Utils.DbConnection;

public final class SchemaInitializer {

    private SchemaInitializer() {
    }

    public static void init() {
        try (Connection connection = DbConnection.getConnection()) {
            UtenteDDL.createTable(connection);
            HostDDL.createTable(connection);
            AbitazioneDDL.createTable(connection);
            PrenotazioneDDL.createTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
