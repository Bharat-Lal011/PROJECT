# University Course Registration System

## How to Run

1. Open project in IntelliJ IDEA
2. Make sure all files are in `src` folder:
    - `interfaces/IUser.java`
    - `users/User.java`, `Student.java`, `Professor.java`, `Admin.java`
    - `models/Course.java`, `Complaint.java`
    - `services/AuthService.java`, `CourseService.java`
    - `Main.java`
3. Run `Main.java`

---

## Demo Data (Already Loaded)

### 5 Courses (loaded automatically):
| Code  | Title                        | Credits | Semester |
|-------|------------------------------|---------|----------|
| EC106 | Digital Electronics          | 4       | 1        |
| MA106 | Discrete Mathematics         | 4       | 2        |
| IA102 | Data Structures              | 4       | 3        |
| IA106 | Object Oriented Programming  | 4       | 4        |
| HS120 | IVS                          | 2       | 5        |

#### Create These Users After Running:

----3 Students:----
- Bharat  — alice@uni.com  / pass123
- Mukesh    — bob@uni.com    / pass123
- Rahul — charlie@uni.com / pass123

---2 Professors:---
- Dr. Smith — smith@svnit.com / pass123 — DM
- Dr. Jones — jones@svnit.com / pass123 — DELD

----1 Admin:----
- Password: admin123


## OOP Concepts Used

### 1. Class
Every entity is a separate class.
- `User`, `Student`, `Professor`, `Admin`, `Course`, `Complaint`

### 2. Interface
`IUser` interface defines common methods for all users.
```java
public interface IUser {
    void login(String email, String password);
    void logout();
    void displayMenu();
}
```

### 3. Inheritance
`Student`, `Professor`, and `Admin` all extend `User`.
- Common fields like name, email, password are in `User`
- Each subclass adds its own fields and methods

### 4. Abstraction
`User` is an abstract class.
- `displayMenu()` is abstract — every subclass must implement it
- Internal details are hidden from outside

### 5. Encapsulation
All fields in every class are `private`.
- Data is accessed only through getters and setters
- Example: `getName()`, `setPassword()`, `getCredits()`

### 6. Polymorphism
`displayMenu()` behaves differently in each class.
- `Student.displayMenu()` shows student options
- `Professor.displayMenu()` shows professor options
- `Admin.displayMenu()` shows admin options
- Same method name, different behavior

---

## Assumptions Made

1. Credit limit per semester is fixed at 20
2. Courses are either 2 or 4 credits only
3. Student starts from semester 1
4. Semester is completed only when admin marks it done
5. Admin password is fixed as `admin123`
6. Student can drop a course anytime in current semester
7. Data is not saved after program exits (no database used)
8. Prerequisites are checked before course registration

---

## Project Structure

```
src/
├── interfaces/
│   └── IUser.java
├── users/
│   ├── User.java
│   ├── Student.java
│   ├── Professor.java
│   └── Admin.java
├── models/
│   ├── Course.java
│   └── Complaint.java
├── services/
│   ├── AuthService.java
│   └── CourseService.java
├── Main.java
└── README.md
```