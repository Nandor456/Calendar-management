package edu.idde.mnim2377.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

public class DatabaseConfig {
    @Getter
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DatabaseProvider.getProperties("url"));
        config.setUsername(DatabaseProvider.getProperties("username"));
        config.setPassword(DatabaseProvider.getProperties("password"));
        config.setDriverClassName(DatabaseProvider.getProperties("driver-class-name"));
        config.setMaximumPoolSize(Integer.parseInt(DatabaseProvider.getProperties("maximum-pool-size")));
        dataSource = new HikariDataSource(config);
    }
}
