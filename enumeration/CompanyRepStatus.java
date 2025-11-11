package enumeration;
public enum CompanyRepStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    CompanyRepStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
