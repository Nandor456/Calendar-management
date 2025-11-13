package edu.bbte.idde.mnim2377.desktop;

import edu.bbte.idde.mnim2377.backend.data.exception.DataException;
import edu.bbte.idde.mnim2377.backend.database.DatabaseInitializer;

public class Main {
    public static void main(String[] args) throws DataException {
        DatabaseInitializer.initializeDatabase();
        new CalendarApp();
    }
}
