import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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




    //////////////////////////////////////////  ALGO  //////////////////////////////////////////////////////////////
    private static final int DAYS = 6; // Monday to Saturday
    private static final int HOURS = 9; // 09:00 to 18:00
    private static final String[] DAY_NAMES = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    // A class to represent a list of exams in a single slot
    private static class ExamSlot {
        List<Exam> exams = new ArrayList<>();

        public void addExam(Exam exam) {
            exams.add(exam);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Exam exam : exams) {
                sb.append(exam.getCourseID()).append(" (").append(exam.getProfessorName()).append("), ");
            }
            return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "Free";
        }
    }

    // Method to create and initialize the timetable
    private static ExamSlot[][] createTimetable() {
        return new ExamSlot[DAYS][HOURS];
    }

    // Method to schedule exams in the timetable
    private static void scheduleExams(AllExam allExam, ExamSlot[][] timetable) {
        Random rand = new Random();

        for (Exam exam : allExam.getNodes()) {
            boolean scheduled = false;
            while (!scheduled) {
                int day = rand.nextInt(DAYS);
                int hour = rand.nextInt(HOURS);

                if (canScheduleExam(timetable, day, hour, exam.getProfessorName())) {
                    if (timetable[day][hour] == null) {
                        timetable[day][hour] = new ExamSlot();
                    }
                    timetable[day][hour].addExam(exam);
                    scheduled = true;
                }
            }
        }
    }

    // Method to check if an exam can be scheduled at the given day and hour
    private static boolean canScheduleExam(ExamSlot[][] timetable, int day, int hour, String professorName) {
        if (timetable[day][hour] != null) {
            for (Exam exam : timetable[day][hour].exams) {
                if (exam.getProfessorName().equals(professorName)) {
                    return false; // Professor already has an exam at this slot
                }
            }
        }
        return true;
    }

    // Method to print the scheduled timetable along with total exam hours per day
    private static void printTimetable(ExamSlot[][] timetable) {
        for (int day = 0; day < DAYS; day++) {
            System.out.println(DAY_NAMES[day] + ":");
            for (int hour = 0; hour < HOURS; hour++) {
                if (timetable[day][hour] != null && !timetable[day][hour].exams.isEmpty()) {
                    for (Exam exam : timetable[day][hour].exams) {
                        String classroomInfo = exam.getAssignedClassrooms().stream()
                                .map(Classroom::getRoomID)
                                .collect(Collectors.joining(", "));
                        System.out.println("  " + (hour + 9) + ":00 - " + exam.getCourseID() +
                                " (" + exam.getProfessorName() + ", " + exam.getExamDuration() +
                                " min) in classrooms: " + classroomInfo);
                    }
                } else {
                    System.out.println("  " + (hour + 9) + ":00 - Free");
                }
            }
            System.out.println();
        }
    }



    private static void assignClassroomsToExams(List<Classroom> classrooms, AllExam allExam) {
        for (Exam exam : allExam.getNodes()) {
            int requiredClassrooms = exam.calculateRequiredClassrooms(classrooms.get(0).getCapacity()); // Assuming all classrooms have the same capacity
            List<Classroom> assignedClassrooms = new ArrayList<>();
            for (int i = 0; i < requiredClassrooms; i++) {
                if (i < classrooms.size()) {
                    assignedClassrooms.add(classrooms.get(i));
                } else {
                    // Handle case when there are not enough classrooms
                    System.out.println("Not enough classrooms for " + exam.getCourseID());
                    break;
                }
            }
            exam.setAssignedClassrooms(assignedClassrooms);
        }
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

        /////////////////////////////////////////////////////////////////////////////////

        assignClassroomsToExams(classrooms, allExam);

        ExamSlot[][] timetable = createTimetable();
        scheduleExams(allExam, timetable);
        printTimetable(timetable);

    }

}
