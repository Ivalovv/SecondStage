package dao;

import dao.implementation.UserDaoImp;
import spring.model.User;
import org.hibernate.*;
import org.hibernate.query.MutationQuery;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoUnitTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Transaction transaction;
    @Mock
    private MutationQuery mutationQuery;

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImp(sessionFactory);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    void testCreateSuccess() {
        User user = new User("Ivan", "ivan@test.com", 25);

        userDao.create(user);

        verify(session).persist(user);
        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
    }

    @Test
    void testCreateThrowsException() {
        User user = new User("Ivan", "ivan@test.com", 25);

        doThrow(new RuntimeException("DB error"))
                .when(session)
                .persist(user);

        userDao.create(user);

        verify(transaction, never()).commit();
        verify(transaction).rollback();
        verify(session).close();
    }

    @Test
    void testReadUserFound() {
        User expectedUser = new User("Ivan", "ivan@example.com", 25);

        when(session.get(User.class, expectedUser.getId())).thenReturn(expectedUser);

        User result = userDao.read((int) (long)expectedUser.getId());

        assertNotNull(result);
        assertEquals(expectedUser, result);

        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
    }

    @Test
    void testReadUserNotFound() {
        int userId = 42;

        when(session.get(User.class, userId)).thenReturn(null);

        User result = userDao.read(userId);

        assertNull(result);

        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
    }

    @Test
    void testReadThrowsException() {
        int userId = 1;

        when(session.get(User.class, userId)).thenThrow(new RuntimeException("DB error"));

        User result = userDao.read(userId);

        assertNull(result);

        verify(transaction, never()).commit();
        verify(transaction).rollback();
        verify(session).close();
    }

    @Test
    void testUpdateSuccess() {
        User user = new User("Ivan", "ivan@example.com", 25);

        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(anyString(), any())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);

        userDao.update(user);

        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
    }

    @Test
    void testUpdateUserNotFound() {
        User user = new User("Ghost", "ghost@example.com", 50);

        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(anyString(), any())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(0);

        userDao.update(user);

        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
    }

    @Test
    void testUpdateThrowsException() {
        User user = new User("Error", "error@example.com", 40);

        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(anyString(), any())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenThrow(new RuntimeException("DB error"));

        userDao.update(user);

        verify(transaction, never()).commit();
        verify(transaction).rollback();
        verify(session).close();
    }

    @Test
    void testDeleteSuccess() {
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(anyString(), any())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);

        userDao.delete(1);

        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
    }

    @Test
    void testDeleteUserNotFound() {
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(anyString(), any())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(0);

        userDao.delete(999);

        verify(transaction).commit();
        verify(transaction, never()).rollback();
        verify(session).close();
    }

    @Test
    void testDeleteThrowsException() {
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(anyString(), any())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenThrow(new RuntimeException("DB error"));

        userDao.delete(1);

        verify(transaction).rollback();
        verify(transaction, never()).commit();
        verify(session).close();
    }

}
