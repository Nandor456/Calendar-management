package edu.bbte.idde.mnim2377.backend.data.dao;

import edu.bbte.idde.mnim2377.backend.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DaoFactory {
    private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);
    private static final DaoFactory instance = createInstance();

    private static DaoFactory createInstance() {
        String daoType = DatabaseConfig.getDaoType();
        if ("jdbc".equalsIgnoreCase(daoType)) {
            logger.info("working with Postgres DAO");
            return new JdbcDaoFactory();
        } else {
            logger.info("working with In-Memory DAO");
            return new InMemoryDaoFactory();
        }
    }


    public static DaoFactory getInstance() {
        return instance;
    }

    public abstract CalendarDao getCalendarDao();

}
