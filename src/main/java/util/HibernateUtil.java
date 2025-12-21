package util;

import dao.implementation.UserDaoImp;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            SessionFactory factory = new Configuration().configure().buildSessionFactory();

            logger.info("Hibernate SessionFactory успешно инициализирована");
            return factory;
        } catch (Exception e) {
            logger.error("Ошибка при инициализации SessionFactory", e);
            throw new ExceptionInInitializerError(e);
        }
    }
}
