import src.model.ui.LoginPanel;

import javax.swing.*;

public class BankApp {

    public static void main(String[] args) {
        // Use system look & feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SecureBank - Banking System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(640, 560);
            frame.setMinimumSize(new java.awt.Dimension(540, 460));
            frame.setLocationRelativeTo(null);
            frame.setResizable(true);

            // Show login screen
            frame.getContentPane().add(new LoginPanel(frame));
            frame.setVisible(true);
        });
    }
}
