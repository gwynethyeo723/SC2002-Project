package enumeration;

/**
 * Represents the review and availability status of an internship.
 * <p>
 * Each status includes a display name used for user-friendly output.
 */
public enum InternshipStatus {
     /** Internship has been created and is awaiting review by career center staff. */
    PENDING("Pending"),
    /** Internship has been reviewed and approved for student applications. */
    APPROVED("Approved"),
     /** Internship has been reviewed and rejected by career center staff. */
    REJECTED("Rejected"),
    /** Internship has no available slots remaining. */
    FILLED("Filled");

    private final String displayName;

    InternshipStatus(String displayName) {
        this.displayName = displayName;
    }

    /** Returns the user-friendly display name of this status. */
    @Override
    public String toString() {
        return displayName;
    }
}
