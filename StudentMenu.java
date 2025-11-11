import java.util.List;
import java.util.Scanner;

public class StudentMenu {
    private final Student student;
    private final Scanner sc;

    public StudentMenu(Student student, Scanner sc) {
        this.student = student;
        this.sc = sc;
    }

    public void display() {
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

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

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
