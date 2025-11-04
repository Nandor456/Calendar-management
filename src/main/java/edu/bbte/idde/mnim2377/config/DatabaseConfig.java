package edu.bbte.idde.mnim2377.config;

import edu.bbte.idde.mnim2377.data.exception.DatabaseException;

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

    public static String getHost() {
        return properties.getProperty("DB_HOST");
    }

    public static String getName() {
        return properties.getProperty("DB_NAME");
    }

    public static String getPassword() {
        return properties.getProperty("DB_PASSWORD");
    }

    public static String getPort() {
        return properties.getProperty("DB_PORT");
    }

    public static String getUrl() {
        // Format: jdbc:postgresql://<host>:<port>/<databaseName>
        return "jdbc:postgresql://"
                + getHost() + ":"
                + getPort() + "/"
                + getName();
    }

    public static String getDaoType() {
        return properties.getProperty("DAO_TYPE", "INMEMORY");
    }
}
