import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    ////////////////////////////////////////////////////////////////// READ&DİSPLAY CLASSROOM
    private static List<Classroom> readClassroomsFromCSV(String fileName) {
        List<Classroom> classrooms = new ArrayList<>();
        String line = "";
        String splitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader("Classes.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] classroomData = line.split(splitBy);
                Classroom classroom = new Classroom(classroomData[0], Integer.parseInt(classroomData[1]));
                classrooms.add(classroom);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classrooms;
    }
    public static void displayClasses(List<Classroom> classrooms){
        for (Classroom classroom : classrooms) {
            System.out.println("Room ID: " + classroom.getRoomID() + ", Capacity: " + classroom.getCapacity());
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////READ&&DİSPLAY CLASSLİST
    private static List<ClassList> readClassListFromCSV(String fileName) {
        List<ClassList> classLists = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] classData = line.split(",");
                ClassList classList = new ClassList(Integer.parseInt(classData[0].trim()),
                        classData[1].trim(),
                        classData[2].trim(),
                        Integer.parseInt(classData[3].trim()));
                classLists.add(classList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classLists;
    }
    public static void displayClassList(List<ClassList> classlist){
        for (ClassList classList : classlist) {
            System.out.println("Student ID: " + classList.getStudentID() +
                    ", Professor Name: " + classList.getProfessorName() +
                    ", Course ID: " + classList.getCourseID() +
                    ", Exam Duration: " + classList.getExamDuration());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////CREATE GRAPH BY USİNG EXAM NODE
    private static AllExam createExamGraph(List<ClassList> classLists) {
        AllExam allExam = new AllExam();
        Map<String, Exam> courseExamMap = new HashMap<>();

        // Group students by course ID
        for (ClassList classList : classLists) {
            String courseID = classList.getCourseID();

            if (!courseExamMap.containsKey(courseID)) {
                courseExamMap.put(courseID, new Exam(new ArrayList<>(),
                        classList.getProfessorName(),
                        classList.getCourseID(),
                        classList.getExamDuration()));
            }
            courseExamMap.get(courseID).getStudentIDs().add(classList.getStudentID());
        }

        // Add nodes to the graph
        for (Exam exam : courseExamMap.values()) {
            allExam.addNode(exam);
        }

        return allExam;
    }





    public static void main(String[] args) {
        List<Classroom> classrooms = readClassroomsFromCSV("Classes.csv");
        //displayClasses(classrooms);
        List<ClassList> classLists = readClassListFromCSV("1000student.csv");
        //displayClassList(classLists);

        //GRAPH READY

        AllExam allExam =createExamGraph(classLists);
        allExam.printGraph();
        System.out.println(allExam.getNumberOfNodes());


    }

}
