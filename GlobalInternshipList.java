import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    // Optional: get only approved internships
    public static List<Internship> getApprovedInternships() {
        List<Internship> approved = new ArrayList<>();
        for (Internship i : internships) {
            if (i.getStatus() == InternshipStatus.APPROVED || i.getStatus() == InternshipStatus.FILLED) {
                approved.add(i);
            }
        }
        return Collections.unmodifiableList(approved);
    }

    // Optional: clear the list (useful for testing)
    public static void clear() {
        internships.clear();
    }
}