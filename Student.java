import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


class Student extends User {
    private int year;
    private String major;
    private Map<Internship, String> appliedInternships; // Stores key as the Internship object and value as the status
    private Internship acceptedInternship;

    public Student(String userId, String name, int year, String major) {
        super(userId, name);
        this.year = year;
        this.major = major;
        this.appliedInternships = new HashMap<>();
        this.acceptedInternship = null;
    }

    public List<Internship> viewAvailableInternships() {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return new ArrayList<>(); // return empty list instead
        }
        return GlobalInternshipList.getAll().stream()
            .filter(i -> i.isVisible())
            .filter(i -> i.getStatus().equalsIgnoreCase("Approved"))
            .filter(i -> i.getPreferredMajor().equalsIgnoreCase(this.major))
            .filter(i -> {
                if (this.year < 3) {
                    return i.getLevel().equalsIgnoreCase("Basic");
                }
                return true;
            })
            .collect(Collectors.toList());
    }


    public void apply(Internship internship) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        // Max 3 applications
        if(appliedInternships.size() >= 3) {
            System.out.println(getName() + " has already applied for 3 internships.");
            return;
        }

        // Year-level restriction
        if(year < 3 && !internship.getLevel().equals("Basic")) {
            System.out.println("Year restriction: cannot apply for this level.");
            return;
        }

        // Check visibility and approval
        if(!internship.isVisible() || !"Approved".equals(internship.getStatus())) {
            System.out.println("Cannot apply: Internship not available.");
            return;
        }

        // Check application date
        Date now = new Date();
        if(now.before(internship.getOpeningDate()) || now.after(internship.getClosingDate())) {
            System.out.println("Cannot apply: Outside application period.");
            return;
        }

        // Apply
        appliedInternships.put(internship, "Pending");
        internship.addApplicant(this);
        System.out.println(getName() + " applied for " + internship.getTitle() + " at " + internship.getCompany().getName());
    }

    public void acceptInternship(Internship internship) {
        if (!isLoggedIn) {
            System.out.println("You must be logged in to perform this action.");
            return;
        }
        String status = appliedInternships.get(internship);
        if(status != null && status.equals("Successful")) {
            if(acceptedInternship != null) {
                System.out.println("Already accepted an internship.");
                return;
            }
            acceptedInternship = internship;
            internship.decreaseSlot();
            // withdraw from other applications
            for(Internship i : appliedInternships.keySet()) {
                if(!i.equals(internship)) {
                    appliedInternships.put(i, "Withdrawn");
                }
            }
            System.out.println(getName() + " accepted internship: " + internship.getTitle() + "for " + internship.getCompany());
        } else {
            System.out.println("Cannot accept: Application not successful yet.");
        }
    }

    public String getMajor() {
        return major;
    }

    // Getter for appliedInternships
    public Map<Internship, String> getAppliedInternships() {
        return appliedInternships;
    }

    public Internship getAcceptedInternship() {
        return acceptedInternship;
    }

    public void setAcceptedInternship(Internship acceptedInternship) {
        this.acceptedInternship = acceptedInternship;
    }
}