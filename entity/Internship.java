package entity;
import java.util.*;

import enumeration.InternshipLevel;
import enumeration.InternshipStatus;

/**
 * Represents an internship offered by a company.
 * <p>
 * Stores details such as title, description, company, representative,
 * internship level, preferred major, slot availability, visibility,
 * approval status, and application period.
 */
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

    /**
     * Creates a new internship with the specified details.
     * Slots are capped at a maximum of 10 and the internship starts
     * with {@link InternshipStatus#PENDING} and hidden visibility.
     *
     * @param title          internship title
     * @param description    description of the internship role
     * @param company        company offering the internship
     * @param representative the company representative who created it
     * @param level          internship level (basic/intermediate/advanced)
     * @param preferredMajor preferred applicant major
     * @param slots          total available slots (max 10)
     * @param openingDate    date applications open
     * @param closingDate    date applications close
     */
    public Internship(String title, String description, Company company, CompanyRep representative,
                        InternshipLevel level, String preferredMajor, int slots, Date openingDate, Date closingDate) {
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

    private InternshipLevel mapLevel(InternshipLevel level) {
        switch(level.toString().toLowerCase()) {
            case "basic": return InternshipLevel.BASIC;
            case "intermediate": return InternshipLevel.INTERMEDIATE;
            case "advanced": return InternshipLevel.ADVANCED;
            default: return InternshipLevel.BASIC; // fallback
        }
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
    public boolean getVisibility() {return visible;}
    public void setStatus(InternshipStatus status) { this.status = status; }
    public Company getCompany() { return company; }
    public CompanyRep getRepresentative() { return representative; }
    public void setRepresentative(CompanyRep representative) { this.representative=representative; }
    public Date getOpeningDate() { return openingDate; }
    public Date getClosingDate() { return closingDate; }
    public void setTitle(String title) { this.title = title; }
    public void setVisibility(boolean visibility) {this.visible = visibility;}
    public void setDescription(String description) { this.description = description; }
    public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }
    public void setTotalSlots(int slots) { this.totalSlots = slots; }
    public void setSlotsRemaining(int slots) { this.slotsRemaining = slots; }
    public void setOpeningDate(Date openingDate) { this.openingDate = openingDate; }
    public void setClosingDate(Date closingDate) { this.closingDate = closingDate; }
}