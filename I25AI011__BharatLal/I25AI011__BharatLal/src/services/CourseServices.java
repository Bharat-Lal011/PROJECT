package services;

import models.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseServices {

    private List<Course> allCourses;

    public CourseServices() {
        allCourses = new ArrayList<>();
        loadSampleCourses(); // demo data
    }

    public List<Course> getAllCourses() {
        return allCourses;
    }

    public void addCourse(Course c) {
        allCourses.add(c);
        System.out.println("Course added: " + c.getTitle());
    }
    public void deleteCourse(String courseCode) {
        for (Course c : allCourses) {
            if (c.getCourseCode().equals(courseCode)) {
                allCourses.remove(c);
                System.out.println("Course deleted: " + c.getTitle());
                return;
            }
        }
        System.out.println("Course not found!");
    }

    // --- Find course by code ---
    public Course findCourse(String courseCode) {
        for (Course c : allCourses) {
            if (c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        System.out.println("Course not found!");
        return null;
    }

    // --- View all courses ---
    public void viewAllCourses() {
        if (allCourses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        System.out.println("\n---- All Courses ----");
        for (Course c : allCourses) {
            c.displayDetails();
        }
    }

    public void viewCoursesBySemester(int semester) {
        System.out.println("\n--- Courses for Semester " + semester + " ----");
        boolean found = false;
        for (Course c : allCourses) {
            if (c.getSemester() == semester) {
                c.displayDetails();
                found = true;
            }
        }
        if (!found) System.out.println("No courses found for semester " + semester);
    }

    private void loadSampleCourses() {
        // Semester 1 courses
        Course c1 = new Course(" EC106 ", " Digital Electronics ", 4,
                " Mon 9-11am ", " Room 106 ", 1, 30);

        Course c2 = new Course(" MA106 ", " Discrete Mathematics  ", 4,
                " Tue 9-10am ", " Room 107 ", 1, 30);

        Course c3 = new Course(" IA102 ", " Data Structures ", 4,
                 " Wed 9-12am ", " Room 108 ", 1, 30);

        Course c4 = new Course(" IA106 ", " Object Oriented Programming ", 4,
                " Mon 11-1pm ", " Room 109 ", 1, 30);
        c4.addPrerequisites(" IA106 ");

        Course c5 = new Course(" HS120 ", "  IVS ", 2,
                " Fri 9-11am ", " Room 110 ", 1, 30);
        c5.addPrerequisites(" HS120 ");

        allCourses.add(c1);
        allCourses.add(c2);
        allCourses.add(c3);
        allCourses.add(c4);
        allCourses.add(c5);

        System.out.println(" Sample courses loaded! ");
    }
}