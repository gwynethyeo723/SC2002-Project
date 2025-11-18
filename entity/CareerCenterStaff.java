package entity;

/**
 * Represents a career center staff member in the system.
 * <p>
 * Extends {@link User} and adds staff-specific information such as department.
 * Staff members have the ability to review internships and withdrawal requests.
 */
public class CareerCenterStaff extends User {
    private String department;

    /**
     * Creates a new CareerCenterStaff member with the given user details.
     *
     * @param userId     the staff member's login ID
     * @param name       the staff member's name
     * @param department the department the staff member belongs to
     */
    public CareerCenterStaff(String userId, String name, String department) {
        super(userId, name);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Authenticates the staff member using their ID and password.
     * Overrides the {@link User#login(String, String)} method.
     *
     * @param id        the entered user ID
     * @param password2 the entered password
     * @return {@code true} if login is successful, otherwise {@code false}
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