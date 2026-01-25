package edu.idde.mnim2377.data.dao;

import edu.idde.mnim2377.data.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

@Slf4j
public class LoggingConfig {
    private static Properties properties;
    private static Boolean enabled;
    private static String logLevel;

    public static Properties getInstance() {
        if (properties == null) {
            properties = new Properties();
            String FILE_NAME = "/loggerfile.properties";
            try (InputStream inputStream = LoggingConfig.class.getResourceAsStream(FILE_NAME)) {
                if (inputStream == null) {
                    // No resource found -> fall back to safe defaults
                    enabled = false;
                    logLevel = "INFO";
                    return properties;
                }
                properties.load(inputStream);
                enabled = Boolean.parseBoolean(properties.getProperty("dao.logging.enabled", "false"));
                logLevel = properties.getProperty("dao.logging.level", "INFO");
                return properties;
            } catch (IOException e) {
                throw new DataException("cant read loggerfile" + e);
            }
        }
        return properties;
    }

    public static Boolean isEnabled() {
        getInstance();
        return enabled;
    }

    public static String getLogLevel() {
        getInstance();
        return logLevel;
    }

    private static Level safeLevel() {
        String raw = getLogLevel();
        if (raw == null) {
            return Level.INFO;
        }
        try {
            return Level.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return Level.INFO;
        }
    }

    public static <T> void log(String s, T variable) {
        if (!isEnabled()) return;

        log.atLevel(safeLevel())
                .log(s, variable);
    }

    public static void log(String s) {
        if (!isEnabled()) return;

        log.atLevel(safeLevel())
                .log(s);
    }


    public static String getProperties(String key) {
        return getInstance().getProperty(key);
    }
}
