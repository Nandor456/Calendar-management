package edu.bbte.idde.mnim2377.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class DataSourceProvider {

    private DataSourceProvider() {
    }

    // Builds the HikariDataSource from configuration. Called once by Holder class for lazy, thread-safe init.
    private static HikariDataSource configureDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DatabaseConfig.getUrl());
        config.setUsername(DatabaseConfig.getUser());
        config.setPassword(DatabaseConfig.getPassword());
        config.setDriverClassName(DatabaseConfig.getDriver());
        config.setMaximumPoolSize(DatabaseConfig.getMaxPoolSize());
        return new HikariDataSource(config);
    }

    // Initialization-on-demand holder idiom ensures thread-safe lazy instantiation without synchronization.
    private static final class Holder {
        static final HikariDataSource INSTANCE = configureDataSource();
    }

    public static HikariDataSource getDataSource() {
        return Holder.INSTANCE;
    }
}
