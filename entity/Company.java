package entity;

// ----- This class defines the attributes relevant to each Company. -----//
// ----- Relevant attributes include Company Name. -----//
public class Company {
    private String name;
    // private List<CompanyRep> representatives;
    // private List<Internship> internshipsOffered;

    public Company(String name) {
        this.name = name;
        // this.representatives = new ArrayList<>();
        // this.internshipsOffered = new ArrayList<>();
    }

    // public void addRepresentative(CompanyRep rep) {
    //     representatives.add(rep);
    // }

    // public void addInternship(Internship internship) {
    //     internshipsOffered.add(internship);
    // }

    // public void removeInternship(Internship internship) {
    //     internshipsOffered.remove(internship);
    // }

    public String getName() {
        return name;
    }

    // public List<Internship> getInternshipsOffered() {
    //     return internshipsOffered;
    // }
}
