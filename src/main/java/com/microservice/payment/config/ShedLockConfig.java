package com.microservice.payment.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Shed lock library is used where same instances of
 * microservices are more than one and required to
 * execute program in scheduled based. To avoid all
 * other instances to process the same data shedlock
 * helps out to lock all other instances to process
 * those records to avoid duplication process
 *
 * @author Asif Bakht
 * @since 2024
 */
@Configuration
public class ShedLockConfig {


    /**
     * lock provider configuration to configure with
     * existing spring datasource configuration
     *
     * @param dataSource {@link DataSource} spring datasource
     * @return {@link LockProvider}
     */
    @Bean
    public LockProvider lockProvider(final DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        .usingDbTime() // Works on Postgres, MySQL, MariaDb, MS SQL, Oracle, DB2, HSQL and H2
                        .build()
        );
    }
}
