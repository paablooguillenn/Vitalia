package com.vitalia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

/**
 * Basic application context test
 * Note: Database auto-configuration is disabled for this skeleton project
 * Enable it once you have a database set up
 */
@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
class VitaliaApplicationTests {

    @Test
    void contextLoads() {
        // TODO: Implement tests once database and entities are configured
    }

}
