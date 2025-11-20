package com.example.companycompass.config.db;

import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Configuration class for persistence layer components in the application. This class is annotated
 * with Spring's configuration-related annotations for enabling features such as transaction management
 * and JPA repositories. The class also loads database-specific properties and defines several beans
 * related to database connectivity, JPA, and transaction management.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.example.companycompass.repository")
@PropertySource("classpath:db-${env:local}.properties")
@ComponentScan("com.example.companycompass.service")
public class PersistenceConfig {
    /**
     * The name of the database driver class to be used for establishing a database connection.
     * Populated from the external property "db.driver" defined in the application properties file.
     * This property is mandatory for setting up the JPA DataSource.
     */
    @Value("${db.driver}")
    private String driver;

    /**
     * The URL of the database to be used for establishing a connection. Retrieved from the external
     * property "db.url" defined in the application properties file. This property is essential for
     * configuring the DataSource to identify the database location.
     */
    @Value("${db.url}")
    private String url;

    /**
     * The username used to authenticate the connection to the database. This value is injected from
     * the external property "db.username" defined in the application properties file. It is a
     * required property for establishing database connections.
     */
    @Value("${db.username}")
    private String username;

    /**
     * The password used to authenticate the connection to the database. This value is injected from
     * the external property "db.password" defined in the application properties file. It is a
     * required property for establishing database connections.
     */
    @Value("${db.password}")
    private String password;

    /**
     * Configures and initializes a Flyway instance responsible for managing
     * database versioning and migrations. The method sets up the data source,
     * specifies the locations of migration scripts, and executes any pending migrations.
     *
     * @return a fully configured Flyway instance with migrations applied
     */
    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource())
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        return flyway;
    }

    /**
     * Configures and provides a DataSource bean for interacting with the database.
     * This method sets up the data source with the specified driver, URL, username,
     * and password, enabling database connectivity for the application.
     *
     * @return a fully configured DataSource instance
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    /**
     * Configures and provides a LocalContainerEntityManagerFactoryBean for managing JPA persistence.
     * This method sets up the EntityManagerFactory with the provided DataSource and Flyway instances,
     * configures the scanning of entity packages, and applies JPA-specific properties.
     *
     * @return a fully configured LocalContainerEntityManagerFactoryBean for JPA persistence
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Flyway flyway) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.example.companycompass.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties properties = new Properties();
        properties.put("hibernate.transaction.jta.platform",
                "org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        em.setJpaProperties(properties);
        return em;
    }

    /**
     * Configures and provides a JpaTransactionManager bean to enable declarative transaction
     * management in the application. The transaction manager is responsible for coordinating
     * transactions with the underlying database through JPA.
     *
     * @param emf the EntityManagerFactory to be managed for transactional support
     * @return a configured instance of JpaTransactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
