package entity;

public abstract class User {
    protected String userId;
    protected String name;
    private String password;
    protected boolean isLoggedIn = false; // default false

    // ----- This class defines the attributes relevant to the User abstract class. -----//
    // ----- Relevant attributes include UserID, Name and Password. -----//
    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.password = "password"; // default password
    }

    // public boolean login(String inputUserId, String password) {
    //     if (!this.getUserId().equals(inputUserId)) {
    //         System.out.println("User ID does not exist.");
    //         return false;
    //     }

    //     if (!this.password.equals(password)) {
    //         System.out.println("Incorrect password.");
    //         return false;
    //     }

    //     this.isLoggedIn = true; // set to true on successful login
    //     System.out.println("Login successful for " + this.getName());
    //     return true;
    // }

    // public void logout() {
    //     if (isLoggedIn) {
    //         isLoggedIn = false;
    //         System.out.println(this.getName() + " has logged out.");
    //     } else {
    //         System.out.println("User is not logged in.");
    //     }
    // }

    // public boolean changePassword(String oldPass, String newPass) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return false; // <- fixed
    //     }
    //     if(this.password.equals(oldPass)) {
    //         this.password = newPass;
    //         return true;
    //     }
    //     return false;
    // }

    public String getName() {
        return this.name;
    }

    public void setName(String newName){
        this.name=newName;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

}
