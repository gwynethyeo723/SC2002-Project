import java.io.*;
import java.util.*;

import database.GlobalInternshipList;
import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.Internship;
import entity.Student;
import entity.User;
import enumeration.InternshipLevel;
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
        loadInternships("internship_list.csv", users, companyMap);

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
                StudentMenu.showMenu(student);
            } else if (loggedInUser instanceof CareerCenterStaff staff) {
                CareerStaffMenu.showMenu(staff, users);
            } else if (loggedInUser instanceof CompanyRep rep) {
                CompanyRepMenu.showMenu(rep);
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

    private static void loadInternships(String filePath, List<User> users, Map<String, Company> companyMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",", -1); // handle empty values safely
                if (tokens.length < 9) continue;

                String title = tokens[0].trim();
                String description = tokens[1].trim();
                String companyName = tokens[2].trim();
                String companyRepId = tokens[3].trim();
                String levelStr = tokens[4].trim();
                String preferredMajor = tokens[5].trim();
                int totalSlots = Integer.parseInt(tokens[6].trim());
                Date openingDate = sdf.parse(tokens[7].trim());
                Date closingDate = sdf.parse(tokens[8].trim());

                Company company = companyMap.get(companyName);
                if (company == null) {
                    company = new Company(companyName);
                    companyMap.put(companyName, company);
                }

                InternshipLevel level;
                    try {
                        level = InternshipLevel.valueOf(levelStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid internship level '" + levelStr + "' for " + title + ". Skipping...");
                        continue;
                    }

                CompanyRep rep = users.stream()
                        .filter(u -> u instanceof CompanyRep)
                        .map(u -> (CompanyRep) u)
                        .filter(r -> r.getUserId().equalsIgnoreCase(companyRepId))
                        .findFirst()
                        .orElse(null);

                Internship internship = new Internship(title, description, company,rep, level, preferredMajor, totalSlots, openingDate, closingDate);
                internship.setRepresentative(rep);
                GlobalInternshipList.addInternship(internship);
            }
            System.out.println("Internships loaded.");
        } catch (Exception e) {
            System.out.println("Error loading internships: " + e.getMessage());
        }
    }
}
