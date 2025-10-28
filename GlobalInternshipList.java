import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalInternshipList {
    private static List<Internship> internships = new ArrayList<>();

    // Add an internship to the global list
    public static void addInternship(Internship internship) {
        internships.add(internship);
    }

    // Remove an internship from the global list
    public static void removeInternship(Internship internship) {
        internships.remove(internship);
    }

    // Get all internships (unmodifiable to prevent external modification)
    public static List<Internship> getAll() {
        return Collections.unmodifiableList(internships);
    }

    // Optional: clear the list (useful for testing)
    public static void clear() {
        internships.clear();
    }
}