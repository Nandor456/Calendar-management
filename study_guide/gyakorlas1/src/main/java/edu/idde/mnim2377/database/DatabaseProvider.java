package edu.idde.mnim2377.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseProvider {
    private static Properties properties;
    private static final Logger log = LoggerFactory.getLogger(DatabaseProvider.class);

    public static Properties getInstance() {
        if (properties == null) {
            properties = new Properties();
            String profile = System.getenv("PROFILE");
            String FILE_NAME = "/application-" + profile + ".properties";
            try (InputStream inputStream = DatabaseProvider.class.getResourceAsStream(FILE_NAME)) {
                if (inputStream == null) {
                    log.warn("profile doesnt exist, going with default");
                    return loadDefault();
                }
                properties.load(inputStream);
                log.info("Loaded config: {}", FILE_NAME);
            } catch (IOException e) {
                log.info("Error handling exception", e);
            }
        }
        return properties;
    }

    public static String getProperties(String key) {
        return getInstance().getProperty(key);
    }

    private static Properties loadDefault() {
        try(InputStream inputStream = DatabaseProvider.class.getResourceAsStream("/application-prod.properties")){
            if (inputStream == null) {
                throw new RuntimeException("application.properties not found");
            }
            properties.load(inputStream);
            log.info("Loaded default application.properties");
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
