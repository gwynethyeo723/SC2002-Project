import java.util.List;
import java.util.Scanner;

import controller.AccountsController;
import controller.ApplicationController;
import controller.InternshipController;
import controller.UserController;
import entity.CareerCenterStaff;
import entity.CompanyRep;
import database.GlobalInternshipList;
import entity.Internship;
import entity.Student;
import entity.User;

public class CareerStaffMenu {

    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu(CareerCenterStaff staff, List<User> users) {
        while (true) {
            System.out.println("\n--- Career Staff Menu ---");
            System.out.println("1. Review company rep account");
            System.out.println("2. Review internship");
            System.out.println("3. Review student withdrawal");
            System.out.println("4. Generate internship report");
            System.out.println("5. Change password");
            System.out.println("6. Logout");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter CompanyRep email: ");
                    String email = sc.nextLine();
                    User u = users.stream()
                            .filter(user -> user instanceof CompanyRep)
                            .filter(user -> user.getUserId().equals(email))
                            .findFirst().orElse(null);
                    if (u != null) {
                        System.out.print("Approve or reject? (1=Approve, 2=Reject): ");
                        int approve = sc.nextInt(); sc.nextLine();
                        AccountsController.reviewCompanyRep((CompanyRep) u,staff, approve == 1);
                    } else {
                        System.out.println("CompanyRep not found.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter internship title: ");
                    String inputTitle = sc.nextLine().trim();
                    System.out.print("Enter company name: ");
                    String inputCompany = sc.nextLine().trim();

                
                    Internship internship = GlobalInternshipList.getAll().stream()
                        .filter(i -> i.getTitle().trim().equalsIgnoreCase(inputTitle))
                        .filter(i -> i.getCompany().getName().trim().equalsIgnoreCase(inputCompany))
                        .findFirst()
                        .orElse(null);

                    if (internship != null) {
                        System.out.print("Approve or reject? (1=Approve, 2=Reject): ");
                        int approve1 = sc.nextInt(); sc.nextLine();
                        boolean approve2 = approve1 == 1; // 1 = approve, 2 = reject
                        InternshipController.reviewInternship(staff, internship, approve2);
                        if (approve2) {
                            System.out.println("Internship '" + internship.getTitle() + "' at '" + internship.getCompany().getName() + "' has been approved.");
                        } else {
                            System.out.println("Internship '" + internship.getTitle() + "' at '" + internship.getCompany().getName() + "' has been rejected.");
                        }
                    }else {
                        System.out.println("Internship not found.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter student ID: ");
                    String studentId = sc.nextLine();
                    User u = users.stream()
                            .filter(user -> user instanceof Student)
                            .filter(user -> user.getUserId().equals(studentId))
                            .findFirst().orElse(null);

                    if (u != null) {
                        Student s = (Student) u;
                        System.out.print("Enter internship title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter company name: ");
                        String companyName = sc.nextLine();

                        Internship internship = GlobalInternshipList.getAll().stream()
                                .filter(i -> i.getTitle().equalsIgnoreCase(title))
                                .filter(i -> i.getCompany().getName().equalsIgnoreCase(companyName))
                                .findFirst().orElse(null);

                        if (internship != null) {
                            System.out.print("Approve or reject withdrawal? (1=Approve, 2=Reject): ");
                            int option = sc.nextInt();
                            sc.nextLine();
                            boolean approve = (option == 1);
                            ApplicationController.reviewWithdrawal(staff,s, internship, approve);
                        } else {
                            System.out.println("Internship not found.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                }
                case 4 -> {
                    System.out.print("Filter by status (or leave empty): ");
                    String status = sc.nextLine();
                    System.out.print("Filter by major (or leave empty): ");
                    String major = sc.nextLine();
                    System.out.print("Filter by level (or leave empty): ");
                    String level = sc.nextLine();
                    InternshipController.generateInternshipReport(
                            staff,
                            status.isBlank() ? null : status,
                            major.isBlank() ? null : major,
                            level.isBlank() ? null : level
                    );
                }
                case 5 -> {
                    System.out.print("Enter old password: ");
                    String oldPass = sc.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = sc.nextLine();
                    boolean success = UserController.changePassword(staff, oldPass, newPass);
                    System.out.println(success ? "Password changed successfully." : "Password change failed.");
                }
                case 6 -> {
                    UserController.logout(staff);
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
