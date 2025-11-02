package edu.bbte.idde.mnim2377.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceProvider {
    private static HikariDataSource dataSource;
    private DataSourceProvider() {
    }

    private static HikariDataSource configureDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DatabaseConfig.getUrl());
        config.setUsername(DatabaseConfig.getUser());
        config.setPassword(DatabaseConfig.getPassword());
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        if (dataSource == null) {
            dataSource = configureDataSource();
        }
        return dataSource;
    }

    public static void close() {
        if (!dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
