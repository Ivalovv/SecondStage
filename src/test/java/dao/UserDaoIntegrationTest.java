package dao;

import dao.implementation.UserDaoImp;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;
import util.HibernateUtilTest;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("test_db")
                    .withUsername("test")
                    .withPassword("test");

    private static SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    static void initSessionFactory() {
        sessionFactory = HibernateUtilTest.getSessionFactory(postgres);
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImp(sessionFactory);

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from User").executeUpdate();
            tx.commit();
        }
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @Test
    void testCreateUser() {
        User user = new User("Ivan", "ivan@example.com", 25);
        userDao.create(user);

        assertTrue(user.getId() > 0, "ID должен быть сгенерирован");

        User readUser = userDao.read(user.getId());

        assertNotNull(readUser, "Пользователь должен быть найден");
        assertEquals("Ivan", readUser.getName());
        assertEquals("ivan@example.com", readUser.getEmail());
        assertEquals(25, readUser.getAge());
    }

    @Test
    void testCreateUserWithNullName() {
        User user = new User(null, "ivan@example.com", 25);

        assertThrows(Exception.class, () -> userDao.create(user));
    }

    @Test
    void testCreateUserWithNullEmail() {
        User user = new User("Ivan", null, 25);

        assertThrows(Exception.class, () -> userDao.create(user));
    }

    @Test
    void testCreateDuplicateEmail() {
        User user1 = new User("Ivan", "ivan@example.com", 25);
        userDao.create(user1);

        assertTrue(user1.getId() > 0);

        User user2 = new User("Ivan2", "ivan@example.com", 30);

        assertThrows(Exception.class, () -> userDao.create(user2));
    }

    @Test
    void testReadExistingUser() {
        User user = new User("Ivan", "ivan@example.com", 25);
        userDao.create(user);

        User readUser = userDao.read(user.getId());

        assertNotNull(readUser, "Пользователь должен быть найден");
        assertEquals(user.getId(), readUser.getId());
        assertEquals("Ivan", readUser.getName());
        assertEquals("ivan@example.com", readUser.getEmail());
        assertEquals(25, readUser.getAge());
    }

    @Test
    void testReadNonExistingUser() {
        int nonExistentId = 9999;

        User readUser = userDao.read(nonExistentId);

        assertNull(readUser, "Метод должен вернуть null для несуществующего пользователя");
    }

    @Test
    void testUpdateExistingUser() {
        User user = new User("Ivan", "ivan@example.com", 25);
        userDao.create(user);

        user.setName("Ivan Updated");
        user.setEmail("updated@example.com");
        user.setAge(30);

        userDao.update(user);

        User updatedUser = userDao.read(user.getId());
        assertNotNull(updatedUser);
        assertEquals("Ivan Updated", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(30, updatedUser.getAge());
    }

    @Test
    void testUpdateNonExistingUser() {
        User nonExistentUser = new User("Ghost", "ghost@example.com", 50);

        userDao.update(nonExistentUser);

        User fromDb = userDao.read(nonExistentUser.getId());
        assertNull(fromDb, "Пользователь не должен существовать в БД");
    }

    @Test
    void testUpdateWithNullFields() {
        User user = new User("Ivan", "ivan@example.com", 25);
        userDao.create(user);

        user.setName(null);
        user.setEmail(null);

        assertThrows(Exception.class,() -> userDao.update(user));
    }

    @Test
    void testDeleteExistingUser() {
        User user = new User("Ivan", "ivan@example.com", 25);
        userDao.create(user);

        userDao.delete(user.getId());

        User deletedUser = userDao.read(user.getId());
        assertNull(deletedUser);
    }

    @Test
    void testDeleteNonExistingUser() {
        int fakeId = 9999;

        assertDoesNotThrow(() -> userDao.delete(fakeId));

        User readUser = userDao.read(fakeId);
        assertNull(readUser);
    }

}

