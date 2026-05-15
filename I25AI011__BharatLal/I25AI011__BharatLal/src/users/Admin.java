package users;

import models.Course;
import models.Complaint;

public class Admin extends User {

    public Admin() {
        super("Admin", "admin@svnit.com", "admin123", "Admin");
    }

    public void assignProfessor(Professor p, Course c) {
        c.setProfessor(p.getName());
        p.addCourse(c);
        System.out.println(p.getName() + " assigned to " + c.getTitle());
    }

    public void giveGrade(Student s, String courseCode, double grade) {
        s.assignGrade(courseCode, grade);
        System.out.println("Grade given to " + s.getName());
    }

    public void finishSemester(Student s) {
        s.completeSemester();
    }

    public void resolveComplaint(Complaint c, String details) {
        c.setStatus("Resolved");
        c.setResolutionDetails(details);
        System.out.println("Complaint " + c.getComplaintID() + " resolved.");
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. Manage Courses");
        System.out.println("2. Manage Student Records");
        System.out.println("3. Assign Professor to Course");
        System.out.println("4. Handle Complaints");
        System.out.println("5. Complete Student Semester");
        System.out.println("6. Logout");
    }
}
