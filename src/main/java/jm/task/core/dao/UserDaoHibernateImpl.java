package jm.task.core.dao;

import jm.task.core.model.User;
import jm.task.core.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.transaction.Transactional;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    // метод для выполнения транзакции, во избежание дублирования кода
    private void doTransaction(Session session, String SQL) {
        Transaction transaction = null;

        try {
            Query query = session.createNativeQuery(SQL);
            transaction = session.beginTransaction();
            query.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        session.close();
    }

    @Override
    public void createUsersTable() {
        Session session = Util.openHibernateSession();

        String SQL = "CREATE TABLE IF NOT EXISTS users (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `name` VARCHAR(45) NOT NULL," +
                "  `last_name` VARCHAR(45) NOT NULL," +
                "  `age` INT NOT NULL," +
                "  PRIMARY KEY (`id`));";

        doTransaction(session, SQL);
    }

    @Override
    public void dropUsersTable() {
        Session session = Util.openHibernateSession();
        final Transaction transaction = session.getTransaction();
        transaction.begin();

        try {
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.openHibernateSession();
        final Transaction transaction = session.getTransaction();
        transaction.begin();

        try {
            session.persist(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.openHibernateSession();
        final Transaction transaction = session.getTransaction();
        transaction.begin();

        try {
            User user = session.load(User.class, id);
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.openHibernateSession();
        final Transaction transaction = session.getTransaction();
        transaction.begin();

        List<User> users = session.createQuery("FROM User").list();

        session.close();

        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.openHibernateSession();
        final Transaction transaction = session.getTransaction();
        transaction.begin();

        try {
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }
}
