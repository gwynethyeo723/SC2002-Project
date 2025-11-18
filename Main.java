import java.io.*;
import java.util.*;

import controller.UserController;
import database.GlobalInternshipList;
import entity.CareerCenterStaff;
import entity.CompanyRep;
import entity.Internship;
import entity.Student;
import entity.User;
import enumeration.InternshipLevel;
import entity.Company;
import java.text.SimpleDateFormat;

/**
 * Entry point of the Internship Portal application.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Loading initial data from CSV files</li>
 *   <li>Authenticating users</li>
 *   <li>Routing users to the appropriate menu based on their role</li>
 * </ul>
 */
public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    static Map<String, Company> companyMap = new HashMap<>(); // temporary map for companies
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Starts the application by loading data and prompting for user login.
     * Launches the appropriate menu after successful authentication.
     *
     * @param args command-line arguments (not used)
     */
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

            User loggedInUser = UserController.login(users, id, password);
            if (loggedInUser == null) {
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

    /**
     * Splits a CSV line while preserving values enclosed in quotes.
     *
     * @param line a raw CSV line
     * @return an array of trimmed column values
     */
    private static String[] splitCSV(String line) {
        List<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
            inQuotes = !inQuotes; // toggle quote state
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

    tokens.add(sb.toString().trim());
    return tokens.toArray(new String[0]);
}

    // ================== CSV Loaders ==================
    /**
     * Loads company representative records from a CSV file and adds them to the user list.
     *
     * @param path      file path of the CSV
     * @param users     list to store loaded users
     * @param companyMap map to store and reuse {@link Company} objects
     */
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
    /**
     * Loads career center staff data from a CSV file and adds them to the user list.
     *
     * @param filePath path to the staff CSV file
     */
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

    /**
     * Loads student data from a CSV file and adds them to the user list.
     *
     * @param filePath path to the student CSV file
     */
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

    /**
     * Loads internship records from a CSV file, resolves company and representative references,
     * constructs {@link Internship} objects, and stores them in the global list.
     *
     * @param filePath   path to the internship CSV file
     * @param users      list of users (used to match company representatives)
     * @param companyMap map containing existing companies for reuse
     */
    private static void loadInternships(String filePath, List<User> users, Map<String, Company> companyMap) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line = br.readLine(); // skip header
        while ((line = br.readLine()) != null) {
            String[] tokens = splitCSV(line);
            if (tokens.length < 9) {
                System.out.println("Skipping invalid row: " + line);
                continue;
            }

            try {
                String title = tokens[0].replace("\uFEFF", "").trim();
                String description = tokens[1].trim();
                String companyName = tokens[2].trim();
                String companyRepId = tokens[3].trim();
                String levelStr = tokens[4].trim();
                String preferredMajor = tokens[5].trim();
                int totalSlots = Integer.parseInt(tokens[6].trim());
                Date openingDate = sdf.parse(tokens[7].trim());
                Date closingDate = sdf.parse(tokens[8].trim());

                // Get or create company
                Company company = companyMap.getOrDefault(companyName, new Company(companyName));
                companyMap.putIfAbsent(companyName, company);

                // Find the company rep
                CompanyRep rep = users.stream()
                        .filter(u -> u instanceof CompanyRep)
                        .map(u -> (CompanyRep) u)
                        .filter(r -> r.getUserId().equalsIgnoreCase(companyRepId))
                        .findFirst()
                        .orElse(null);

                if (rep == null) {
                    System.out.println("Warning: CompanyRep with ID '" + companyRepId + "' not found for internship '" + title + "'. Skipping...");
                    continue;
                }

                // Parse level
                InternshipLevel level;
                try {
                    level = InternshipLevel.valueOf(levelStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid internship level '" + levelStr + "' for " + title + ". Defaulting to BASIC.");
                    level = InternshipLevel.BASIC;
                }

                Internship internship = new Internship(title, description, company, rep, level, preferredMajor, totalSlots, openingDate, closingDate);
                GlobalInternshipList.addInternship(internship);

                // Debug
                System.out.println("Loaded internship: " + title + " | Company: " + companyName + " | Rep: " + rep.getUserId());

            } catch (Exception e) {
                System.out.println("Skipping invalid internship row: " + line);
                e.printStackTrace();
            }
        }

        System.out.println("Internships loaded.");
    } catch (Exception e) {
        System.out.println("Error loading internships: " + e.getMessage());
        e.printStackTrace();
    }
}
}
