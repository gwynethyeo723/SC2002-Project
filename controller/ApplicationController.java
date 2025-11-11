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

public class ApplicationController {

    // 1. Create a new application when a student applies for an internship
    public static void createApplication(Student student, Internship internship) {
        if (!student.isLoggedIn()) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }

        // Check maximum 3 active applications
        long activeApplications = GlobalApplicationList.getByStudent(student).stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING
                        || a.getStatus() == ApplicationStatus.APPROVED
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
                || app.getStatus() == ApplicationStatus.ACCEPTED_BY_COMPANY_REPRESENTATIVE) {
            app.setStatus(ApplicationStatus.PENDING_WITHDRAWAL);
        } else if (app.getStatus() == ApplicationStatus.PENDING || app.getStatus() == ApplicationStatus.APPROVED) {
            app.setStatus(ApplicationStatus.WITHDRAWN);
        } else {
            System.out.println("Cannot withdraw: Current status is " + app.getStatus());
            return;
        }

        System.out.println(student.getName() + " has requested withdrawal from " + internship.getTitle());
    }

    
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

        Application app = applications.get(0);

        if (app.getStatus() == ApplicationStatus.ACCEPTED_BY_COMPANY_REPRESENTATIVE) {
            app.setStatus(ApplicationStatus.ACCEPTED_BY_STUDENT);
            InternshipController.decreaseSlot(internship);
            System.out.println(student.getName() + " accepted internship: " + internship.getTitle() + " at " + internship.getCompany().getName());
        } else {
            System.out.println("Cannot accept internship: Current status is " + app.getStatus());
        }
    }

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
        if (app.getStatus() == ApplicationStatus.PENDING) {
            app.setStatus(ApplicationStatus.ACCEPTED_BY_COMPANY_REPRESENTATIVE);
            System.out.println("Application by " + student.getName() + " has been accepted by " + rep.getName());
        } else {
            System.out.println("Cannot accept application: Current status is " + app.getStatus());
        }
    }
}
