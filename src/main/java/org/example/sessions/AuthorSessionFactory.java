package org.example.sessions;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AuthorSessionFactory {

    public static SessionFactory getAuthorSessionFactory() {
        Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.configure("/hibernate.cfg.xml");
        return configuration.buildSessionFactory();
    }

    public static SessionFactory getAuthorSessionFactoryTest(String jdbcUrl, String username, String password) {
        Configuration configuration = new Configuration();
        configuration.configure("/hibernate-test.cfg.xml");
        configuration.setProperty("hibernate.connection.url", jdbcUrl);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);
        return configuration.buildSessionFactory();
    }
}
