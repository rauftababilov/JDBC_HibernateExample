package jm.task.core;

import jm.task.core.service.UserService;
import jm.task.core.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Bob", "Marley", (byte) 35);

        userService.saveUser("Mike", "Tyson", (byte) 48);

        userService.saveUser("John", "Lennon", (byte) 40);

        userService.saveUser("Viktor", "Tsoy", (byte) 28);

        userService.getAllUsers().forEach(user -> System.out.println(user.toString()));

        userService.cleanUsersTable();

        userService.dropUsersTable();

    }
}
