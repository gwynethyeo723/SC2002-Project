import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class InternshipController {

    // Create a new internship
    public static Internship createInternship(String title, String description, Company company, 
                                              CompanyRep rep, InternshipLevel level, 
                                              String preferredMajor, int slots, 
                                              Date openingDate, Date closingDate) {
        // Limit to 5 internships per representative
        long repCount = GlobalInternshipList.getAll().stream()
                .filter(i -> i.getRepresentative().equals(rep))
                .count();

        if (repCount >= 5) {
            System.out.println("Cannot create more than 5 internships per representative.");
            return null;
        }

        // Cap slots at 10
        if (slots > 10) slots = 10;

        Internship internship = new Internship(title, description, company, rep, level, preferredMajor, 
                                               slots, openingDate, closingDate);

        // Add to global list
        GlobalInternshipList.addInternship(internship);

        System.out.println("Internship '" + title + "' created successfully and awaiting approval.");
        return internship;
    }

    // Edit an existing internship
    public static void editInternship(Internship internship, String newTitle, String newDescription,
                                      InternshipLevel newLevel, String newPreferredMajor, int newSlots,
                                      Date newOpeningDate, Date newClosingDate) {
        // Enforce maximum slots of 10
        if (newSlots > 10) newSlots = 10;

        // Apply updates
        internship.setTitle(newTitle);
        internship.setDescription(newDescription);
        internship.setLevel(newLevel);
        internship.setPreferredMajor(newPreferredMajor);
        internship.setTotalSlots(newSlots);
        internship.setSlotsRemaining(newSlots); // reset remaining slots
        internship.setOpeningDate(newOpeningDate);
        internship.setClosingDate(newClosingDate);

        System.out.println("Internship '" + internship.getTitle() + "' has been updated.");
    }

    // Approve or reject an internship created by a CompanyRep
    public void reviewInternship(CompanyRep rep, Internship internship, boolean approve) {
        if (!rep.isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        if (approve) {
            internship.setStatus(InternshipStatus.APPROVED);
            internship.setVisibility(true);
        } else {
            internship.setStatus(InternshipStatus.REJECTED);
        }
    }

    // Delete an internship
    public static void deleteInternship(Internship internship) {
        // Update all applications for this internship to DELETED
        List<Application> appsForInternship = GlobalApplicationList.getByInternship(internship);
        for (Application app : appsForInternship) {
            app.setStatus(ApplicationStatus.DELETED);
        }

        System.out.println("Internship '" + internship.getTitle() + "' deleted successfully.");
    }

    // Generate a report on internships (example: by status)
    public void generateInternshipReport(CareerCenterStaff staff, String filterStatus, String filterMajor, String filterLevel) {

        if (!staff.isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        List<Internship> internships = GlobalInternshipList.getAll(); // use global list
        Date today = new Date(); // current date
        System.out.println("---- Internship Report ----");

        for (Internship internship : internships) {
            // Apply filters
            if (filterStatus != null) {
                try {
                    InternshipStatus statusFilter = InternshipStatus.valueOf(filterStatus.toUpperCase());
                    if (internship.getStatus() != statusFilter) continue;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status filter: " + filterStatus);
                    continue;
                }
            }
            if (filterMajor != null && !internship.getPreferredMajor().equalsIgnoreCase(filterMajor)) {
                continue;
            }
            if (filterLevel != null) {
                try {
                    InternshipLevel levelFilter = InternshipLevel.valueOf(filterLevel.toUpperCase());
                    if (internship.getLevel() != levelFilter) continue;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid level filter: " + filterLevel);
                    continue;
                }
            }
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