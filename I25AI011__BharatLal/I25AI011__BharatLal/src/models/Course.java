package models;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String CourseCode;
    private String title;
    private String professor;
    private int credits;
    private List<String> prerequisites;
    private String timing;
    private String location;
    private String syllabus;
    private int EnrollmentLimit;
    private int semester;
    private ArrayList<Object> enrolledStudentEmails;



    public Course (String CourseCode, String title, int credits, String timing, String location, int syllabus, int EnrollmentLimit ) {
        this.CourseCode = CourseCode;
        this.title = title;
        this.credits = credits;
        this.timing = timing;
        this.location = location;
        this.semester = semester;
        this.EnrollmentLimit = EnrollmentLimit;
        this.professor = "ABCD";
        this.syllabus = "XYZ";
        this.prerequisites = new ArrayList<>();
        this.enrolledStudentEmails = new ArrayList<>();
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getProfessor() {
        return professor;
    }

    public int getCredits() {
        return credits;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public String getTiming() {
        return timing;
    }

    public String getLocation() {
        return location;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public int getEnrollmentLimit() {
        return EnrollmentLimit;
    }

    public int getSemester() {
        return semester;
    }

    public ArrayList<Object> getEnrolledStudentEmails() {
        return enrolledStudentEmails;
    }
    public void setProfessor (String name) {
        this.professor = name;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setEnrollmentLimit(int Limit) {
        this.EnrollmentLimit = Limit;
    }

    public void addPrerequisites(String Code) {
        this.prerequisites.add(Code);
    }
    public boolean isFull () {
        return enrolledStudentEmails.size() >= EnrollmentLimit;
    }
    public void EnrollStudent (String email) {
         enrolledStudentEmails.add(email);
    }
    public void removeStudent (String email) {
        enrolledStudentEmails.remove(email);
    }
    public void displayDetails () {
        System.out.println("----------------");
        System.out.println(" Code :  " + CourseCode);
        System.out.println(" Title :  " + title);
        System.out.println(" Professor :  " + professor);
        System.out.println(" Credits :  " + credits);
        System.out.println(" Timing :  " + timing);
        System.out.println("  Location : " + location);
        System.out.println(" Semester : " + semester);
        System.out.println("  Prerequisites : " + prerequisites);
        System.out.println("  Slots Lefts : " + (EnrollmentLimit - enrolledStudentEmails.size()));
        System.out.println("------------------");
    }
}
