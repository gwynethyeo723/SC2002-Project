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

    // Delete an internship
    public static void deleteInternship(Internship internship) {
        // Update all applications for this internship to DELETED
        List<Application> appsForInternship = GlobalApplicationList.getByInternship(internship);
        for (Application app : appsForInternship) {
            app.setStatus(ApplicationStatus.DELETED);
        }

        // Remove from global internship list
        GlobalInternshipList.removeInternship(internship);

        System.out.println("Internship '" + internship.getTitle() + "' deleted successfully.");
    }
}

