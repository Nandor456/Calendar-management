package edu.bbte.idde.mnim2377;

import edu.bbte.idde.mnim2377.UI.CalendarApp;
import edu.bbte.idde.mnim2377.database.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        new CalendarApp();
    }
}
