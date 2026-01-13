package edu.bbte.idde.mnim2377.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("in-memory")
@SuppressWarnings({"checkstyle:Indentation"})
@ImportAutoConfiguration(exclude = { 
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class })
public class DisableDataSourceAutoConfigForInMemoryProfile {
}

