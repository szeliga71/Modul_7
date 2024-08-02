import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AuthorSessionsFactoryTest {

    public static SessionFactory getAuthorSessionFactoryTest(String jdbcUrl, String username, String password) {
        Configuration configuration = new Configuration();
        configuration.configure("/hibernate-test.cfg.xml");
        configuration.setProperty("hibernate.connection.url", jdbcUrl);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);
        return configuration.buildSessionFactory();
    }
}
