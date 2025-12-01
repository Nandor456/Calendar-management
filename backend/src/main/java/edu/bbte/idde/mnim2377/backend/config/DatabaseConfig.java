package edu.bbte.idde.mnim2377.backend.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.mnim2377.backend.data.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class DatabaseConfig {
    private static JsonNode config;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    static {
        loadConfig();
    }

    private static void loadConfig() {
        // Determine active profile
        String activeProfile = determineActiveProfile();
        String configFile = "/application-" + activeProfile + ".json";

        logger.info("Loading configuration from: {}", configFile);
        try (InputStream input = DatabaseConfig.class.getResourceAsStream(configFile)) {
            if (input == null) {
                throw new DatabaseException("Unable to find " + configFile);
            }

            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readTree(input);

            logger.info("Configuration loaded successfully for profile: {}", activeProfile);
        } catch (IOException e) {
            throw new DatabaseException("Failed to load database configuration", e);
        }
    }

    private static String determineActiveProfile() {
        // Check JVM property first: -Dapp.profile=prod
        String profile = System.getProperty("app.profile");

        if (profile == null || profile.isEmpty()) {
            // Check environment variable: APP_PROFILE=prod
            profile = System.getenv("APP_PROFILE");
        }

        if (profile == null || profile.isEmpty()) {
            // Default profile
            profile = "dev";
            logger.warn("No profile specified, using default: {}", profile);
        }

        return profile;
    }

    public static String getUser() {
        return config.path("database").path("username").asText();
    }

    public static String getPassword() {
        return config.path("database").path("password").asText();
    }

    public static String getUrl() {
        logger.error("Accessing database URL: {}", config.path("database").path("url").asText());
        return config.path("database").path("url").asText();
    }

    public static String getDriver() {
        return config.path("database").path("driver").asText();
    }

    public static String getDaoType() {
        // Use a specific Locale to avoid locale-sensitive case issues (e.g. Turkish i)
        return config.path("dao").path("implementation").asText("memory").toUpperCase(Locale.ROOT);
    }

    public static int getMaxPoolSize() {
        return config.path("connectionPool").path("maxSize").asInt(10);
    }
}
