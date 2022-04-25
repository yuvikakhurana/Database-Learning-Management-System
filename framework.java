import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class framework extends JComponent implements Runnable {
    JButton newAccountButton;
    JButton loginButton;
    JButton exitButton;
    JFrame frame = new JFrame();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new framework());
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == newAccountButton) {
               //todo run a class or method to create a new account
            }
            if (e.getSource() == loginButton) {
                //todo run the class or method to login to an existing account
            }
            if (e.getSource() == exitButton) {
                frame.setVisible(false);
                JOptionPane.showMessageDialog(null, "Discussion Forum has been exited. Goodbye!", "Discussion Forum", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
                //todo, program is closed, rewrite text files to log any system changes and exit program
            }
        }
    };

    @Override
    public void run() {
        frame.setTitle("Purdue Enhanced Classroom Discussion Forum");
        Container content = frame.getContentPane();
        frame.setSize(300,300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Font mainFont = new Font("Courier", Font.BOLD,40);

        newAccountButton = new JButton(" New Account ");
        newAccountButton.setFont(mainFont);
        newAccountButton.addActionListener(actionListener);
        newAccountButton.setBackground(Color.lightGray);

        loginButton = new JButton("       Login       ");
        loginButton.setFont(mainFont);
        loginButton.addActionListener(actionListener);
        loginButton.setBackground(Color.lightGray);

        exitButton = new JButton("         Exit         ");
        exitButton.setFont(mainFont);
        exitButton.addActionListener(actionListener);
        exitButton.setBackground(Color.lightGray);

        JPanel startPage = new JPanel();

        setSize(300,300);
        startPage.add(newAccountButton);
        startPage.add(loginButton);
        startPage.add(exitButton);
        content.add(startPage);
    }
}
