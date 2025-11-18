package database;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import entity.Company;
import entity.CompanyRep;
import entity.Internship;
import enumeration.InternshipStatus;

/**
 * Stores and manages all {@link Internship} objects in the system.
 * <p>
 * Acts as an in-memory database that supports adding, removing, retrieving,
 * and filtering internships based on representative, company, or approval
 * status.
 */

public class GlobalInternshipList {
    private static List<Internship> internships = new ArrayList<>();

    /**
     * Adds an internship to the global list if it is not already present.
     *
     * @param internship the {@link Internship} to add
     */

    // Add an internship to the global list
    public static void addInternship(Internship internship) {
        if (!internships.contains(internship)) {
            internships.add(internship);
        }
    }

    /**
     * Removes an internship from the global list.
     *
     * @param internship the {@link Internship} to remove
     */
    // Remove an internship from the global list
    public static void removeInternship(Internship internship) {
        internships.remove(internship);
    }

    /**
     * Returns an unmodifiable list of all internships stored in the system.
     *
     * @return list of all {@link Internship} objects
     */
    // Get all internships (unmodifiable to prevent external modification)
    public static List<Internship> getAll() {
        return Collections.unmodifiableList(internships);
    }

    /**
     * Retrieves all internships created by the specified company representative.
     *
     * @param rep the {@link CompanyRep} responsible for the internship(s)
     * @return list of internships created by the representative
     */
    // Get internships created by a specific company representative
    public static List<Internship> getByCompanyRep(CompanyRep rep) {
        return Collections.unmodifiableList(
            internships.stream()
                       .filter(i -> i.getRepresentative().equals(rep))
                       .collect(Collectors.toList())
        );
    }

    /**
     * Retrieves all internships offered by the specified company.
     *
     * @param company the {@link Company} offering the internship(s)
     * @return list of internships offered by the company
     */
    // Get internships offered by a specific company
    public static List<Internship> getByCompany(Company company) {
        return Collections.unmodifiableList(
            internships.stream()
                       .filter(i -> i.getCompany().equals(company))
                       .collect(Collectors.toList())
        );
    }

    /**
     * Retrieves all internships that are approved or filled.
     *
     * @return list of approved or filled internships
     */
    // Get only approved internships
    public static List<Internship> getApprovedInternships() {
        return Collections.unmodifiableList(
            internships.stream()
                       .filter(i -> i.getStatus() == InternshipStatus.APPROVED || i.getStatus() == InternshipStatus.FILLED)
                       .collect(Collectors.toList())
        );
    }

    /**
     * Removes all internships from the list.
     * Intended for testing purposes.
     */
    // Clear the list (useful for testing)
    public static void clear() {
        internships.clear();
    }
}
