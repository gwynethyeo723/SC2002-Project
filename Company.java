import java.util.List;
import java.util.ArrayList;
class Company {
    private String name;
    private List<CompanyRep> representatives;
    private List<Internship> internshipsOffered;

    public Company(String name) {
        this.name = name;
        this.representatives = new ArrayList<>();
        this.internshipsOffered = new ArrayList<>();
    }

    public void addRepresentative(CompanyRep rep) {
        representatives.add(rep);
    }

    public void addInternship(Internship internship) {
        internshipsOffered.add(internship);
    }

    public String getName() {
        return name;
    }

    public List<Internship> getInternshipsOffered() {
        return internshipsOffered;
    }
}
