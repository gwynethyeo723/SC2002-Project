package entity;
import java.time.LocalDate;

import enumeration.ApplicationStatus;

/**
 * Represents an internship application submitted by a student.
 * <p>
 * Stores the applicant ({@link Student}), the target {@link Internship},
 * the current application status, and the date the application was created.
 */
public class Application {
    private Student student;
    private Internship internship;
    private ApplicationStatus status;
    private LocalDate applicationDate;

    /**
     * Creates a new application for the given student and internship.
     * The application is initialized with {@link ApplicationStatus#PENDING}
     * and the current date.
     *
     * @param student     the student submitting the application
     * @param internship  the internship being applied for
     */
    public Application(Student student, Internship internship) {
        this.student = student;
        this.internship = internship;
        this.status = ApplicationStatus.PENDING;
        this.applicationDate = LocalDate.now(); // automatically set to current date
    }

    // ===== Getters and Setters =====
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Internship getInternship() {
        return internship;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }
}