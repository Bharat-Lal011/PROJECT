package users;

import models.Course;
import java.util.ArrayList;
import java.util.List;

public class Professor extends User {

    private String department;
    private List <Course> myCourses;
    public Professor(String name, String email, String password, String department) {
        super( name, email, password, "Professor" );
        this.department = department;
        this.myCourses  = new ArrayList<>();
    }
    public String getDepartment()    { return department; }
    public List<Course> getMyCourses()   { return myCourses; }

    public void addCourse(Course c) {
        myCourses.add(c);
    }

    public void viewEnrolledStudents(List<Student> allStudents) {
        for (Course c : myCourses) {
            System.out.println("Students in: " + c.getTitle());
            boolean found = false;
            for (Student s : allStudents) {
                for (Course sc : s.getEnrolledCourses()) {
                    if (sc.getCourseCode().equals(c.getCourseCode())) {
                        System.out.println("  " + s.getName()
                                + " | " + s.getEmail()
                                + " | Sem: " + s.getSemester());
                        found = true;
                    }
                }
            }
            if (!found) System.out.println("  No students yet.");
        }
    }
    @Override
    public void displayMenu() {
        System.out.println("\n --- Professor Menu --- ");
        System.out.println(" 1. View My Courses");
        System.out.println(" 2. Update Course Details");
        System.out.println(" 3. View Enrolled Students");
        System.out.println("  4. Logout");
    }
}