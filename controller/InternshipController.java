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

public class InternshipController {

    // Create a new internship
    public static Internship createInternship(CompanyRep rep, String title, String description, Company company, 
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

    // Approve or reject an internship created by a CompanyRep
    public void reviewInternship(CareerCenterStaff staff, Internship internship, boolean approve) {
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

    // Increase available slots
    public static void increaseSlot(Internship internship) {
        internship.setSlotsRemaining(internship.getSlotsRemaining() + 1);
        if (internship.getStatus() == InternshipStatus.FILLED) {
            internship.setStatus(InternshipStatus.APPROVED); // reopen if previously full
        }
    }

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

    // Generate a report on internships (example: by status)
    public void generateInternshipReport(CareerCenterStaff staff, String filterStatus, String filterMajor, String filterLevel) {

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

