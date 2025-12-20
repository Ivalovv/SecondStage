package util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Ошибка при создании SessionFactory: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void close() {
        getSessionFactory().close();
    }
}
