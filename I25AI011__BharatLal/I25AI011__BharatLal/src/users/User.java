package users;
import interfaces.IUser;
public abstract class User implements IUser {
    private String name;
    private String email;
    private String password;
    private String roll;

    public User (String name, String email, String password, String roll) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roll = roll;
    }

    public String getName() { return name ;}
    public String getEmail() { return email ;}
    public String getRole() { return roll ;}

    public boolean checkPassword(String pwd) {
        return this.password.equals(pwd);
    }

    public boolean setPassword (String pwd) {
        this.password = pwd;
        return false;
    }
    public abstract void displayMenu();

    // Method Overridding
    public void login( String email, String password) {
        System.out.println(name + " logged in Successfully: ");
    }
    public void logout() {
        System.out.println(name + " logged out: ");
    }
}