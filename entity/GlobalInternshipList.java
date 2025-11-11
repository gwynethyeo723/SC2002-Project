package entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import enumeration.InternshipStatus;

public class GlobalInternshipList {
    private static List<Internship> internships = new ArrayList<>();

    // Add an internship to the global list
    public static void addInternship(Internship internship) {
        if (!internships.contains(internship)) {
            internships.add(internship);
        }
    }

    // Remove an internship from the global list
    public static void removeInternship(Internship internship) {
        internships.remove(internship);
    }

    // Get all internships (unmodifiable to prevent external modification)
    public static List<Internship> getAll() {
        return Collections.unmodifiableList(internships);
    }

    // Get internships created by a specific company representative
    public static List<Internship> getByCompanyRep(CompanyRep rep) {
        return Collections.unmodifiableList(
            internships.stream()
                       .filter(i -> i.getRepresentative().equals(rep))
                       .collect(Collectors.toList())
        );
    }

    // Get internships offered by a specific company
    public static List<Internship> getByCompany(Company company) {
        return Collections.unmodifiableList(
            internships.stream()
                       .filter(i -> i.getCompany().equals(company))
                       .collect(Collectors.toList())
        );
    }

    // Get only approved internships
    public static List<Internship> getApprovedInternships() {
        return Collections.unmodifiableList(
            internships.stream()
                       .filter(i -> i.getStatus() == InternshipStatus.APPROVED || i.getStatus() == InternshipStatus.FILLED)
                       .collect(Collectors.toList())
        );
    }

    // Clear the list (useful for testing)
    public static void clear() {
        internships.clear();
    }
}
