package edu.bbte.idde.mnim2377.desktop;

import edu.bbte.idde.mnim2377.backend.data.exception.DataException;
import edu.bbte.idde.mnim2377.backend.database.DatabaseInitializer;

public class Main {
    public static void main(String[] args) throws DataException {
        String profile = System.getProperty("app.profile");
        if (profile == null || profile.isEmpty()) {
            profile = System.getenv("APP_PROFILE");
        }
        if (profile == null || profile.isEmpty()) {
            profile = "dev";
        }

        if ("prod".equalsIgnoreCase(profile)) {
            DatabaseInitializer.initializeDatabase();
        }
        new CalendarApp();
    }
}
