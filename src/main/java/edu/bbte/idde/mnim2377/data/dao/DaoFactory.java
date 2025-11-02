package edu.bbte.idde.mnim2377.data.dao;

import edu.bbte.idde.mnim2377.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DaoFactory {
    private static DaoFactory instance;
    private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);

    public static DaoFactory getInstance() {
        if (instance == null) {
            String daoType = DatabaseConfig.getDaoType();
            if (daoType.equalsIgnoreCase("jdbc")) {
                logger.info("working with Postgres DAO");
                instance = new PostgresCalendarDaoFactory();
            } else {
                logger.info("working with In-Memory DAO");
                instance = new InMemoryCalendarDaoFactory();
            }
        }
        return instance;
    }

    public abstract CalendarDao getCalendarDao();

}
