package entity;
import enumeration.CompanyRepStatus;

/**
 * Represents a company representative in the system.
 * <p>
 * Extends {@link User} and includes additional information such as the
 * associated company, application approval status, department, position,
 * and email (used as the login ID).
 * <p>
 * Company representatives must be approved by Career Center staff before
 * they can log in successfully.
 */
public class CompanyRep extends User {
    private Company company;
    private CompanyRepStatus status;
    private String department;
    private String position;
    private String email; // new attribute, serves as userId

    /**
     * Creates a new company representative with the provided details.
     * The representative starts with {@link CompanyRepStatus#PENDING} status.
     *
     * @param email      the representative's email (also used as userId)
     * @param name       the representative's name
     * @param company    the company this representative belongs to
     * @param department the representative's department
     * @param position   the representative's job position
     */
    public CompanyRep(String email, String name, Company company, String department, String position) {
        super(email, name); // use email as userId in parent User class
        this.email = email;
        this.company = company;
        this.status = CompanyRepStatus.PENDING; // default until authorized
        this.department = department;
        this.position = position;
    }

    // Getters and setters
    public Company getCompany() {
        return company;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        super.setUserId(email); // update userId in User class as well
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public CompanyRepStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyRepStatus status) {
        this.status = status;
    }

    /**
     * Attempts to log in the company representative.
     * <p>
     * Login succeeds only if:
     * <ul>
     *   <li>the entered credentials match, and</li>
     *   <li>the representative's status is {@link CompanyRepStatus#APPROVED}</li>
     * </ul>
     * Otherwise, an appropriate message is shown and login fails.
     *
     * @param id        the entered user ID
     * @param password2 the entered password
     * @return {@code true} if login succeeds, otherwise {@code false}
     */
    @Override
    public boolean login(String id, String password2) {
        // check credentials first
        if (this.getUserId().equals(id) && this.getPassword().equals(password2)) {
            // check approval status
            if (this.getStatus() == CompanyRepStatus.APPROVED) {
                this.setLoggedIn(true);
                System.out.println("Login successful. Welcome, " + this.getName() + "!");
                return true;
            } else if (this.getStatus() == CompanyRepStatus.PENDING) {
                System.out.println("Your account is still pending approval by the Career Center.");
                return false;
            } else if (this.getStatus() == CompanyRepStatus.REJECTED) {
                System.out.println("Your account has been rejected. Please contact Career Services for assistance.");
                return false;
            }
        }
        return false;
    }
}
