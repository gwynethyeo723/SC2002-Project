package controller;

import java.util.List;

import entity.User;

public class UserController {
    // public static boolean login(User user, String inputUserId, String password) {
    //     if (!user.getUserId().equals(inputUserId)) {
    //         System.out.println("User ID does not exist.");
    //         return false;
    //     }

    //     if (!user.getPassword().equals(password)) {
    //         System.out.println("Incorrect password.");
    //         return false;
    //     }

    //     user.setLoggedIn(true); // login successful 
    //     System.out.println("Login successful for " + user.getName());
    //     return true;
    // }
    public static User login(List<User> users, String inputUserId, String password) {
        // Search for the user by ID
        User user = users.stream()
                         .filter(u -> u.getUserId().equals(inputUserId))
                         .findFirst()
                         .orElse(null);

        if (user == null) {
            System.out.println("User ID does not exist.");
            return null;
        }

        // Check password
        if (!user.getPassword().equals(password)) {
            System.out.println("Incorrect password.");
            return null;
        }

        // Successful login
        user.setLoggedIn(true);
        System.out.println("Login successful for " + user.getName());
        return user;
    }

    public static void logout(User user) {
        if (user.isLoggedIn()) {
            user.setLoggedIn(false);
            System.out.println(user.getName() + " has logged out.");
        } else {
            System.out.println("User is not logged in.");
        }
    }

    public static boolean changePassword(User user, String oldPass, String newPass) {
        if (!user.isLoggedIn()) { // not logged in
            System.out.println("You must be logged in to perform this action.");
            return false;
        }

        if (user.getPassword().equals(oldPass)) { // password change successfully
            user.setPassword(newPass);
            return true;
        }

        System.out.println("Old password does not match."); // old password is wrong
        return false;
    }
}
