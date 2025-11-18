package entity;

/**
 * Abstract base class representing a generic user in the system.
 * <p>
 * Stores common user attributes such as user ID, name, password, and
 * login status. Subclasses such as {@link Student}, {@link CompanyRep},
 * and {@link CareerCenterStaff} must implement the {@link #login(String, String)}
 * method to define their own authentication behaviour.
 */
public abstract class User {
    protected String userId;
    protected String name;
    protected String password;
    protected boolean isLoggedIn = false; // default false

    /**
     * Creates a new user with the given ID and name.
     * The user is assigned a default password ("password").
     *
     * @param userId the unique login ID of the user
     * @param name   the user's name
     */
    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.password = "password"; // default password
    }

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

    /**
     * Authenticates the user using the provided credentials.
     * Each subclass defines its own login rules.
     *
     * @param id        the entered user ID
     * @param password2 the entered password
     * @return {@code true} if login succeeds, otherwise {@code false}
     */
    public abstract boolean login(String id, String password2);

}
