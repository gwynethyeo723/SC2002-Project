import java.util.List;
import java.util.Date;

public class CareerCenterStaff extends User {
    //hi
    private String department;

    public CareerCenterStaff(String userId, String name, String department) {
        super(userId, name);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    // Approve or reject a CompanyRep account
    public void reviewCompanyRep(CompanyRep rep, boolean approve) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        if(approve) {
            rep.setStatus("Approved");
            System.out.println("CompanyRep " + rep.getName() + " approved.");
        } else {
            rep.setStatus("Rejected");
            System.out.println("CompanyRep " + rep.getName() + " rejected.");
        }
    }

    // Approve or reject an internship created by a CompanyRep
    public void reviewInternship(Internship internship, boolean approve) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        if (approve) {
            internship.setStatus("Approved");
            internship.setVisibility(true); // make it visible automatically
            GlobalInternshipList.addInternship(internship); // add to global list
            System.out.println("Internship '" + internship.getTitle() + "' approved, visibility ON, and added to global list.");
        } else {
            internship.setStatus("Rejected");
            System.out.println("Internship '" + internship.getTitle() + "' rejected and will not be visible to students.");
        }
    }

    // Approve or reject a student withdrawal request
    public void reviewWithdrawal(Student student, Internship internship, boolean approve) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        if(!student.getAppliedInternships().containsKey(internship)) {
            System.out.println(student.getName() + " did not apply for this internship.");
            return;
        }

        if(approve) {
            student.getAppliedInternships().put(internship, "Withdrawn");
            if(student.getAcceptedInternship() != null && student.getAcceptedInternship().equals(internship)) {
                student.setAcceptedInternship(null);
                internship.increaseSlot();
            }
            System.out.println("Withdrawal approved for " + student.getName());
        } else {
            System.out.println("Withdrawal rejected for " + student.getName());
        }
    }

    // Generate a report on internships (example: by status)
    public void generateInternshipReport(String filterStatus, String filterMajor, String filterLevel) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        List<Internship> internships = GlobalInternshipList.getAll(); // use global list
        Date today = new Date(); // current date
        System.out.println("---- Internship Report ----");

        for (Internship internship : internships) {
            // Apply filters
            if (filterStatus != null && !internship.getStatus().equalsIgnoreCase(filterStatus)) continue;
            if (filterMajor != null && !internship.getPreferredMajor().equalsIgnoreCase(filterMajor)) continue;
            if (filterLevel != null && !internship.getLevel().equalsIgnoreCase(filterLevel)) continue;

            // Determine if the internship is open or closed
            String openStatus = internship.getClosingDate().before(today) ? "Closed" : "Open";

            // Print internship details including total slots
            System.out.println(internship.getTitle() + " at " + internship.getCompany().getName()
                + " | Level: " + internship.getLevel()
                + " | Preferred Major: " + internship.getPreferredMajor()
                + " | Slots: " + internship.getSlotsRemaining() + "/" + internship.getTotalSlots()
                + " | Status: " + internship.getStatus()
                + " | Closing Date: " + internship.getClosingDate()
                + " | Open/Closed: " + openStatus);
        }
    }
}