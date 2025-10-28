import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalStudentList {
    private static List<Student> students = new ArrayList<>();

    public static void addStudent(Student student) {
        students.add(student);
    }

    public static void removeStudent(Student student) {
        students.remove(student);
    }

    public static List<Student> getAll() {
        return Collections.unmodifiableList(students);
    }

    public static void clear() {
        students.clear();
    }
}