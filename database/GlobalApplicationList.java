package database;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import enumeration.ApplicationStatus;
import entity.Application;
import entity.Internship;
import entity.Student;

/**
 * Stores and manages all {@link Application} objects in the system.
 * <p>
 * Acts as an in-memory database that provides operations for adding,
 * removing, retrieving, and filtering applications based on different
 * criteria such as internship, student, and application status.
 */

public class GlobalApplicationList {
    private static List<Application> applications = new ArrayList<>();

    /**
     * Adds an application to the global list if it is not already present.
     *
     * @param application the {@link Application} to add
     */
    // Add an application to the global list
    public static void addApplication(Application application) {
        if (!applications.contains(application)) {
            applications.add(application);
        }
    }

    /**
     * Removes an application from the global list.
     *
     * @param application the {@link Application} to remove
     */
    // Remove an application (if needed)
    public static void removeApplication(Application application) {
        applications.remove(application);
    }

    /**
     * Returns an unmodifiable list of all applications stored in the system.
     *
     * @return list of all {@link Application} objects
     */
    // Get all applications (unmodifiable)
    public static List<Application> getAll() {
        return Collections.unmodifiableList(applications);
    }

    /**
     * Retrieves all applications submitted for the specified internship.
     *
     * @param internship the {@link Internship} to filter by
     * @return list of matching applications
     */
    // Filter applications by internship
    public static List<Application> getByInternship(Internship internship) {
        return applications.stream()
                .filter(app -> app.getInternship().equals(internship))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all applications submitted by the specified student.
     *
     * @param student the {@link Student} to filter by
     * @return list of matching applications
     */
    // Filter applications by student
    public static List<Application> getByStudent(Student student) {
        return applications.stream()
                .filter(app -> app.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all applications with the specified status.
     *
     * @param status the {@link ApplicationStatus} to filter by
     * @return list of matching applications
     */
    // Filter applications by status
    public static List<Application> getByStatus(ApplicationStatus status) {
        return applications.stream()
                .filter(app -> app.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all applications for a specific internship made by a specific student.
     *
     * @param internship the internship to filter by
     * @param student    the student who submitted the application(s)
     * @return list of matching applications
     */
    // Filter applications by internship and student
    public static List<Application> getByInternshipAndStudent(Internship internship, Student student) {
        return applications.stream()
                .filter(a -> a.getInternship().equals(internship) && a.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    /**
     * Removes all applications from the list.
     * Intended for testing purposes.
     */
    // Clear all applications (useful for testing)
    public static void clear() {
        applications.clear();
    }
}
