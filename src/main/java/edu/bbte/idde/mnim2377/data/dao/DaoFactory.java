package edu.bbte.idde.mnim2377.data.dao;

import edu.bbte.idde.mnim2377.config.DatabaseConfig;

public abstract class DaoFactory {
    private static DaoFactory instance;

    public static DaoFactory getInstance() {
        if (instance == null) {
            String daoType = DatabaseConfig.getDaoType();
            System.out.println("DAO type from config: " + daoType);
            if (daoType.equalsIgnoreCase("jdbc")) {
                System.out.println("Using PostgresCalendarDaoFactory");
                instance = new PostgresCalendarDaoFactory();
            } else {
                System.out.println("Using InMemoryCalendarDaoFactory");
                instance = new InMemoryCalendarDaoFactory();
            }
        }
        return instance;
    }

    public abstract CalendarDao getCalendarDao();

}
