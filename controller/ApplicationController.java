package controller;
import java.time.LocalDate;
import java.util.List;

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
 * Controller class that manages internship applications.
 * <p>
 * Provides operations for students to apply, withdraw and accept internships,
 * for company representatives to accept applications, and for career center
 * staff to review withdrawal requests.
 */

public class ApplicationController {

    /**
     * Controller class that manages internship applications.
     * <p>
     * Provides operations for students to apply, withdraw and accept internships,
     * for company representatives to accept applications, and for career center
     * staff to review withdrawal requests.
     */

    // 1. Create a new application when a student applies for an internship
    public static void createApplication(Student student, Internship internship) {
        if (!student.isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }

        // Check maximum 3 active applications
        long activeApplications = GlobalApplicationList.getByStudent(student).stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING
                        || a.getStatus() == ApplicationStatus.ACCEPTED_BY_COMPANY_REPRESENTATIVE
                        || a.getStatus() == ApplicationStatus.ACCEPTED_BY_STUDENT)
                .count();

        if (activeApplications >= 3) {
            System.out.println(student.getName() + " has already applied for 3 internships.");
            return;
        }

        // Check year-level restriction
        if (student.getYear() < 3 && internship.getLevel() != InternshipLevel.BASIC) {
            System.out.println("Year restriction: cannot apply for this level.");
            return;
        }

        // Check visibility and approval
        if (!internship.getVisibility() || internship.getStatus() != InternshipStatus.APPROVED) {
            System.out.println("Cannot apply: Internship not available.");
            return;
        }

        // Check application period
        LocalDate today = LocalDate.now();
        if (today.isBefore(internship.getOpeningDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                || today.isAfter(internship.getClosingDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())) {
            System.out.println("Cannot apply: Outside application period.");
            return;
        }

        // Create application and add to global list
        Application application = new Application(student, internship);
        GlobalApplicationList.addApplication(application);
        System.out.println(student.getName() + " applied for " + internship.getTitle() + " at " + internship.getCompany().getName());
    }

    /**
     * Requests withdrawal from an existing application for the given internship
     * on behalf of the student, updating the application status accordingly.
     *
     * @param student    the student requesting withdrawal
     * @param internship the internship from which the student wants to withdraw
     */
    // 2. Student requests withdrawal from an application
    public static void requestWithdrawal(Student student, Internship internship) {
        if (!student.isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }

        List<Application> applications = GlobalApplicationList.getByStudent(student).stream()
                .filter(a -> a.getInternship().equals(internship))
                .toList();

        if (applications.isEmpty()) {
            System.out.println("No application found for this internship.");
            return;
        }

        Application app = applications.get(0);

        if (app.getStatus() == ApplicationStatus.ACCEPTED_BY_STUDENT
                || app.getStatus() == ApplicationStatus.ACCEPTED_BY_COMPANY_REPRESENTATIVE
                || app.getStatus() == ApplicationStatus.PENDING){
            app.setStatus(ApplicationStatus.PENDING_WITHDRAWAL);
        // } else if (app.getStatus() == ApplicationStatus.PENDING || app.getStatus() == ApplicationStatus.APPROVED) {
        //     app.setStatus(ApplicationStatus.WITHDRAWN);
        } else {
            System.out.println("Cannot withdraw: Current status is " + app.getStatus());
            return;
        }

        System.out.println(student.getName() + " has requested withdrawal from " + internship.getTitle());
    }

    /**
     * Reviews a student's withdrawal request for a specific internship and
     * either approves or rejects it on behalf of a career center staff member.
     *
     * @param staff      the staff member reviewing the withdrawal request
     * @param student    the student who requested the withdrawal
     * @param internship the internship associated with the application
     * @param approve    {@code true} to approve the withdrawal, {@code false} to reject it
     */
    
