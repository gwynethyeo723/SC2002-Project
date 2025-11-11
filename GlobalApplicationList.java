import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalApplicationList {
    private static List<Application> applications = new ArrayList<>();

    // Add an application to the global list
    public static void addApplication(Application application) {
        if (!applications.contains(application)) {
            applications.add(application);
        }
    }

    // Remove an application (if needed)
    public static void removeApplication(Application application) {
        applications.remove(application);
    }

    // Get all applications (unmodifiable)
    public static List<Application> getAll() {
        return Collections.unmodifiableList(applications);
    }

    // Filter applications by internship
    public static List<Application> getByInternship(Internship internship) {
        return applications.stream()
                .filter(app -> app.getInternship().equals(internship))
                .collect(Collectors.toList());
    }

    // Filter applications by student
    public static List<Application> getByStudent(Student student) {
        return applications.stream()
                .filter(app -> app.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    // Filter applications by status
    public static List<Application> getByStatus(ApplicationStatus status) {
        return applications.stream()
                .filter(app -> app.getStatus() == status)
                .collect(Collectors.toList());
    }

    // Filter applications by internship and student
    public static List<Application> getByInternshipAndStudent(Internship internship, Student student) {
        return applications.stream()
                .filter(a -> a.getInternship().equals(internship) && a.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    // Clear all applications (useful for testing)
    public static void clear() {
        applications.clear();
    }
}
