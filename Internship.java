import java.util.*;

public class Internship {
    private String title;
    private String description;
    private Company company;
    private CompanyRep representative;
    private InternshipLevel level;
    private String preferredMajor;
    private int totalSlots;
    private int slotsRemaining;
    private boolean visible;
    private InternshipStatus status;
    private Date openingDate;
    private Date closingDate;

    // Constructor
    public Internship(String title, String description, Company company, CompanyRep representative,
                        String level, String preferredMajor, int slots, Date openingDate, Date closingDate) {
        this.title = title;
        this.description = description;
        this.company = company;
        this.representative = representative;
        this.level = mapLevel(level);
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
            
        // Limit slots to at most 10
        if (slots > 10) {
            System.out.println("Number of slots cannot exceed 10. Setting slots to 10.");
            this.totalSlots = 10;
        } else {
            this.totalSlots = slots;
        }
    
        this.slotsRemaining = this.totalSlots;
        this.visible = false; // default visibility
        this.status = InternshipStatus.PENDING;
    }

    private InternshipLevel mapLevel(String level) {
        switch(level.toLowerCase()) {
            case "basic": return InternshipLevel.BASIC;
            case "intermediate": return InternshipLevel.INTERMEDIATE;
            case "advanced": return InternshipLevel.ADVANCED;
            default: return InternshipLevel.BASIC; // fallback
        }
    }


    // Move to application
    // // Decrease available slots by 1
    // public void decreaseSlot() {
    //     if (slotsRemaining > 0) {
    //         slotsRemaining--;
    //         if (slotsRemaining == 0) {
    //             status = InternshipStatus.FILLED; // use enum
    //         }
    //     }
    // }


    // Move to application
    // public void increaseSlot() {
    //     slotsRemaining++;
    //     if (status == InternshipStatus.FILLED) { // compare enum directly
    //         status = InternshipStatus.APPROVED; // reopen if previously full
    //     }
    // }


    // Move to application
    // // Toggle visibility for students
    // public void setVisibility(boolean visibility) {
    //     this.visible = visibility;
    // }

    public boolean isVisible() {
        return visible;
    }

    // Setters and Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public InternshipLevel getLevel() { return level; }
    public void setLevel(InternshipLevel level) { this.level = level; }
    public String getPreferredMajor() { return preferredMajor; }
    public int getTotalSlots() { return totalSlots; }
    public int getSlotsRemaining() { return slotsRemaining; }
    public InternshipStatus getStatus() { return status; }
    public void setStatus(InternshipStatus status) { this.status = status; }
    public Company getCompany() { return company; }
    public CompanyRep getRepresentative() { return representative; }
    public Date getOpeningDate() { return openingDate; }
    public Date getClosingDate() { return closingDate; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }
    public void setTotalSlots(int slots) { this.totalSlots = slots; }
    public void setSlotsRemaining(int slots) { this.slotsRemaining = slots; }
    public void setOpeningDate(Date openingDate) { this.openingDate = openingDate; }
    public void setClosingDate(Date closingDate) { this.closingDate = closingDate; }
}