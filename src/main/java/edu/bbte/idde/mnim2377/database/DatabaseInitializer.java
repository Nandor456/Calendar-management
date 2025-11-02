package edu.bbte.idde.mnim2377.database;

import edu.bbte.idde.mnim2377.config.DataSourceProvider;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        String sql =
                "CREATE TABLE IF NOT EXISTS calendar ("
                + "  id VARCHAR(64) PRIMARY KEY,"
                + "  address TEXT NOT NULL,"
                + "  location TEXT NOT NULL,"
                + "  date DATE NOT NULL,"
                + "  is_online BOOLEAN NOT NULL"
                + ");";
        try(Connection conn = DataSourceProvider.getDataSource().getConnection()) {
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
