package dao.implementation;

import dao.UserDao;
import spring.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImp implements UserDao {

    private final Logger logger = LoggerFactory.getLogger(UserDaoImp.class);
    private final SessionFactory sessionFactory;

    public UserDaoImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(User user) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            session.persist(user);

            tx.commit();
            logger.info("Пользователь сохранён: {}", user);
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();

            logger.error("Ошибка при сохранении пользователя", e);
        }
    }

    @Override
    public User read(int id) {
        Transaction tx = null;
        User user = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            user = session.get(User.class, id);

            tx.commit();

            if (user == null) {
                logger.warn("Пользователь с id={} не найден", id);
            } else {
                logger.info("Пользователь найден: {}", user);
            }

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();

            logger.error("Ошибка при чтении пользователя", e);
        }
        return user;
    }

    @Override
    public void update(User user) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            int updated = session.createMutationQuery(
                            "update User u set " +
                                    "u.name = :name, " +
                                    "u.email = :email, " +
                                    "u.age = :age " +
                                    "where u.id = :id"
                    )
                    .setParameter("name", user.getName())
                    .setParameter("email", user.getEmail())
                    .setParameter("age", user.getAge())
                    .setParameter("id", user.getId())
                    .executeUpdate();

            tx.commit();

            if (updated == 0) {
                logger.warn("Пользователь с id={} не найден, обновление не выполнено", user.getId());
            } else {
                logger.info("Пользователь обновлён: {}", user);
            }
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();

            logger.error("Ошибка при обновление пользователя", e);
        }
    }

    @Override
    public void delete(int id) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            int deleted = session.createMutationQuery(
                            "delete from User u where u.id = :id"
                    )
                    .setParameter("id", id)
                    .executeUpdate();

            tx.commit();

            if (deleted == 0) {
                logger.warn("Пользователь с id={} не найден, удаление не выполнено", id);
            } else {
                logger.info("Пользователь с id={} удалён", id);
            }
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();

            logger.error("Ошибка при удаление пользователя", e);
        }
    }
}
