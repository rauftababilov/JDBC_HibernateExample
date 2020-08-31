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

        String SQL = "DROP TABLE IF EXISTS users";

        doTransaction(session, SQL);
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.openHibernateSession();

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.openHibernateSession();

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            User user = new User();
            user.setId(id);
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.openHibernateSession();

        String SQL = "SELECT * FROM users;";

        Query query = session.createNativeQuery(SQL).addEntity(User.class);

        List<User> users = query.list();
        session.close();

        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.openHibernateSession();

        String SQL = "DELETE FROM users;";

        doTransaction(session, SQL);
    }

}
