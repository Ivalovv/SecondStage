package dao.implementation;

import dao.UserDao;
import model.User;
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

            User existing = session.get(User.class, user.getId());

            if (existing == null) {
                logger.warn("Пользователь с id= {} не найден, обновление отменено",
                        user.getId());

                tx.rollback();
                return;
            }

            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            existing.setAge(user.getAge());

            tx.commit();
            logger.info("Пользователь обновлён: {}", existing);
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

            User existing = session.get(User.class, id);
            if (existing == null) {
                logger.warn("Пользователь с id= {} не найден, удаление отменено", id);

                tx.rollback();
                return;
            }
            session.remove(existing);

            tx.commit();

            logger.info("Пользователь удален: {}", existing);
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();

            logger.error("Ошибка при удаление пользователя", e);
        }
    }
}
