package edu.bbte.idde.mnim2377;

import edu.bbte.idde.mnim2377.repository.JpaCalendarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("jpa")
class JpaWiringTest {

    @Autowired(required = false)
    private JpaCalendarRepository jpaCalendarRepository;

    @Test
    void contextLoads_andJpaRepositoryBeanIsPresent() {
        assertNotNull(jpaCalendarRepository, "JpaCalendarRepository should be created when profile 'jpa' is active");
    }
}

