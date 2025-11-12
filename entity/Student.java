package entity;

// ----- This class defines the attributes relevant to the Student user. -----//
// ----- Relevant attributes include Year of Study and  Major. -----//

public class Student extends User {
    private int year;
    private String major;
    // private Map<Internship, ApplicationStatus> appliedInternships;
    // private Internship acceptedInternship;
    // private boolean pendingWithdrawal;

    public Student(String userId, String name, int year, String major) {
        super(userId, name);
        this.year = year;
        this.major = major;
        // this.appliedInternships = new HashMap<>();
        // this.acceptedInternship = null;
        // this.pendingWithdrawal = false;
    }

    // public List<Internship> viewAvailableInternships() {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return new ArrayList<>(); // return empty list instead
    //     }

    //     return GlobalInternshipList.getAll().stream()
    //         .filter(Internship::getVisibility)
    //         .filter(i -> i.getStatus() == InternshipStatus.APPROVED) // enum comparison
    //         .filter(i -> i.getPreferredMajor().equalsIgnoreCase(this.major))
    //         .filter(i -> {
    //             if (this.year < 3) {
    //                 return i.getLevel() == InternshipLevel.BASIC; // enum comparison
    //             }
    //             return true;
    //         })
    //         .collect(Collectors.toList());
    // }


    // public void apply(Internship internship) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return;
    //     }
    //     // Max 3 applications
    //     if(appliedInternships.size() >= 3) {
    //         System.out.println(getName() + " has already applied for 3 internships.");
    //         return;
    //     }

    //     // Year-level restriction
    //     if (year < 3 && internship.getLevel() != InternshipLevel.BASIC) {
    //         System.out.println("Year restriction: cannot apply for this level.");
    //         return;
    //     }

    //     // Check visibility and approval
    //     if (!internship.getVisibility() || internship.getStatus() != InternshipStatus.APPROVED) {
    //         System.out.println("Cannot apply: Internship not available.");
    //         return;
    //     }

    //     // Check application date
    //     Date now = new Date();
    //     if(now.before(internship.getOpeningDate()) || now.after(internship.getClosingDate())) {
    //         System.out.println("Cannot apply: Outside application period.");
    //         return;
    //     }

    //     // Apply
    //     appliedInternships.put(internship, ApplicationStatus.PENDING);
    //     internship.addApplicant(this);
    //     System.out.println(getName() + " applied for " + internship.getTitle() + " at " + internship.getCompany().getName());
    // }

    // // Method called when student accepts an internship
    // public void acceptInternship(Internship internship) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return;
    //     }

    //     ApplicationStatus status = appliedInternships.get(internship); // get enum value
    //     if (status != null && status == ApplicationStatus.SUCCESSFUL) { // compare enum with ==
    //         if (acceptedInternship != null) {
    //             System.out.println("Already accepted an internship.");
    //             return;
    //         }
    //         acceptedInternship = internship;
    //         internship.decreaseSlot();
    //         internship.addAcceptedStudent(this); // track accepted student

    //         // Withdraw from other applications
    //         for (Internship i : appliedInternships.keySet()) {
    //             if (!i.equals(internship)) {
    //                 appliedInternships.put(i, ApplicationStatus.WITHDRAWN); // use enum constant
    //             }
    //         }

    //         System.out.println(getName() + " accepted internship: " + internship.getTitle() +
    //             " for " + internship.getCompany().getName());
    //     } else {
    //         System.out.println("Cannot accept: Application not successful yet.");
    //     }
    // }

    // // Method to remove an internship from appliedInternships (used during deletion)
    // public void removeAppliedInternship(Internship internship) {
    //     appliedInternships.remove(internship);
    // }


    // public void requestWithdrawal(Internship internship) {
    //     if (!isLoggedIn) {
    //         System.out.println("You must be logged in to perform this action.");
    //         return;
    //     }

    //     // Check if the student has applied OR has accepted this internship
    //     boolean hasApplied = appliedInternships.containsKey(internship);
    //     boolean hasAccepted = acceptedInternship != null && acceptedInternship.equals(internship);

    //     if (!hasApplied && !hasAccepted) {
    //         System.out.println("You have neither applied for nor accepted this internship.");
    //         return;
    //     }

    //     pendingWithdrawal = true;
    //     System.out.println(getName() + " has requested withdrawal from " + internship.getTitle());
    // }

    // Getters and Setters
    public int getYear() {return year;}
    public String getMajor() {return major;}
    public void setYear(int year) {this.year = year;}
    public void setMajor(String major){this.major = major;}
    // public Map<Internship, ApplicationStatus> getAppliedInternships() {return appliedInternships;}
    // public Internship getAcceptedInternship() {return acceptedInternship;}
    // public void setAcceptedInternship(Internship acceptedInternship) {this.acceptedInternship = acceptedInternship;}
    // public boolean isPendingWithdrawal() {return pendingWithdrawal;}
    // public void setPendingWithdrawal(boolean pendingWithdrawal) {this.pendingWithdrawal = pendingWithdrawal;}

    @Override
    public boolean login(String id, String password2) {
        if (this.userId.equals(id) && this.getPassword().equals(password2)) {
            this.setLoggedIn(true);
            return true;
        }
        return false;
    }
}