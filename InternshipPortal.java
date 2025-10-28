import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InternshipPortal {
    public static void main(String[] args) throws ParseException {
        // Create a company
        Company compA = new Company("TechCorp");

        // Create a company rep
        CompanyRep rep1 = new CompanyRep("rep1@techcorp.com", "Alice", compA, "HR", "Manager");

        // Create a student
        Student stu1 = new Student("stu123", "Bob", 2, "Computer Science");

        // Create a career center staff
        CareerCenterStaff staff = new CareerCenterStaff("staff001", "Carol", "Internship Dept");

        // Staff approves the company rep first
        staff.login("staff001", "password"); // staff login
        staff.reviewCompanyRep(rep1, true); // approve rep

        // Company rep logs in
        rep1.login("rep1@techcorp.com", "password");

        // Company rep creates an internship
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date openDate = sdf.parse("2025-11-01");
        Date closeDate = sdf.parse("2025-11-30");
        Internship intern1 = rep1.createInternship(
                "Software Intern",
                "Work on backend systems",
                "Basic",
                "Computer Science",
                5,
                openDate,
                closeDate
        );

        // Staff logs in (again) to approve the internship
        staff.login("staff001", "password");
        staff.reviewInternship(intern1, true);

        // Student logs in
        stu1.login("stu123", "password");

        // Student views available internships
        List<Internship> available = stu1.viewAvailableInternships();
        System.out.println("Available internships for " + stu1.getName() + ":");
        for (Internship i : available) {
            System.out.println(i.getTitle() + " at " + i.getCompany().getName());
        }

        // Student applies for internship
        stu1.apply(intern1);
    }
}
