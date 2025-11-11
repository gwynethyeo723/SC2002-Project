import java.io.*;
import java.util.*;

import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.GlobalInternshipList;
import entity.Internship;
import entity.Student;
import entity.User;
import entity.Company;

import java.text.SimpleDateFormat;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    static Map<String, Company> companyMap = new HashMap<>(); // temporary map for companies
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        // Load data from CSV
        loadCompanyReps("company_rep_list.csv", users, companyMap);
        loadCareerStaff("staff_list.csv");
        loadStudents("student_list.csv");

        while (true) {
            System.out.println("=== Internship Portal ===");
            System.out.println("Enter User ID: ");
            String id = sc.nextLine();
            System.out.println("Enter Password: ");
            String password = sc.nextLine();

            User loggedInUser = users.stream()
                    .filter(u -> u.getUserId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (loggedInUser == null) {
                System.out.println("User not found.");
                continue;
            }

            if (!loggedInUser.login(id, password)) {
                continue;
            }

            // Show menu based on role
            if (loggedInUser instanceof Student student) {
                studentMenu(student);
            } else if (loggedInUser instanceof CareerCenterStaff staff) {
                staffMenu(staff);
            } else if (loggedInUser instanceof CompanyRep rep) {
                repMenu(rep);
            }
        }
    }

    // ================== CSV Loaders ==================
    private static void loadCompanyReps(String path, List<User> users, Map<String, Company> companyMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String email = parts[0].trim();
                String name = parts[1].trim();
                String companyName = parts[2].trim();
                String department = parts[3].trim();
                String position = parts[4].trim();

                Company company = companyMap.get(companyName);
                if (company == null) {
                    company = new Company(companyName);
                    companyMap.put(companyName, company);
                }

                users.add(new CompanyRep(email, name, company, department, position));
            }
        } catch (IOException e) {
            System.out.println("Error loading company reps: " + e.getMessage());
        }
    }
    private static void loadCareerStaff(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String userId = tokens[0].trim();
                String name = tokens[1].trim();
                String department = tokens[2].trim();

                CareerCenterStaff staff = new CareerCenterStaff(userId, name, department);
                users.add(staff);
            }
            System.out.println("Career staff loaded.");
        } catch (Exception e) {
            System.out.println("Error loading career staff: " + e.getMessage());
        }
    }

    private static void loadStudents(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String userId = tokens[0].trim();
                String name = tokens[1].trim();
                int year = Integer.parseInt(tokens[2].trim());
                String major = tokens[3].trim();

                Student student = new Student(userId, name, year, major);
                users.add(student);
            }
            System.out.println("Students loaded.");
        } catch (Exception e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    // ================== Menus ==================
    private static void studentMenu(Student student) {
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
                case 1 -> student.viewAvailableInternships()
                        .forEach(i -> System.out.println(i.getTitle() + " | " + i.getCompany().getName()));
                case 2 -> {
                    System.out.print("Enter internship title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter company name: ");
                    String companyName = sc.nextLine();

                    Internship internship = GlobalInternshipList.getAll().stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(title))
                            .filter(i -> i.getCompany().getName().equalsIgnoreCase(companyName))
                            .findFirst().orElse(null);
                    if (internship != null) student.apply(internship);
                    else System.out.println("Internship not found.");
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
                    if (internship != null) student.acceptInternship(internship);
                    else System.out.println("Internship not found.");
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
                    if (internship != null) student.requestWithdrawal(internship);
                    else System.out.println("Internship not found.");
                }
                case 5 -> {
                    System.out.println("Applied Internships:");
                    student.getAppliedInternships().keySet().forEach(i -> 
                        System.out.println(i.getTitle() + " | " + i.getCompany().getName() + " | Status: " 
                            + student.getAppliedInternships().get(i))
                    );
                }
                case 6 -> {
                    Internship accepted = student.getAcceptedInternship();
                    if (accepted != null)
                        System.out.println(accepted.getTitle() + " | " + accepted.getCompany().getName());
                    else System.out.println("No accepted internship yet.");
                }
                case 7 -> {
                    System.out.print("Enter old password: ");
                    String oldPass = sc.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = sc.nextLine();
                    if (student.changePassword(oldPass, newPass))
                        System.out.println("Password changed successfully.");
                    else System.out.println("Password change failed.");
                }
                case 8 -> {
                    student.logout();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void staffMenu(CareerCenterStaff staff) {
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
                        System.out.println("Approve or reject? (1=Approve, 2=Reject): ");
                        int approve = sc.nextInt(); sc.nextLine();
                        staff.reviewCompanyRep((CompanyRep) u, approve == 1);
                    } else System.out.println("CompanyRep not found.");
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
                    if (internship != null) {
                        System.out.println("Approve or reject? (1=Approve, 2=Reject): ");
                        int approve = sc.nextInt(); sc.nextLine();
                        staff.reviewInternship(internship, approve == 1);
                    } else System.out.println("Internship not found.");
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
                        if (internship != null)
                        {
                            System.out.println("Approve or reject withdrawal? (1=Approve, 2=Reject): ");
                            int approve = sc.nextInt(); sc.nextLine();
                            staff.reviewWithdrawal(s, internship, approve == 1);
                        } else System.out.println("Internship not found.");
                    } else System.out.println("Student not found.");
                }
                case 4 -> {
                    System.out.print("Filter by status (or leave empty): ");
                    String status = sc.nextLine();
                    System.out.print("Filter by major (or leave empty): ");
                    String major = sc.nextLine();
                    System.out.print("Filter by level (or leave empty): ");
                    String level = sc.nextLine();
                    staff.generateInternshipReport(status.isBlank() ? null : status,
                            major.isBlank() ? null : major,
                            level.isBlank() ? null : level);
                }
                case 5 -> {
                    System.out.print("Enter old password: ");
                    String oldPass = sc.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = sc.nextLine();
                    if (staff.changePassword(oldPass, newPass))
                        System.out.println("Password changed successfully.");
                    else System.out.println("Password change failed.");
                }
                case 6 -> {
                    staff.logout();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void repMenu(CompanyRep rep) {
        while (true) {
            System.out.println("\n--- CompanyRep Menu ---");
            System.out.println("1. Create internship");
            System.out.println("2. Edit internship");
            System.out.println("3. Delete internship");
            System.out.println("4. Toggle internship visibility");
            System.out.println("5. View my internships");
            System.out.println("6. Change password");
            System.out.println("7. Logout");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Title: "); String title = sc.nextLine();
                    System.out.print("Description: "); String desc = sc.nextLine();
                    System.out.print("Level (Basic/Intermediate/Advanced): "); String level = sc.nextLine();
                    System.out.print("Preferred Major: "); String major = sc.nextLine();
                    System.out.print("Slots: "); int slots = sc.nextInt(); sc.nextLine();
                    System.out.print("Opening Date (yyyy-MM-dd): "); 
                    Date open = parseDate(sc.nextLine());
                    System.out.print("Closing Date (yyyy-MM-dd): "); 
                    Date close = parseDate(sc.nextLine());
                    rep.createInternship(title, desc, level, major, slots, open, close);
                }
                case 2 -> {
                    System.out.print("Enter internship title to edit: "); 
                    String titleEdit = sc.nextLine();
                    System.out.print("Enter company name: "); 
                    String companyName = sc.nextLine();

                    Internship internship = GlobalInternshipList.getAll().stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(titleEdit))
                            .filter(i -> i.getCompany().getName().equalsIgnoreCase(companyName))
                            .findFirst().orElse(null);

                    if (internship != null) {
                        System.out.print("New Title: "); String newTitle = sc.nextLine();
                        System.out.print("New Description: "); String newDesc = sc.nextLine();
                        System.out.print("New Level: "); String newLevel = sc.nextLine();
                        System.out.print("New Preferred Major: "); String newMajor = sc.nextLine();
                        System.out.print("New Slots: "); int newSlots = sc.nextInt(); sc.nextLine();
                        System.out.print("New Opening Date (yyyy-MM-dd): "); Date newOpen = parseDate(sc.nextLine());
                        System.out.print("New Closing Date (yyyy-MM-dd): "); Date newClose = parseDate(sc.nextLine());
                        rep.editInternship(internship, newTitle, newDesc, newLevel, newMajor, newSlots, newOpen, newClose);
                    } else System.out.println("Internship not found.");
                }
                case 3 -> {
                    System.out.print("Enter internship title to delete: ");
                    String titleDel = sc.nextLine();
                    System.out.print("Enter company name: "); 
                    String companyName = sc.nextLine();
                    Internship internship = GlobalInternshipList.getAll().stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(titleDel))
                            .filter(i -> i.getCompany().getName().equalsIgnoreCase(companyName))
                            .findFirst().orElse(null);
                    if (internship != null) rep.deleteInternship(internship);
                    else System.out.println("Internship not found.");
                }
                case 4 -> {
                    System.out.print("Enter internship title to toggle visibility: ");
                    String titleVis = sc.nextLine();
                    System.out.print("Enter company name: "); 
                    String companyName = sc.nextLine();
                    Internship internship = GlobalInternshipList.getAll().stream()
                            .filter(i -> i.getTitle().equalsIgnoreCase(titleVis))
                            .filter(i -> i.getCompany().getName().equalsIgnoreCase(companyName))
                            .findFirst().orElse(null);
                    if (internship != null) rep.toggleInternshipVisibility(internship);
                    else System.out.println("Internship not found.");
                }
                case 5 -> rep.viewInternships();
                case 6 -> {
                    System.out.print("Enter old password: ");
                    String oldPass = sc.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = sc.nextLine();
                    if (rep.changePassword(oldPass, newPass))
                        System.out.println("Password changed successfully.");
                    else System.out.println("Password change failed.");
                }
                case 7 -> {
                    rep.logout();
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
