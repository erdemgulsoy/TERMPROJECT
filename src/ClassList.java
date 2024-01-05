public class ClassList {
    private int studentID;
    private String professorName;
    private String courseID;
    private int examDuration;

    public ClassList(int studentID, String professorName, String courseID, int examDuration) {
        this.studentID = studentID;
        this.professorName = professorName;
        this.courseID = courseID;
        this.examDuration = examDuration;
    }

    // Getter ve Setter metodları

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public int getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(int examDuration) {
        this.examDuration = examDuration;
    }
}