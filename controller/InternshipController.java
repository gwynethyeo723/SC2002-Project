package controller;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import database.GlobalInternshipList;
import entity.Company;
import entity.Application;
import enumeration.ApplicationStatus;
import entity.CareerCenterStaff;
import entity.CompanyRep;
import database.GlobalApplicationList;
import entity.Internship;
import enumeration.InternshipLevel;
import enumeration.InternshipStatus;
import entity.Student;

/**
 * Controller class that manages the lifecycle of internships.
 * <p>
 * Provides operations for company representatives to create, edit, delete and 
 * manage internships, for staff to review and approve them, and for students 
 * to view available internships. Also handles slot updates and report 
 * generation.
 */

public class InternshipController {

    /**
     * Creates a new internship for the given company representative, applying 
     * validations such as login status and maximum internship count.
     *
     * @param title          internship title
     * @param description    internship description
     * @param company        company offering the internship
     * @param rep            the company representative creating it
     * @param level          internship level (basic/intermediate/advanced)
     * @param preferredMajor preferred major for applicants
     * @param slots          number of available slots (max 10)
     * @param openingDate    application start date
     * @param closingDate    application end date
     * @return the created {@link Internship}, or {@code null} if creation failed
     */
    // Create a new internship
    public static Internship createInternship(String title, String description, Company company, CompanyRep rep,
                                              InternshipLevel level, String preferredMajor, int slots, 
                                              Date openingDate, Date closingDate) {

        if (!rep.isLoggedIn()) {
            System.out.println("You must be logged in to create an internship.");
            return null;
        }

    // Limit to 5 internships per representative
    long repCount = GlobalInternshipList.getByCompanyRep(rep).size();
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

    /**
     * Edits an existing internship, updating its fields if the representative is 
     * logged in. Slot count is capped at 10.
     *
     * @param rep              company representative editing the internship
     * @param internship       internship being modified
     * @param newTitle         updated title
     * @param newDescription   updated description
     * @param newLevel         updated level
     * @param newPreferredMajor updated preferred major
     * @param newSlots         updated number of slots
     * @param newOpeningDate   updated opening date
     * @param newClosingDate   updated closing date
     */
    // Edit an existing internship
    public static void editInternship(CompanyRep rep, Internship internship, String newTitle, String newDescription,
                                      InternshipLevel newLevel, String newPreferredMajor, int newSlots,
                                      Date newOpeningDate, Date newClosingDate) {

        if (!rep.isLoggedIn()) {
            System.out.println("You must be logged in to edit an internship.");
            return;
        }

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

    /**
     * Allows a career center staff member to approve or reject an internship 
     * submitted by a company representative.
     *
     * @param staff      the reviewing staff member
     * @param internship the internship under review
     * @param approve    {@code true} to approve, {@code false} to reject
     */
    // Approve or reject an internship created by a CompanyRep
    public static void reviewInternship(CareerCenterStaff staff, Internship internship, boolean approve) {
        if (!staff.isLoggedIn()) {
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

    /**
     * Deletes an internship created by a company representative and marks all
     * associated applications as {@link ApplicationStatus#DELETED}.
     *
     * @param rep        the company representative requesting deletion
     * @param internship the internship to delete
     */    
    // Delete an internship
    public static void deleteInternship(CompanyRep rep, Internship internship) {
        if (!rep.isLoggedIn()) {
            System.out.println("You must be logged in to delete an internship.");
            return;
        }

        // Update all applications for this internship to DELETED
        List<Application> appsForInternship = GlobalApplicationList.getByInternship(internship);
        for (Application app : appsForInternship) {
            app.setStatus(ApplicationStatus.DELETED);
        }

        System.out.println("Internship '" + internship.getTitle() + "' deleted successfully.");
    }

    /**
     * Decreases the remaining slot count for an internship. If slots reach zero,
     * the internship status becomes {@link InternshipStatus#FILLED}.
     *
     * @param internship the internship whose slots are reduced
     */
    // Decrease available slots
    public static void decreaseSlot(Internship internship) {
        if (internship.getSlotsRemaining() > 0) {
            internship.setSlotsRemaining(internship.getSlotsRemaining() - 1);
            if (internship.getSlotsRemaining() == 0) {
                internship.setStatus(InternshipStatus.FILLED);
            }
        } else {
            System.out.println("No slots remaining to decrease.");
        }
    }

    /**
     * Increases the remaining slot count for an internship. If the internship 
     * was previously filled, it becomes approved again.
     *
     * @param internship the internship whose slots are increased
     */
    // Increase available slots
    public static void increaseSlot(Internship internship) {
        internship.setSlotsRemaining(internship.getSlotsRemaining() + 1);
        if (internship.getStatus() == InternshipStatus.FILLED) {
            internship.setStatus(InternshipStatus.APPROVED); // reopen if previously full
        }
    }

    /**
     * Returns a list of internships available for a given student based on 
     * visibility, approval status, slot availability, and application dates.
     *
     * @param student the student viewing available internships
     * @return a list of available internships
     */
    public static List<Internship> viewAvailableInternships(Student student) {
        if (!student.isLoggedIn()) {
            System.out.println("You must be logged in to view available internships.");
            return List.of();
        }

        LocalDate today = LocalDate.now();

        // Filter internships that are visible, approved, not filled, and within application period
        List<Internship> available = GlobalInternshipList.getAll().stream()
                .filter(i -> i.getVisibility()
                        && i.getStatus() == InternshipStatus.APPROVED
                        && i.getSlotsRemaining() > 0
                        && !i.getRepresentative().equals(student) // optionally filter out internships by student themselves
                        && today.isAfter(i.getOpeningDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().minusDays(1))
                        && today.isBefore(i.getClosingDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().plusDays(1))
                )
                .toList();

        if (available.isEmpty()) {
            System.out.println("No internships available for you at the moment.");
        } else {
            System.out.println("Available internships:");
            for (Internship i : available) {
                System.out.println("- " + i.getTitle() + " at " + i.getCompany().getName() +
                                   " [" + i.getLevel() + ", Slots: " + i.getSlotsRemaining() + "]");
            }
        }

        return available;
    }

    /**
     * Generates and prints a report of internships, optionally filtered by 
     * status, preferred major, or level.
     *
     * @param staff        the staff member requesting the report
     * @param filterStatus optional status filter
     * @param filterMajor  optional major filter
     * @param filterLevel  optional level filter
     */
    // Generate a report on internships (example: by status)
    public static void generateInternshipReport(CareerCenterStaff staff, String filterStatus, String filterMajor, String filterLevel) {

        if (!staff.isLoggedIn()) {
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

