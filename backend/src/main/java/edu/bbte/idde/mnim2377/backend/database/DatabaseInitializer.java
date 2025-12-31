package edu.bbte.idde.mnim2377.backend.database;

import edu.bbte.idde.mnim2377.backend.config.DataSourceProvider;
import edu.bbte.idde.mnim2377.backend.data.exception.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initializeDatabase() throws DatabaseException {
        String sql =
                "CREATE TABLE IF NOT EXISTS calendar ("
                + "  id UUID PRIMARY KEY,"
                + "  address TEXT NOT NULL,"
                + "  location TEXT NOT NULL,"
                + "  date DATE NOT NULL,"
                + "  is_online BOOLEAN NOT NULL"
                + ");";
        try (Connection conn = DataSourceProvider.getDataSource().getConnection()) {
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to initialize database", e);
        }
    }
}
