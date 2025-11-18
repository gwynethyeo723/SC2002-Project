package enumeration;

/**
 * Represents all possible statuses of a student's internship application.
 * <p>
 * Each status includes a display name for user-friendly output.
 */
public enum ApplicationStatus {
    /** The application has been submitted and is awaiting review. */
    PENDING("Pending"),
     /** The application has been approved by the career center staff. */
    APPROVED("Approved"),
    /** The company representative has accepted the student. */
    ACCEPTED_BY_COMPANY_REPRESENTATIVE("Accepted by Company Representative"),
    /** The student has accepted the internship offer. */
    ACCEPTED_BY_STUDENT("Accepted by Student"),
    /** The student has requested to withdraw and awaits staff review. */
    PENDING_WITHDRAWAL("Pending Withdrawal"),
    /** The application has been withdrawn. */
    WITHDRAWN("Withdrawn"),
    /** The application was removed because the internship was deleted. */
    DELETED("Deleted");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

     /** Returns the user-friendly display name for this status. */
    @Override
    public String toString() {
        return displayName;
    }
}
