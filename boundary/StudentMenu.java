import java.util.List;
import java.util.Scanner;

import entity.Student;
import entity.Internship;
import controller.UserController;
import database.GlobalInternshipList;
import controller.InternshipController;
import controller.ApplicationController;
import entity.Application;
import database.GlobalApplicationList;
import enumeration.ApplicationStatus;

/**
 * Displays the main menu for a logged-in {@link Student} and processes
 * the selected actions. This menu allows the student to browse internships,
 * submit applications, manage their accepted internship, and update their
 * account information.
 *
 * <p>This method runs continuously in a loop until the student chooses
 * the <b>Logout</b> option. For each menu selection, the corresponding
 * controller method is invoked. The available functions include:
 *
 * <ul>
 *     <li>viewing all available internships</li>
 *     <li>applying for internships</li>
 *     <li>accepting internships after approval</li>
 *     <li>requesting withdrawal from an internship</li>
 *     <li>viewing all submitted applications and their statuses</li>
 *     <li>viewing the internship accepted by the student</li>
 *     <li>changing the student's account password</li>
 * </ul>
 *
 * <p>This method does not return a value. The loop exits only when
 * {@link controller.UserController#logout(entity.User)} is invoked.
 *
 * @param student the logged-in student using this menu
 */

public class StudentMenu {
    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu(Student student) {
        if (!student.isLoggedIn()) {
            System.out.println("You must be logged in to access the student menu.");
            return;
        }

        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View available internships");
            System.out.println("2. Apply for internship");
            System.out.println("3. Accept internship");
            System.out.println("4. Request withdrawal");
            System.out.println("5. View applied internships");
            System.out.println("6. View accepted internship");
            System.out.println("7. Change password");
            System.out.println("8. Logout");

            int choice;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); 
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 8.");
                sc.next(); 
                continue; 
            }

            switch (choice) {
                case 1 -> {
                    List<Internship> available = InternshipController.viewAvailableInternships(student);
                    available.forEach(i -> System.out.println(i.getTitle() + " | " + i.getCompany().getName()));
                }
                case 2 -> {
                    System.out.print("Enter internship title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter company name: ");
                    String companyName = sc.nextLine();

                    Internship internship = GlobalInternshipList.getAll().stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(title))
                            .filter(i -> i.getCompany().getName().equalsIgnoreCase(companyName))
                            .findFirst().orElse(null);

                    if (internship != null)
                        ApplicationController.createApplication(student, internship);
                    else
                        System.out.println("Internship not found.");
                }
                case 3 -> {
                    System.out.print("Enter internship title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter company name: ");
                    String companyName = sc.nextLine();

                    Internship internship = GlobalInternshipList.getAll().stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(title))
                            .filter(i -> i.getCompany().getName().equalsIgnoreCase(companyName))
                            .findFirst().orElse(null);

                    if (internship != null)
                        ApplicationController.acceptInternship(student, internship);
                    else
                        System.out.println("Internship not found.");
                }
                case 4 -> {
                    System.out.print("Enter internship title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter company name: ");
                    String companyName = sc.nextLine();

                    Internship internship = GlobalInternshipList.getAll().stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(title))
                            .filter(i -> i.getCompany().getName().equalsIgnoreCase(companyName))
                            .findFirst().orElse(null);

                    if (internship != null)
                        ApplicationController.requestWithdrawal(student, internship);
                    else
                        System.out.println("Internship not found.");
                }
                case 5 -> {
                    List<Application> applications = GlobalApplicationList.getByStudent(student);
                    if (applications.isEmpty()) {
                        System.out.println("No applied internships.");
                    } else {
                        System.out.println("Applied Internships:");
                        applications.forEach(app -> System.out.println(
                            app.getInternship().getTitle() + " | " 
                            + app.getInternship().getCompany().getName() + " | Status: " 
                            + app.getStatus()
                        ));
                    }
                }

                case 6 -> {
                    GlobalApplicationList.getByStudent(student).stream()
                        .filter(a -> a.getStatus() == ApplicationStatus.ACCEPTED_BY_STUDENT)
                        .findFirst()
                        .ifPresentOrElse(
                            app -> System.out.println(app.getInternship().getTitle() + " | " 
                                                    + app.getInternship().getCompany().getName()),
                            () -> System.out.println("No accepted internship yet.")
                        );
                }
                case 7 -> {
                    System.out.print("Enter old password: ");
                    String oldPass = sc.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = sc.nextLine();
                    boolean success = UserController.changePassword(student, oldPass, newPass);
                    System.out.println(success ? "Password changed successfully." : "Password change failed.");
                }
                case 8 -> {
                    UserController.logout(student);
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
