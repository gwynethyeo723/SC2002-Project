package enumeration;
/**
 * Represents the approval status of a company representative's account.
 * <p>
 * Each status includes a display name used for user-friendly output.
 */
public enum CompanyRepStatus {
    /** The account has been created and is awaiting approval. */
    PENDING("Pending"),
    /** The account has been reviewed and approved by career center staff. */
    APPROVED("Approved"),
    /** The account request has been rejected. */
    REJECTED("Rejected");

    private final String displayName;

    CompanyRepStatus(String displayName) {
        this.displayName = displayName;
    }
    
    /** Returns the user-friendly display name of this status. */
    @Override
    public String toString() {
        return displayName;
    }
}
