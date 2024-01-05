import java.util.ArrayList;
import java.util.List;

public class Exam {

        private List<Integer> studentIDs; // List of student IDs
        private String professorName;
        private String courseID;
        private int examDuration;
        private List<Exam> conflicts;

        public Exam(List<Integer> studentIDs, String professorName, String courseID, int examDuration) {
            this.studentIDs = studentIDs;
            this.professorName = professorName;
            this.courseID = courseID;
            this.examDuration = examDuration;
            this.conflicts = new ArrayList<>();
        }

        // Methods to add and get conflicts
        public void addConflict(Exam node) {
            conflicts.add(node);
        }

        public List<Exam> getConflicts() {
            return conflicts;
        }

        // Getter methods for the properties
        public List<Integer> getStudentIDs() {
            return studentIDs;
        }

        public String getProfessorName() {
            return professorName;
        }

        public String getCourseID() {
            return courseID;
        }

        public int getExamDuration() {
            return examDuration;
        }

        public int getNumberOfStudents() {
        return studentIDs.size();
    }

}
