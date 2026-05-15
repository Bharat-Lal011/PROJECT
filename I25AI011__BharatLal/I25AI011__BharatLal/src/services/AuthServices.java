package services;

import users.Student;
import users.Professor;
import users.Admin;
import java.util.ArrayList;
import java.util.List;

public class AuthServices {

    private List<Student> students;
    private List<Professor> professors;
    private Admin admin;

    public AuthServices() {
        students   = new ArrayList<>();
        professors = new ArrayList<>();
        admin      = new Admin();
    }

    // --- Getters ---
    public List<Student>   getStudents()   { return students; }
    public List<Professor> getProfessors() { return professors; }
    public Admin           getAdmin()      { return admin; }

    // --- Student Signup ---
    public void signupStudent(String name, String email, String password) {
        for (Student s : students) {
            if (s.getEmail().equals(email)) {
                System.out.println("Email already registered!");
                return;
            }
        }
        Student s = new Student(name, email, password);
        students.add(s);
        System.out.println("Student registered: " + name);
    }

    // --- Professor Signup ---
    public void signupProfessor(String name, String email, String password, String dept) {
        for (Professor p : professors) {
            if (p.getEmail().equals(email)) {
                System.out.println("Email already registered!");
                return;
            }
        }
        Professor p = new Professor(name, email, password, dept);
        professors.add(p);
        System.out.println("Professor registered: " + name);
    }

    // --- Student Login ---
    public Student loginStudent(String email, String password) {
        for (Student s : students) {
            if (s.getEmail().equals(email) && s.checkPassword(password)) {
                System.out.println("Welcome, " + s.getName() + "!");
                return s;
            }
        }
        System.out.println("Wrong email or password!");
        return null;
    }

    // --- Professor Login ---
    public Professor loginProfessor(String email, String password) {
        for (Professor p : professors) {
            if (p.getEmail().equals(email) && p.checkPassword(password)) {
                System.out.println("Welcome, " + p.getName() + "!");
                return p;
            }
        }
        System.out.println("Wrong email or password!");
        return null;
    }

    // --- Admin Login ---
    public Admin loginAdmin(String password) {
        if (password.equals("admin123")) {
            System.out.println("Welcome, Admin!");
            return admin;
        }
        System.out.println("Wrong password!");
        return null;
    }
}