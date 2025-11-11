public enum ApplicationStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    ACCEPTED_BY_CAREER_STAFF("Accepted by Career Staff"),
    ACCEPTED_BY_COMPANY_REPRESENTATIVE("Accepted by Company Representative"),
    PENDING_WITHDRAWAL("Pending Withdrawal"),
    WITHDRAWN("Withdrawn");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
