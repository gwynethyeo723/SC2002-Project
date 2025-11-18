import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import entity.Application;
import entity.CompanyRep;
import controller.InternshipController;
import controller.ApplicationController;
import enumeration.InternshipLevel;
import entity.Internship;
import entity.Student;
import controller.UserController;
import database.GlobalApplicationList;
import database.GlobalInternshipList;

/**
 * Displays the main menu for a logged-in {@link CompanyRep} and processes
 * the selected actions. This menu allows the representative to manage their
 * internships and account settings in a text-based interface.
 *
 * <p>This method runs in a loop until the representative chooses the
 * <b>Logout</b> option. For each selection, the appropriate controller is
 * invoked to perform the requested operation. The available functions include:
 * <ul>
 *     <li>creating new internships</li>
 *     <li>editing existing internships</li>
 *     <li>deleting internships</li>
 *     <li>toggling internship visibility</li>
 *     <li>viewing all internships created by the representative</li>
 *     <li>accepting applicants for an internship</li>
 *     <li>changing the representative's account password</li>
 * </ul>
 *
 * <p>This method does not return a value. The menu continues running until
 * {@link controller.UserController#logout(entity.User)} is invoked.
 *
 * @param rep the logged-in company representative using this menu
 */

public class CompanyRepMenu {

    private static final Scanner sc = new Scanner(System.in);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void showMenu(CompanyRep rep) {
        while (true) {
            System.out.println("\n--- CompanyRep Menu ---");
            System.out.println("1. Create internship");
            System.out.println("2. Edit internship");
            System.out.println("3. Delete internship");
            System.out.println("4. Toggle internship visibility");
            System.out.println("5. View my internships");
            System.out.println("6. Accept applicant"); 
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
                    System.out.print("Title: "); String title = sc.nextLine();
                    System.out.print("Description: "); String desc = sc.nextLine();
                    System.out.print("Level (Basic/Intermediate/Advanced): "); String level = sc.nextLine();
                    System.out.print("Preferred Major: "); String major = sc.nextLine();
                    System.out.print("Slots: "); int slots = sc.nextInt(); sc.nextLine();
                    System.out.print("Opening Date (yyyy-MM-dd): "); Date open = parseDate(sc.nextLine());
                    System.out.print("Closing Date (yyyy-MM-dd): "); Date close = parseDate(sc.nextLine());
                    InternshipController.createInternship( title, desc, rep.getCompany(), rep,
                            InternshipLevel.valueOf(level.toUpperCase()), major, slots, open, close);
                }
                case 2 -> {
                    System.out.print("Enter internship title to edit: "); String titleEdit = sc.nextLine();
                    Internship internship = GlobalInternshipList.getByCompanyRep(rep).stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(titleEdit))
                            .findFirst().orElse(null);
                    if (internship != null) {
                        System.out.print("New Title: "); String newTitle = sc.nextLine();
                        System.out.print("New Description: "); String newDesc = sc.nextLine();
                        System.out.print("New Level: "); String newLevel = sc.nextLine();
                        System.out.print("New Preferred Major: "); String newMajor = sc.nextLine();
                        System.out.print("New Slots: "); int newSlots = sc.nextInt(); sc.nextLine();
                        System.out.print("New Opening Date (yyyy-MM-dd): "); Date newOpen = parseDate(sc.nextLine());
                        System.out.print("New Closing Date (yyyy-MM-dd): "); Date newClose = parseDate(sc.nextLine());
                        InternshipController.editInternship(rep, internship, newTitle, newDesc,
                                InternshipLevel.valueOf(newLevel.toUpperCase()), newMajor, newSlots, newOpen, newClose);
                    } else System.out.println("Internship not found.");
                }
                case 3 -> {
                    System.out.print("Enter internship title to delete: "); String titleDel = sc.nextLine();
                    Internship internship = GlobalInternshipList.getByCompanyRep(rep).stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(titleDel))
                            .findFirst().orElse(null);
                    if (internship != null) InternshipController.deleteInternship(rep, internship);
                    else System.out.println("Internship not found.");
                }
                case 4 -> {
                    System.out.print("Enter internship title to toggle visibility: "); String titleVis = sc.nextLine();
                    Internship internship = GlobalInternshipList.getByCompanyRep(rep).stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(titleVis))
                            .findFirst().orElse(null);
                    if (internship != null) {
                        internship.setVisibility(!internship.getVisibility());
                        System.out.println("Internship visibility toggled.");
                    } else System.out.println("Internship not found.");
                }
                case 5 -> {
                    List<Internship> internships = GlobalInternshipList.getByCompanyRep(rep);
                    if (internships.isEmpty()) System.out.println("No internships found.");
                    else internships.forEach(i ->
                            System.out.println(i.getTitle() + " | " + i.getCompany().getName() + " | Status: " + i.getStatus()));
                }
                case 6 -> {
                    System.out.print("Enter internship title: ");
                    String internshipTitle = sc.nextLine();
                    System.out.print("Enter student username: ");
                    String studentUsername = sc.nextLine();
                    Internship internship = GlobalInternshipList.getByCompanyRep(rep).stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(internshipTitle))
                            .findFirst()
                            .orElse(null);
                    if (internship == null) {
                        System.out.println("Internship not found.");
                        break;
                    }
                    Application application = GlobalApplicationList.getByInternship(internship).stream()
                            .filter(app -> app.getStudent().getUserId().equalsIgnoreCase(studentUsername))
                            .findFirst()
                            .orElse(null);
                    if (application == null) {
                        System.out.println("No application found for this student on this internship.");
                        break;
                    }
                    Student student = application.getStudent();
                    ApplicationController.acceptApplicationByCompanyRep(rep, internship, student);
                }
                case 7 -> {
                    System.out.print("Enter old password: "); String oldPass = sc.nextLine();
                    System.out.print("Enter new password: "); String newPass = sc.nextLine();
                    if (UserController.changePassword(rep,oldPass, newPass)) System.out.println("Password changed successfully.");
                    else System.out.println("Password change failed.");
                }
                case 8 -> {
                    UserController.logout(rep);
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static Date parseDate(String input) {
        try {
            return sdf.parse(input);
        } catch (Exception e) {
            System.out.println("Invalid date format. Using today as fallback.");
            return new Date();
        }
    }
}
