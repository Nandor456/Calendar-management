package edu.bbte.idde.mnim2377.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("jdbc")
@SuppressWarnings({"checkstyle:Indentation"})
@ImportAutoConfiguration(
        exclude = {HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class })
public class DisableJpaAutoConfigForNonJpaProfiles {
}
