package edu.bbte.idde.mnim2377.backend.config;

import edu.bbte.idde.mnim2377.backend.data.exception.DatabaseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getResourceAsStream("/db.properties")) {
            if (input == null) {
                throw new DatabaseException("Unable to find db.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new DatabaseException("Failed to load database configuration", e);
        }
    }


    public static String getUser() {
        return properties.getProperty("DB_USER");
    }

    public static String getPassword() {
        return properties.getProperty("DB_PASSWORD");
    }

    public static String getUrl() {
        return properties.getProperty("DB_URL");
    }

    public static String getDriver() {
        return properties.getProperty("DB_DRIVER");
    }

    public static String getDaoType() {
        return properties.getProperty("DAO_TYPE", "INMEMORY");
    }
}
