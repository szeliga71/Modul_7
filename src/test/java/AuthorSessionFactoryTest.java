import org.example.sessions.AuthorSessionFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthorSessionFactoryTest {

    @Test
    void getAuthorSessionNotNullTest() {
        SessionFactory sessionFactory=AuthorSessionFactory.getAuthorSessionFactory();
        Assertions.assertNotNull(sessionFactory);
        sessionFactory.close();
    }
    @Test
    void getAuthorSessionOpenSessionNotNullTest() {
        SessionFactory sessionFactory=AuthorSessionFactory.getAuthorSessionFactory();
        Assertions.assertNotNull(sessionFactory.openSession());
        sessionFactory.close();
    }
}
