public class UserController {
    public static boolean login(User user, String inputUserId, String password) {
        if (!user.getUserId().equals(inputUserId)) {
            System.out.println("User ID does not exist.");
            return false;
        }

        if (!user.getPassword().equals(password)) {
            System.out.println("Incorrect password.");
            return false;
        }

        user.setLoggedIn(true); // login successful 
        System.out.println("Login successful for " + user.getName());
        return true;
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
            System.out.println("Password changed successfully.");
            return true;
        }

        System.out.println("Old password does not match."); // old password is wrong
        return false;
    }
}
