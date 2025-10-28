import java.util.Date;

public class CompanyRep extends User {
    private Company company;
    private String status; // Pending, Approved, Rejected
    private String department;
    private String position;
    private String email; // new attribute, serves as userId

    public CompanyRep(String email, String name, Company company, String department, String position) {
        super(email, name); // use email as userId in parent User class
        this.email = email;
        this.company = company;
        this.status = "Pending"; // default until authorized
        this.department = department;
        this.position = position;
        company.addRepresentative(this);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Create internship with all required fields
    public Internship createInternship(String title, String description, String level, String preferredMajor, int slots, Date openingDate, Date closingDate) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return null;
        }
        if(company.getInternshipsOffered().size() >= 5) {
            System.out.println("Cannot create more than 5 internships for this company.");
            return null;
        }

        Internship internship = new Internship(title, description, company, this, level, preferredMajor, slots, openingDate, closingDate);
        company.addInternship(internship);
        System.out.println("Internship '" + title + "' created and awaiting approval.");
        return internship;
    }


    @Override
    public boolean login(String inputUserId, String password) {
        // First, check approval status
        if (!"Approved".equals(this.status)) {
            System.out.println("Account not approved yet. Please contact Career Center Staff.");
            return false;
        }
        // Call parent method to check userId and password
        return super.login(inputUserId, password);
    }

    // Review student application (approve or reject)
    public void reviewApplication(Student student, Internship internship, boolean approve) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        // Check that this rep owns the internship
        if(!internship.getRepresentative().equals(this)) {
            System.out.println("You cannot review applications for internships you do not manage.");
            return;
        }

        // Check if student actually applied
        if(!student.getAppliedInternships().containsKey(internship)) {
            System.out.println(student.getName() + " did not apply to this internship.");
            return;
        }

        // Approve or reject
        if(approve) {
            student.getAppliedInternships().put(internship, "Successful"); // only update status
            System.out.println("Application approved for " + student.getName());
        } else {
            student.getAppliedInternships().put(internship, "Unsuccessful");
            System.out.println("Application rejected for " + student.getName());
        }
    }

    public void toggleInternshipVisibility(Internship internship) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        // Check that this rep owns the internship
        if (!internship.getRepresentative().equals(this)) {
            System.out.println("You cannot change visibility for internships you do not manage.");
            return;
        }

        // Toggle visibility using the existing setter
        internship.setVisibility(!internship.isVisible());
        System.out.println("Internship '" + internship.getTitle() + " 's visibility is now " + (internship.isVisible() ? "ON" : "OFF"));
    }
}
