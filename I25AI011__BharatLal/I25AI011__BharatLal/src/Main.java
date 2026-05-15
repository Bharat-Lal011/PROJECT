import services.AuthServices;
import services.CourseServices;
import users.Student;
import users.Professor;
import users.Admin;
import models.Course;
import models.Complaint;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static AuthServices auth = new AuthServices();
    static CourseServices courseService = new CourseServices();

    public static void main(String[] args) {

        System.out.println(" ---- University Registration System ----");

        boolean running = true;
        while (running) {
            System.out.println("\n 1. Enter Application : ");
            System.out.println(" 2. Exit : ");
            System.out.print(" Choose : ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                showLoginMenu();
            } else {
                System.out.println("Goodbye!");
                running = false;
            }
        }
    }

    static void showLoginMenu() {
        System.out.println(" \nLogin as  :");
        System.out.println("1. Student ");
        System.out.println("  2. Professo r");
        System.out.println(" 3. Admin ");
        System.out.println(" 4. Back ");
        System.out.print(" Choose : ");
        int choice = sc.nextInt();
        sc.nextLine();

        if      (choice == 1) studentLoginMenu();
        else if (choice == 2) professorLoginMenu();
        else if (choice == 3) adminLoginMenu();
    }

    static void studentLoginMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Signup");
        System.out.print("Choose: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();

            Student s = auth.loginStudent(email, pass);
            if (s != null) {
                studentMenu(s); // go to student menu
            }

        } else if (choice == 2) {
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();

            auth.signupStudent(name, email, pass);
        }
    }

    static void professorLoginMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Signup");
        System.out.print("Choose: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();

            Professor p = auth.loginProfessor(email, pass);
            if (p != null) {
                professorMenu(p);
            }

        } else if (choice == 2) {
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();
            System.out.print("Department: ");
            String dept = sc.nextLine();

            auth.signupProfessor(name, email, pass, dept);
        }
    }

    static void adminLoginMenu() {
        System.out.print("Admin Password: ");
        String pass = sc.nextLine();

        Admin a = auth.loginAdmin(pass);
        if (a != null) {
            adminMenu(a); // go to admin menu
        }
    }

    static void studentMenu(Student s) {
        boolean loggedIn = true;
        while (loggedIn) {
            s.displayMenu();
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                courseService.viewAllCourses();

            } else if (choice == 2) {
                System.out.print("Enter Course Code: ");
                String code = sc.nextLine();
                Course c = courseService.findCourse(code);
                if (c != null) s.registerCourse(c);

            } else if (choice == 3) {
                s.viewSchedule();

            } else if (choice == 4) {
                s.viewProgress();

            } else if (choice == 5) {
                System.out.print("Enter Course Code to drop: ");
                String code = sc.nextLine();
                s.dropCourse(code);

            } else if (choice == 6) {
                System.out.print("Enter your complaint: ");
                String desc = sc.nextLine();
                s.submitComplaint(desc);

            } else if (choice == 7) {
                s.viewComplaints();

            } else if (choice == 8) {
                s.logout();
                loggedIn = false;
            }
        }
    }

    static void professorMenu(Professor p) {
        boolean loggedIn = true;
        while (loggedIn) {
            p.displayMenu();
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                if (p.getMyCourses().isEmpty()) {
                    System.out.println("No courses assigned yet.");
                } else {
                    for (Course c : p.getMyCourses())
                        c.displayDetails();
                }

            } else if (choice == 2) {
                System.out.print("Enter Course Code: ");
                String code = sc.nextLine();
                Course c = courseService.findCourse(code);
                if (c != null) updateCourseMenu(p, c);

            } else if (choice == 3) {
                p.viewEnrolledStudents(auth.getStudents());

            } else if (choice == 4) {
                p.logout();
                loggedIn = false;
            }
        }
    }

    static void updateCourseMenu(Professor p, Course c) {
        System.out.println(" \n--- Update Course : " + c.getTitle() + " ---");
        System.out.println(" 1. Update Syllabus ");
        System.out.println(" 2. Update Timing ");
        System.out.println(" 3. Update Credits ");
        System.out.println(" 4. Update Enrollment Limit ");
        System.out.print(" Choose: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("  New Syllabus: ");
            c.setSyllabus(sc.nextLine());
            System.out.println(" Syllabus updated! ");

        } else if (choice == 2) {
            System.out.print(" New Timing: ");
            c.setTiming(sc.nextLine());
            System.out.println(" Timing updated!");

        } else if (choice == 3) {
            System.out.print(" Credits (2 or 4): ");
            int cr = sc.nextInt(); sc.nextLine();
            if (cr == 2 || cr == 4) {
                c.setCredits(cr);
                System.out.println(" Credits updated! ");
            } else {
                System.out.println(" Only 2 or 4 allowed! ");
            }

        } else if (choice == 4) {
            System.out.print(" New Limit :  ");
            c.setEnrollmentLimit(sc.nextInt());
            sc.nextLine();
            System.out.println(" Limit updated! ");
        }
    }

    static void adminMenu(Admin a) {
        boolean loggedIn = true;
        while (loggedIn) {
            a.displayMenu();
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                manageCourses();

            } else if (choice == 2) {
                manageStudents(a);

            } else if (choice == 3) {
                assignProfessor(a);

            } else if (choice == 4) {
                handleComplaints(a);

            } else if (choice == 5) {
                completeSemester(a);

            } else if (choice == 6) {
                a.logout();
                loggedIn = false;
            }
        }
    }

    static void manageCourses() {
        System.out.println(" \n1. View All Courses");
        System.out.println(" 2. Add Course");
        System.out.println(" 3. Delete Course");
        System.out.print( " Choose: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            courseService.viewAllCourses();

        } else if (choice == 2) {
            System.out.print("Course Code: ");
            String code = sc.nextLine();
            System.out.print("Title: ");
            String title = sc.nextLine();
            System.out.print("Credits (2 or 4): ");
            int credits = sc.nextInt(); sc.nextLine();
            System.out.print("Timing: ");
            String timing = sc.nextLine();
            System.out.print("Location: ");
            String location = sc.nextLine();
            System.out.print("Semester: ");
            int sem = sc.nextInt(); sc.nextLine();
            System.out.print("Enrollment Limit: ");
            int limit = sc.nextInt(); sc.nextLine();

            Course c = new Course(code, title, credits, timing, location, sem, limit);
            courseService.addCourse(c);

        } else if (choice == 3) {
            System.out.print("Course Code to delete: ");
            String code = sc.nextLine();
            courseService.deleteCourse(code);
        }
    }

    static void manageStudents(Admin a) {
        System.out.println("\n1. View All Students");
        System.out.println("2. Assign Grade");
        System.out.print("Choose: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            List<Student> list = auth.getStudents();
            if (list.isEmpty()) {
                System.out.println("No students found.");
                return;
            }
            for (Student s : list) {
                System.out.println("Name: " + s.getName()
                        + " | Email: " + s.getEmail()
                        + " | Semester: " + s.getSemester());
            }

        } else if (choice == 2) {
            System.out.print("Student Email: ");
            String email = sc.nextLine();

            Student found = null;
            for (Student s : auth.getStudents()) {
                if (s.getEmail().equals(email)) {
                    found = s;
                    break;
                }
            }

            if (found == null) {
                System.out.println("Student not found!");
                return;
            }

            System.out.print("Course Code: ");
            String code = sc.nextLine();
            System.out.print("Grade (0-10): ");
            double grade = sc.nextDouble();
            sc.nextLine();

            a.giveGrade(found, code, grade);
        }
    }

    static void assignProfessor(Admin a) {
        System.out.print("Professor Email: ");
        String email = sc.nextLine();

        Professor found = null;
        for (Professor p : auth.getProfessors()) {
            if (p.getEmail().equals(email)) {
                found = p;
                break;
            }
        }

        if (found == null) {
            System.out.println("Professor not found!");
            return;
        }

        System.out.print("Course Code: ");
        String code = sc.nextLine();
        Course c = courseService.findCourse(code);
        if (c != null) a.assignProfessor(found, c);
    }

    static void handleComplaints(Admin a) {
        List<Complaint> allComplaints = new ArrayList<>();
        for (Student s : auth.getStudents())
            allComplaints.addAll(s.getComplaints());

        System.out.println("\n1. View All Complaints");
        System.out.println("2. Resolve a Complaint");
        System.out.println("3. Filter by Status");
        System.out.print("Choose: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            if (allComplaints.isEmpty()) {
                System.out.println("No complaints found.");
                return;
            }
            for (Complaint c : allComplaints)
                c.displayComplaint();

        } else if (choice == 2) {
            System.out.print("Complaint ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            Complaint found = null;
            for (Complaint c : allComplaints) {
                if (c.getComplaintID() == id) {
                    found = c;
                    break;
                }
            }

            if (found == null) {
                System.out.println("Complaint not found!");
                return;
            }

            System.out.print("Resolution: ");
            String details = sc.nextLine();
            a.resolveComplaint(found, details);

        } else if (choice == 3) {
            System.out.print("Status (Pending/Resolved): ");
            String status = sc.nextLine();
            for (Complaint c : allComplaints) {
                if (c.getStatus().equalsIgnoreCase(status))
                    c.displayComplaint();
            }
        }
    }

    static void completeSemester(Admin a) {
        System.out.print("Student Email: ");
        String email = sc.nextLine();

        for (Student s : auth.getStudents()) {
            if (s.getEmail().equals(email)) {
                a.finishSemester(s);
                return;
            }
        }
        System.out.println("Student not found!");
    }
}