
package controller;

import java.util.ArrayList;
import model.Report_Student;
import model.Student;
import view.Menu;


public class STUDENT_MANAGEMENT extends Menu<String>  {
    static String[] mc = {"Create","Find and Sort","Update/Delete","Report", "Exit"};
    private ArrayList<Student> students ;
    private Check_Valid checkValid;
    private int count = 0;
    public STUDENT_MANAGEMENT(){
        super("Student Management",mc);
        students = new ArrayList<>();
        checkValid = new Check_Valid();
    }
    
        @Override
    public void execute(int choice) {
        switch(choice){
            case 1 -> {create_Student();
            }
            case 2 -> {
                Find_and_Sort();
            }
            case 3 -> {
                Update_or_Delete();
            }
            case 4 -> {
                report();
            }
            default -> System.out.println("Try again");
        }
    }
    
    public void create_Student(){
        if (count > 9) {
            System.out.print("Do you want to continue (Y/N): ");
            if (!checkValid.checkInputYN()) {
                return;
            }
        }
         while (true) {
            System.out.print("Enter id: ");
            String id = checkValid.checkInputString();
            System.out.print("Enter name student: ");
            String name = checkValid.checkInputString();
            if (!checkValid.checkIdExist(students, id, name)) {
                System.err.println("Id has exist student. Pleas re-input.");
                continue;
            }
            System.out.print("Enter semester: ");
            String semester = checkValid.checkInputString();
            System.out.print("Enter name course: ");
            String course = checkValid.checkInputCourse();
            //check student exist or not
            if (checkValid.checkStudentExist(students, id, name, semester, course)) {
                students.add(new Student(id, name, semester, course));
                count++;
                System.out.println("Add student success.");
                return;
            }
            System.err.println("Duplicate.");

        }
    }

 public void Find_and_Sort() {
    if (students.isEmpty()) {
        System.err.println("List empty.");
        return;
    }
    
    System.out.print("Enter name to search: ");
    String name = checkValid.checkInputString();
    
    boolean found = false; // Sử dụng để kiểm tra xem có sinh viên nào thỏa mãn hay không
    
    System.out.printf("%-15s%-15s%-15s\n", "Student name", "semester", "Course Name");
    
    for (Student student : students) {
        if (student.getStudent_Name().contains(name)) {
            student.print();
            found = true; // Đánh dấu là đã tìm thấy ít nhất một sinh viên thỏa mãn
        }
    }
    
    if (!found) {
        System.err.println("Not exist!");
    }
}


 public void Update_or_Delete() {
    if (students.isEmpty()) {
        System.err.println("List empty.");
        return;
    }

    System.out.print("Enter student ID: ");
    String id = checkValid.checkInputString();

    ArrayList<Student> studentsToUpdateOrDelete = new ArrayList<>();
    for (Student student : students) {
        if (student.getID().equalsIgnoreCase(id)) {
            studentsToUpdateOrDelete.add(student);
        }
    }

    if (studentsToUpdateOrDelete.isEmpty()) {
        System.err.println("Student with ID " + id + " not found.");
        return;
    }

    System.out.println("List of students with ID " + id + ":");
    int Count = 1;
    System.out.printf("%-15s%-15s%-15s\n", "Student name", "semester", "Course Name");
    for (Student student : studentsToUpdateOrDelete) {
        System.out.println(Count + ". " );
        student.print();
        Count++;
    }

    System.out.print("Do you want to update (U) or delete (D) student? ");
    if (checkValid.checkInputUD()) {
        System.out.print("Enter the number of the student to update/delete: ");
        int choice = checkValid.checkInputIntLimit(1, studentsToUpdateOrDelete.size());

        Student studentToUpdateOrDelete = studentsToUpdateOrDelete.get(choice - 1);

            System.out.print("Enter new ID: ");
        String newId = checkValid.checkInputString();
        System.out.print("Enter new student name: ");
        String newName = checkValid.checkInputString();
        System.out.print("Enter new semester: ");
        String newSemester = checkValid.checkInputString();
        System.out.print("Enter new course name: ");
        String newCourse = checkValid.checkInputCourse();

        if (!checkValid.checkChangeInfomation(studentToUpdateOrDelete, newId, newName, newSemester, newCourse)) {
            System.err.println("Nothing changed.");
            return;
        }

        if (checkValid.checkStudentExist(students, newId, newName, newSemester, newCourse)) {
            studentToUpdateOrDelete.setID(newId);
            studentToUpdateOrDelete.setStudent_Name(newName);
            studentToUpdateOrDelete.setSemester(newSemester);
            studentToUpdateOrDelete.setCourse_Name(newCourse);
            System.err.println("Update success.");
        }
    } else {
       students.removeAll(studentsToUpdateOrDelete);
       count -= studentsToUpdateOrDelete.size();
       System.err.println("Delete success.");

    }
}

   public void report() {
    if (students.isEmpty()) {
        System.err.println("List empty.");
        return;
    }

    ArrayList<Student> uniqueStudents = new ArrayList<>();
    ArrayList<Report_Student> reportList = new ArrayList<>();
    for (Student student : students) {
        boolean found = false;
        for (Student uniqueStudent : uniqueStudents) {
            if (uniqueStudent.getStudent_Name().equals(student.getStudent_Name()) &&
                uniqueStudent.getCourse_Name().equals(student.getCourse_Name())) {
                found = true;
                break;
            }
        }
        if (!found) {
            uniqueStudents.add(student);
        }
    }

    System.out.printf("%-15s%-15s%-15s\n", "Student name", "Course Name", "Total Courses");
    for (Student uniqueStudent : uniqueStudents) {
        int totalCourses = countTotalCourses(uniqueStudent.getStudent_Name(), uniqueStudent.getCourse_Name());
        reportList.add(new Report_Student(uniqueStudent.getStudent_Name(), uniqueStudent.getCourse_Name(), totalCourses));
    }
    
    for(Report_Student rp : reportList){
        System.out.printf("%-15s%-15s%-15s\n", rp.getStudentName(), rp.getCourseName(), rp.getTotalCourse());
    }
}

private int countTotalCourses(String studentName, String courseName) {
    int totalCourses = 0;
    for (Student student : students) {
        if (student.getStudent_Name().equals(studentName) && student.getCourse_Name().equals(courseName)) {
            totalCourses++;
        }
    }
    return totalCourses;
}

}
