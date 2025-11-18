package enumeration;

/**
 * Represents the difficulty or seniority level of an internship.
 * <p>
 * Each level includes a display name used for user-friendly output.
 */
public enum InternshipLevel {
    
    /** Entry-level internship suitable for lower-year students. */
    BASIC("Basic"),
    /** Mid-level internship requiring moderate experience or knowledge. */
    INTERMEDIATE("Intermediate"),
    /** Advanced internship requiring higher expertise or senior standing. */
    ADVANCED("Advanced");

    private final String displayName;

    InternshipLevel(String displayName) {
        this.displayName = displayName;
    }

    /** Returns the user-friendly display name of this level. */
    @Override
    public String toString() {
        return displayName;
    }
}
