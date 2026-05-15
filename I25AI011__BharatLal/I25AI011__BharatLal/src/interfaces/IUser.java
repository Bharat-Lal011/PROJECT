package interfaces;

public interface IUser {
    void login(String email, String Password);
    void logout();
    void displayMenu();
}