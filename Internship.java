import java.util.*;

public class Internship {
    private String title;
    private String description;
    private Company company;
    private CompanyRep representative;
    private String level; // Basic, Intermediate, Advanced
    private String preferredMajor;
    private int totalSlots;
    private int slotsRemaining;
    private boolean visible;
    private String status; // Pending, Approved, Rejected, Filled
    private List<Student> applicants;
    private List<Student> acceptedStudents;
    private Date openingDate;
    private Date closingDate;

    // Constructor
    public Internship(String title, String description, Company company, CompanyRep representative,
                  String level, String preferredMajor, int slots, Date openingDate, Date closingDate) {
        this.title = title;
        this.description = description;
        this.company = company;
        this.representative = representative;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.applicants = new ArrayList<>();
        this.acceptedStudents = new ArrayList<>();

    
        // Limit slots to at most 10
        if (slots > 10) {
            System.out.println("Number of slots cannot exceed 10. Setting slots to 10.");
            this.totalSlots = 10;
        } else {
            this.totalSlots = slots;
        }
    
        this.slotsRemaining = this.totalSlots;
        this.visible = false; // default visibility
        this.status = "Pending"; // default status
    }

    // Add a student as applicant
    public void addApplicant(Student student) {
        if(!applicants.contains(student)) {
            applicants.add(student);
        }
    }

    // Decrease available slots by 1
    public void decreaseSlot() {
        if(slotsRemaining > 0) {
            slotsRemaining--;
            if(slotsRemaining == 0) {
                status = "Filled";
            }
        }
    }

    public void increaseSlot() {
        slotsRemaining++;
        if (status.equals("Filled")) {
            setStatus("Approved"); // reopen if previously full
        }
    }

    public void addAcceptedStudent(Student student) {
        if (!acceptedStudents.contains(student)) {
            acceptedStudents.add(student);
        }
    }

    public List<Student> getAcceptedStudents() {
        return acceptedStudents;
    }

    // Toggle visibility for students
    public void setVisibility(boolean visibility) {
        this.visible = visibility;
    }

    public boolean isVisible() {
        return visible;
    }

    // Setters and Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public int getTotalSlots() { return totalSlots; }
    public int getSlotsRemaining() { return slotsRemaining; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Company getCompany() { return company; }
    public CompanyRep getRepresentative() { return representative; }
    public List<Student> getApplicants() { return applicants; }
    public Date getOpeningDate() { return openingDate; }
    public Date getClosingDate() { return closingDate; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLevel(String level) { this.level = level; }
    public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }
    public void setTotalSlots(int slots) { this.totalSlots = slots; }
    public void setSlotsRemaining(int slots) { this.slotsRemaining = slots; }
    public void setOpeningDate(Date openingDate) { this.openingDate = openingDate; }
    public void setClosingDate(Date closingDate) { this.closingDate = closingDate; }
}