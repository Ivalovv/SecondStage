import ConsoleApplication.Application;
import dao.UserDao;
import dao.implementation.UserDaoImp;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        UserDao userDao = new UserDaoImp(sessionFactory);
        new Application(userDao).run();
        sessionFactory.close();
    }
}