    // 3. Career center staff to approve or reject a student's withdrawal request
    public static void reviewWithdrawal(CareerCenterStaff staff, Student student, Internship internship, boolean approve) {
        if (!staff.isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }

        // First check if the student actually requested withdrawal
        boolean isPendingWithdrawal = GlobalApplicationList.getByInternshipAndStudent(internship, student).stream()
        .anyMatch(app -> app.getStatus() == ApplicationStatus.PENDING_WITHDRAWAL);
        if (!isPendingWithdrawal) {
            System.out.println(student.getName() + " has not requested withdrawal for this internship.");
            return;
        }

        if (approve) {
            // Get the application for this student and internship
            GlobalApplicationList.getByInternshipAndStudent(internship, student).stream()
                .findFirst()
                .ifPresent(app -> {
                    // Update the application status
                    app.setStatus(ApplicationStatus.WITHDRAWN);
                    InternshipController.increaseSlot(internship);

                    // If the internship was accepted by the student, return the slot
                    if (app.getStatus() == ApplicationStatus.ACCEPTED_BY_STUDENT) {
                        InternshipController.increaseSlot(internship);
                    }
                });

            System.out.println("Withdrawal approved for " + student.getName() + " from " + internship.getTitle());
        } else {
            System.out.println("Withdrawal rejected for " + student.getName() + " from " + internship.getTitle());
        }
    }

    /**
     * Allows a student to accept an internship offer, provided that the
     * application has already been accepted by the company representative.
     *
     * @param student    the student accepting the internship
     * @param internship the internship being accepted
     */
    // 4. Student accepts an internship (after being approved by company)
    public static void acceptInternship(Student student, Internship internship) {
        if (!student.isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }

        List<Application> applications = GlobalApplicationList.getByStudent(student).stream()
                .filter(a -> a.getInternship().equals(internship))
                .toList();

        if (applications.isEmpty()) {
            System.out.println("No application found for this internship.");
            return;
        }

    Application acceptedApp = applications.get(0);

    if (acceptedApp.getStatus() == ApplicationStatus.ACCEPTED_BY_COMPANY_REPRESENTATIVE) {
        acceptedApp.setStatus(ApplicationStatus.ACCEPTED_BY_STUDENT);
        InternshipController.decreaseSlot(internship);

        System.out.println(student.getName() + " accepted internship: "
                + internship.getTitle() + " at " + internship.getCompany().getName());

        GlobalApplicationList.getByStudent(student).stream()
                .filter(app -> app != acceptedApp) 
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING
                        || app.getStatus() == ApplicationStatus.ACCEPTED_BY_COMPANY_REPRESENTATIVE
                        || app.getStatus() == ApplicationStatus.PENDING_WITHDRAWAL)
                .forEach(app -> app.setStatus(ApplicationStatus.WITHDRAWN));
        } else {
            System.out.println("Cannot accept internship: Current status is " + acceptedApp.getStatus());
        }
    }

    /**
     * Allows a company representative to accept a student's application
     * for a specific internship, if the application is still pending.
     *
     * @param rep        the company representative accepting the application
     * @param internship the internship the student applied for
     * @param student    the student whose application is being accepted
     */
    // 5. Company representative accepts a student for an internship 
    public static void acceptApplicationByCompanyRep(CompanyRep rep, Internship internship, Student student) {
        if (!rep.isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }

        List<Application> applications = GlobalApplicationList.getByInternshipAndStudent(internship, student);
        if (applications.isEmpty()) {
            System.out.println("No application found for this internship and student.");
            return;
        }

        Application app = applications.get(0);
        if (app.getStatus() == ApplicationStatus.PENDING
            || internship.getSlotsRemaining() > 0) { // conditions, add code here 
            app.setStatus(ApplicationStatus.ACCEPTED_BY_COMPANY_REPRESENTATIVE);
            System.out.println("Application by " + student.getName() + " has been accepted by " + rep.getName());
        } else {
            System.out.println("Cannot accept application: Current status is " + app.getStatus());
        }
    }
}
