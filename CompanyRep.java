import java.util.Date;
import java.util.List;

public class CompanyRep extends User {
    private Company company;
    private CompanyRepStatus status;
    private String department;
    private String position;
    private String email; // new attribute, serves as userId

    public CompanyRep(String email, String name, Company company, String department, String position) {
        super(email, name); // use email as userId in parent User class
        this.email = email;
        this.company = company;
        this.status = CompanyRepStatus.PENDING; // default until authorized
        this.department = department;
        this.position = position;
        // company.addRepresentative(this);
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

    // Create internship with all required fields
    // public Internship createInternship(String title, String description, String level, 
    //                                String preferredMajor, int slots, Date openingDate, Date closingDate) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return null;
    //     }

    //     // Limit to 5 internships per company rep
    //     long repCount = company.getInternshipsOffered().stream()
    //                        .filter(i -> i.getRepresentative().equals(this))
    //                        .count();
    //     if (repCount >= 5) {
    //         System.out.println("Cannot create more than 5 internships per representative.");
    //         return null;
    //     }

    //     // Cap slots at 10
    //     if (slots > 10) slots = 10;

    //     Internship internship = new Internship(title, description, company, this, level, preferredMajor, slots, openingDate, closingDate);

    //     company.addInternship(internship);
    //     GlobalInternshipList.addInternship(internship); // add immediately to global list

    //     System.out.println("Internship '" + title + "' created and awaiting approval.");
    //     return internship;
    // }


    // @Override
    // public boolean login(String inputUserId, String password) {
    //     // First, check approval status
    //     if (this.status != CompanyRepStatus.APPROVED) {
    //         System.out.println("Account not approved yet. Please contact Career Center Staff.");
    //         return false;
    //     }
    //     // Call parent method to check userId and password
    //     return super.login(inputUserId, password);
    // }

    // Review student application (approve or reject)
    // public void reviewApplication(Student student, Internship internship, boolean approve) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return;
    //     }
    //     // Check that this rep owns the internship
    //     if(!internship.getRepresentative().equals(this)) {
    //         System.out.println("You cannot review applications for internships you do not manage.");
    //         return;
    //     }

    //     // Check if student actually applied
    //     if(!student.getAppliedInternships().containsKey(internship)) {
    //         System.out.println(student.getName() + " did not apply to this internship.");
    //         return;
    //     }

    //     // Approve or reject
    //     if(approve) {
    //         student.getAppliedInternships().put(internship, ApplicationStatus.SUCCESSFUL);
    //     } else {
    //         student.getAppliedInternships().put(internship, ApplicationStatus.UNSUCCESSFUL);
    //     }
    // }

    // public void toggleInternshipVisibility(Internship internship) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return;
    //     }
    //     // Check that this rep owns the internship
    //     if (!internship.getRepresentative().equals(this)) {
    //         System.out.println("You cannot change visibility for internships you do not manage.");
    //         return;
    //     }

    //     // Toggle visibility using the existing setter
    //     internship.setVisibility(!internship.isVisible());
    //     System.out.println("Internship '" + internship.getTitle() + " 's visibility is now " 
    //                    + (internship.isVisible() ? "ON" : "OFF"));
    // }
    
    
    // public void editInternship(Internship internship, String newTitle, String newDescription, 
    //                        String newLevel, String newPreferredMajor, int newSlots, 
    //                        Date newOpeningDate, Date newClosingDate) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return;
    //     }

    //     // Only the representative who created the internship can edit
    //     if (!internship.getRepresentative().equals(this)) {
    //         System.out.println("You cannot edit internships you do not manage.");
    //         return;
    //     }

    //     // Only Pending or Rejected internships can be edited
    //     if (internship.getStatus() != InternshipStatus.PENDING &&internship.getStatus() != InternshipStatus.REJECTED) {
    //         System.out.println("Only internships with Pending or Rejected status can be edited.");
    //         return;
    //     }

    //     // Enforce maximum slots of 10
    //     if (newSlots > 10) {
    //         System.out.println("Number of slots cannot exceed 10. Setting slots to 10.");
    //         newSlots = 10;
    //     }

    //     // Convert string to enum safely
    //     InternshipLevel levelEnum;
    //     try {
    //         levelEnum = InternshipLevel.valueOf(newLevel.toUpperCase());
    //     } catch (IllegalArgumentException e) {
    //         System.out.println("Invalid level entered. Please choose: Basic, Intermediate, or Advanced.");
    //         return;
    //     }

    //     // Apply updates
    //     internship.setTitle(newTitle);
    //     internship.setDescription(newDescription);
    //     internship.setLevel(levelEnum);
    //     internship.setPreferredMajor(newPreferredMajor);
    //     internship.setTotalSlots(newSlots);      // updated slots
    //     internship.setSlotsRemaining(newSlots);  // reset remaining slots
    //     internship.setOpeningDate(newOpeningDate);
    //     internship.setClosingDate(newClosingDate);

    //     System.out.println("Internship '" + internship.getTitle() + "' has been updated.");
    // }

    // public void deleteInternship(Internship internship) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return;
    //     }

    //     // Only allow deleting internships owned by this rep
    //     if (!internship.getRepresentative().equals(this)) {
    //         System.out.println("Cannot delete an internship you do not manage.");
    //         return;
    //     }

    //     // Remove internship from all students who applied
    //     for (Student s : internship.getApplicants()) {
    //         s.removeAppliedInternship(internship); // remove from appliedInternships map
    //         if (s.getAcceptedInternship() != null && s.getAcceptedInternship().equals(internship)) {
    //             s.setAcceptedInternship(null); // clear accepted internship if needed
    //         }
    //     }

    //     // Remove from company's internship list
    //     internship.getCompany().getInternshipsOffered().remove(internship);

    //     // Remove from global internship list
    //     GlobalInternshipList.removeInternship(internship);

    //     System.out.println("Internship '" + internship.getTitle() + "' deleted successfully.");
    // }   


    // public void viewInternships() {
    //     viewInternships(null); // calls the overloaded method with null input
    // }

    // public void viewInternships(Internship specificInternship) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return;
    //     }

    //     List<Internship> myInternships = company.getInternshipsOffered().stream()
    //         .filter(i -> i.getRepresentative().equals(this))
    //         .toList();

    //     if (myInternships.isEmpty()) {
    //         System.out.println("You have not created any internships yet.");
    //         return;
    //     }

    //     System.out.println("---- My Internships ----");

    //     for (Internship internship : myInternships) {
    //         if (specificInternship != null && !internship.equals(specificInternship)) {
    //             continue; // skip others
    //         }

    //         System.out.println("Title: " + internship.getTitle()
    //                 + " | Status: " + internship.getStatus()
    //                 + " | Level: " + internship.getLevel()
    //                 + " | Preferred Major: " + internship.getPreferredMajor()
    //                 + " | Slots: " + internship.getSlotsRemaining() + "/" + internship.getTotalSlots()
    //                 + " | Visible: " + (internship.isVisible() ? "Yes" : "No")
    //                 + " | Opening Date: " + internship.getOpeningDate()
    //                 + " | Closing Date: " + internship.getClosingDate());
    //     }
    // }

}
