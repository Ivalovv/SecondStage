package util;

import org.hibernate.SessionFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.hibernate.cfg.Configuration;


public class HibernateUtilTest {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(PostgreSQLContainer<?> postgres) {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate-test.cfg.xml");

            configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
            configuration.setProperty("hibernate.connection.username", postgres.getUsername());
            configuration.setProperty("hibernate.connection.password", postgres.getPassword());

            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
}
