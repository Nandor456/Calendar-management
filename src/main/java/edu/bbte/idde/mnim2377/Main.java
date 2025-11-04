package edu.bbte.idde.mnim2377;

import edu.bbte.idde.mnim2377.data.exception.DataException;
import edu.bbte.idde.mnim2377.ui.CalendarApp;
import edu.bbte.idde.mnim2377.database.DatabaseInitializer;

public class Main {
    public static void main(String[] args) throws DataException {
        DatabaseInitializer.initializeDatabase();
        new CalendarApp();
    }
}
