package entity;

/**
 * Represents a student user in the system.
 * <p>
 * Extends {@link User} and includes additional student-specific 
 * information such as year of study and major.
 */
public class Student extends User {
    private int year;
    private String major;

    /**
     * Creates a new student with the specified details.
     *
     * @param userId the student's login ID
     * @param name   the student's name
     * @param year   the student's current year of study
     * @param major  the student's major
     */
    public Student(String userId, String name, int year, String major) {
        super(userId, name);
        this.year = year;
        this.major = major;
    }

    // Getters and Setters
    public int getYear() {return year;}
    public String getMajor() {return major;}
    public void setYear(int year) {this.year = year;}
    public void setMajor(String major){this.major = major;}

    /**
     * Authenticates the student using their ID and password.
     * Overrides the {@link User#login(String, String)} method.
     *
     * @param id        the entered user ID
     * @param password2 the entered password
     * @return {@code true} if the login is successful, otherwise {@code false}
     */
    @Override
    public boolean login(String id, String password2) {
        if (this.userId.equals(id) && this.getPassword().equals(password2)) {
            this.setLoggedIn(true);
            return true;
        }
        return false;
    }
}