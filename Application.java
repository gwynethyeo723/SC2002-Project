import java.time.LocalDate;

// ----- This class defines the attributes relevant to each Application. -----//
// ----- Relevant attributes include Student, Internship, Status and Application Date. -----//

public class Application {
    private Student student;
    private Internship internship;
    private ApplicationStatus status;
    private LocalDate applicationDate;

    public Application(Student student, Internship internship, ApplicationStatus status) {
        this.student = student;
        this.internship = internship;
        this.status = status;
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