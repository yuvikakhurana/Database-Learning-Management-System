import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiscussionForum extends JComponent implements Runnable {

    JButton createAccount;
    JButton signIn;
    JButton exit;
    DiscussionForum discussionForum;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createAccount) {
                String username = JOptionPane.showInputDialog(null, "Enter a username:",
                        "New Account", JOptionPane.QUESTION_MESSAGE);
                String password = JOptionPane.showInputDialog(null, "Enter a password:",
                        "New Account", JOptionPane.QUESTION_MESSAGE);
            }

            if (e.getSource() == signIn) {
                String username = JOptionPane.showInputDialog(null, "Enter your username:",
                        "Sign In", JOptionPane.QUESTION_MESSAGE);
                String password = JOptionPane.showInputDialog(null, "Enter your password:",
                        "Sign In", JOptionPane.QUESTION_MESSAGE);
            }

            if (e.getSource() == exit) {
                JOptionPane.showMessageDialog(null, "Thanks for using the Learning Management" +
                        "System", "Exit", JOptionPane.PLAIN_MESSAGE);
            }
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new DiscussionForum());
    }

    public void run() {
        JFrame frame = new JFrame("Menu");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        discussionForum = new DiscussionForum();
        content.add(discussionForum, BorderLayout.CENTER);

        createAccount = new JButton("Create a new Account");
        createAccount.addActionListener(actionListener);
        signIn = new JButton("Sign In");
        signIn.addActionListener(actionListener);
        exit = new JButton("Exit");
        exit.addActionListener(actionListener);

        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel topPanel = new JPanel();
        topPanel.add(createAccount);
        content.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.add(signIn);
        content.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(exit);
        content.add(bottomPanel, BorderLayout.SOUTH);
    }
}
