package edu.idde.mnim2377.data.dao;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class DaoFactory {
    private static final DaoFactory daoFactory = createInstance();

    private static DaoFactory createInstance() {
        String profile = System.getenv("PROFILE");
        if (Objects.equals(profile, "prod")) {
            LoggingConfig.log("working with prod profile");
            return new UserJdbcDaoFactory();
        }
        LoggingConfig.log("working with default profile");
        return new UserJdbcDaoFactory();
    }

    public abstract UserDao getUserDao();

    public static DaoFactory getInstance() {
        return daoFactory;
    }
}
