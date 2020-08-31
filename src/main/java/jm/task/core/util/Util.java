package jm.task.core.util;


import jm.task.core.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Util {

    private static final String HOST = "jdbc:mysql://localhost:3306/test_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    public static Connection openConnection() {
        try {
            Connection connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
            System.out.println("Connection OK");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection ERROR");
            return null;
        }
    }

    private static SessionFactory sessionFactory;

    public static Session openHibernateSession() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory.openSession();
    }
}
