package entity;

/**
 * Represents a company offering internships in the system.
 * <p>
 * Stores basic company information such as the company name.
 */
public class Company {
    private String name;

    /**
     * Creates a new company with the specified name.
     *
     * @param name the company's name
     */
    public Company(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
