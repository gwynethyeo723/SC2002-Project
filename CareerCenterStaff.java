import java.util.List;
import java.util.Map;
import java.util.Date;

public class CareerCenterStaff extends User {
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
            System.out.println("Internship '" + internship.getTitle() + "' approved, and visibility is turned ON.");
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

        // First check if the student actually requested withdrawal
        if (!student.isPendingWithdrawal()) {
            System.out.println(student.getName() + " has not requested withdrawal for this internship.");
            return;
        }

        if (approve) {
            // Mark internship as withdrawn in appliedInternships if it exists
            if (student.getAppliedInternships().containsKey(internship)) {
                student.getAppliedInternships().put(internship, "Withdrawn");
            }

            // Clear accepted internship if it matches
            if (student.getAcceptedInternship() != null && student.getAcceptedInternship().equals(internship)) {
                student.setAcceptedInternship(null);
                internship.increaseSlot(); // return slot if it was accepted
            }

            System.out.println("Withdrawal approved for " + student.getName() + " from " + internship.getTitle());
        } else {
            System.out.println("Withdrawal rejected for " + student.getName() + " from " + internship.getTitle());
        }

        // Reset pendingWithdrawal flag
        student.setPendingWithdrawal(false);
    }


    // Generate a report on internships (example: by status)
    public void generateInternshipReport(String filterStatus, 
                                     String filterMajor, 
                                     String filterLevel) {

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