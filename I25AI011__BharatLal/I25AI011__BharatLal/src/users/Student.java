package users;

import models.Course;
import models.Complaint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {

    private int semester;
    private List<Course> enrolledCourses;
    private List<Course> completedCourses;
    private List<String> completedCourseCodes;
    private Map<String, Double> grades;  // courseCode -> grade
    private List<Complaint> complaints;

    public Student(String name, String email, String password) {
        super(name, email, password, "Student");
        semester = 1;
        enrolledCourses   = new ArrayList<>();
        completedCourses  = new ArrayList<>();
        completedCourseCodes = new ArrayList<>();
        grades     = new HashMap<>();
        complaints = new ArrayList<>();
    }

    public int getSemester()                        { return semester; }
    public List<Course> getEnrolledCourses()        { return enrolledCourses; }
    public List<Course> getCompletedCourses()       { return completedCourses; }
    public List<String> getCompletedCourseCodes()   { return completedCourseCodes; }
    public Map<String, Double> getGrades()          { return grades; }
    public List<Complaint> getComplaints()          { return complaints; }

    public int getTotalCredits() {
        int total = 0;
        for (Course c : enrolledCourses) {
            total += c.getCredits();
        }
        return total;
    }

    public void registerCourse(Course course) {
        if (course.getSemester() != semester) {
            System.out.println("This course is not for semester " + semester);
            return;
        }
        if (getTotalCredits() + course.getCredits() > 20) {
            System.out.println("Credit limit of 20 will exceed!");
            return;
        }
        for (String pre : course.getPrerequisites()) {
            if (!completedCourseCodes.contains(pre)) {
                System.out.println(" Prerequisite not completed:  " + pre);
                return;
            }
        }
        if (course.isFull()) {
            System.out.println("  Course is full! ");
            return;
        }
        enrolledCourses.add(course);
        course.EnrollStudent(getEmail());
        System.out.println("Registered in: " + course.getTitle());
    }

    public void dropCourse(String courseCode) {
        for (Course c : enrolledCourses) {
            if (c.getCourseCode().equals(courseCode)) {
                enrolledCourses.remove(c);
                c.removeStudent(getEmail());
                System.out.println("Dropped: " + c.getTitle());
                return;
            }
        }
        System.out.println("Course not found.");
    }

    public void viewSchedule() {
        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled.");
            return;
        }
        System.out.println("\n----- Your Schedule -----");
        for (Course c : enrolledCourses) {
            System.out.println(c.getTitle() + " | " + c.getTiming()
                    + " | " + c.getLocation()
                    + " | Prof: " + c.getProfessor());
        }
    }

    public void submitComplaint(String description) {
        Complaint c = new Complaint(getEmail(), description);
        complaints.add(c);
        System.out.println("Complaint submitted. ID: " + c.getComplaintID());
    }

    public void viewComplaints() {
        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
            return;
        }
        for (Complaint c : complaints)
            c.displayComplaint();
    }

    public void assignGrade(String courseCode, double grade) {
        grades.put(courseCode, grade);
    }

    public void completeSemester() {
        completedCourses.addAll(enrolledCourses);
        for (Course c : enrolledCourses)
            completedCourseCodes.add(c.getCourseCode());
        enrolledCourses.clear();
        semester++;
        System.out.println("Semester done! Now in semester " + semester);
    }

    public double getCGPA() {
        double totalPoints = 0;
        int totalCredits = 0;
        for (Course c : completedCourses) {
            if (grades.containsKey(c.getCourseCode())) {
                totalPoints += grades.get(c.getCourseCode()) * c.getCredits();
                totalCredits += c.getCredits();
            }
        }
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    public void viewProgress() {
        if (completedCourses.isEmpty()) {
            System.out.println("No completed courses yet.");
            return;
        }
        System.out.println("\n--- Academic Progress ---");
        for (Course c : completedCourses) {
            double g = grades.getOrDefault(c.getCourseCode(), 0.0);
            System.out.println(c.getCourseCode() + " - " + c.getTitle()
                    + " | Grade: " + g);
        }
        System.out.printf("CGPA: %.2f%n", getCGPA());
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- Student Menu ---");
        System.out.println(" 1. View Available Courses ");
        System.out.println(" 2. Register for a Course ");
        System.out.println(" 3. View My Schedule ");
        System.out.println(" 4. View Academic Progress ");
        System.out.println(" 5. Drop a Course ");
        System.out.println(" 6. Submit Complaint ");
        System.out.println(" 7. View My Complaints ");
        System.out.println(" 8. Logout ");
    }
}
