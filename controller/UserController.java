package controller;

import java.util.List;

import entity.CompanyRep;
import entity.User;
import enumeration.CompanyRepStatus;

/**
 * Controller class that handles core user account actions such as login, 
 * logout, and password changes. Operates on {@link User} objects.
 */

public class UserController {


    /**
     * Attempts to log in a user using the provided user ID and password.
     * <p>
     * Searches the system's user list for a matching ID, validates the password,
     * and marks the user as logged in if successful.
     *
     * @param users        list of all registered users
     * @param inputUserId  the ID entered by the user
     * @param password     the password entered by the user
     * @return the authenticated {@link User}, or {@code null} if login failed
     */

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
        
        if (user instanceof CompanyRep rep) {
            if (rep.getStatus() != CompanyRepStatus.APPROVED) {
                System.out.println("Your account is not approved yet. Please wait for staff approval.");
                return null;
            }
        }

        // Successful login
        user.setLoggedIn(true);
        System.out.println("Login successful for " + user.getName());
        return user;
    }

    /**
     * Logs out the specified user by setting their login status to false.
     *
     * @param user the {@link User} to log out
     */
    public static void logout(User user) {
        if (user.isLoggedIn()) {
            user.setLoggedIn(false);
            System.out.println(user.getName() + " has logged out.");
        } else {
            System.out.println("User is not logged in.");
        }
    }

    /**
     * Changes the user's password if the user is logged in and the old
     * password matches.
     *
     * @param user     the {@link User} requesting the password change
     * @param oldPass  the user's current password
     * @param newPass  the new password to set
     * @return {@code true} if the password was changed successfully,
     *         {@code false} otherwise
     */
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
