import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class GUIPracticeClient extends JComponent implements Runnable  {
    JButton createAnAccount;
    JButton signIn;
    JButton exit;
    JFrame jFrame;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;
    private  static Socket socket;

    public static String inputHostName(){
        String name;
        name = JOptionPane.showInputDialog(null, "Enter Host name",
                "Learning Management System", JOptionPane.QUESTION_MESSAGE);
        return name;

    }

    public static void main(String[] args) throws Exception {
        // write your code here

        String hostName;
        int portNumber = -1; //TODO: CHECK
        //PrintWriter writer;
        //Scanner in;
        LMSClient lmsClient = null;

        try {
            hostName = inputHostName();
            //System.out.println(hostName);
            boolean host1;
            boolean port1;
            do {
                host1 = false;
                if (hostName == null || hostName.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid Host Name.",
                            "Learning Management System", JOptionPane.ERROR_MESSAGE);
                    host1 = true;
                    hostName = inputHostName();
                }

            } while (host1);


            do {
                port1 = false;
                try {
                    portNumber = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Port number",
                            "Learning Management System", JOptionPane.QUESTION_MESSAGE));
                    if (portNumber < 0) {
                        JOptionPane.showMessageDialog(null, "Enter a positive number", "Learning Management System", JOptionPane.ERROR_MESSAGE);
                        port1 = true;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Enter an Integer", "Learning Management System", JOptionPane.ERROR_MESSAGE);
                    port1 = true;
                }
            } while (port1);

            //System.out.println(portNumber);

            socket = new Socket(hostName, portNumber);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to establish connection", "Learning Management System",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        JOptionPane.showMessageDialog(null, "Connection has been established!",
                "Learning Management System", JOptionPane.INFORMATION_MESSAGE);

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        SwingUtilities.invokeLater(new GUIPracticeClient());

    }

    //public ActionListener()



    public  void run()  {



        jFrame = new JFrame("Learning Management System");
        Panel panel = new Panel();

        jFrame.setSize(400,100);

        JLabel label1 = new JLabel("Welcome to Discussion Forums!");
        panel.add(label1);

        Panel panel1 = new Panel();

        createAnAccount = new JButton("Create an Account");
        createAnAccount.addActionListener(new ActionListener() {
            String name;
            String username;
            String password;
            public void actionPerformed(ActionEvent ae) {
                jFrame.setVisible(false);
                int userSelection = 1;
                try {
                    objectOutputStream.writeObject(userSelection);
                    objectOutputStream.flush();
                    //System.out.println("Please enter your name");
                    name = JOptionPane.showInputDialog(null, "Please enter your name",
                            "LMS SignUp", JOptionPane.QUESTION_MESSAGE);

                    while (name == null || name.equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter your name!", "LMS SignUp",
                                JOptionPane.ERROR_MESSAGE);
                        name = JOptionPane.showInputDialog(null, "Please enter your name",
                                "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                    }

                    objectOutputStream.writeObject(name);
                    objectOutputStream.flush();

                    //System.out.println("Please enter a username");
                    username = JOptionPane.showInputDialog(null, "Please enter a username",
                            "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                    // Checking for valid input
                    while (username == null || username.equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a username!", "LMS SignUp",
                                JOptionPane.ERROR_MESSAGE);
                        username = JOptionPane.showInputDialog(null, "Please enter a username",
                                "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                    }

                    boolean usernameVerify = false;
                    do {
                        usernameVerify = false;

                        objectOutputStream.writeObject(username);
                        objectOutputStream.flush();

                        usernameVerify = (Boolean) objectInputStream.readObject();

                        if (usernameVerify) {
                            //System.out.println("Invalid. Username has already been taken. Please enter another username.");
                            JOptionPane.showMessageDialog(null, "Invalid. Username has already been taken. Please enter another username.", "LMS SignUp",
                                    JOptionPane.ERROR_MESSAGE);
                            username = JOptionPane.showInputDialog(null, "Please enter a username",
                                    "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                        }

                    } while (usernameVerify);

                    //System.out.println("Please enter a password");
                     password = JOptionPane.showInputDialog(null, "Please enter a password",
                            "LMS SignUp", JOptionPane.QUESTION_MESSAGE);

                    while (password == null || password.equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a password!", "LMS SignUp",
                                JOptionPane.ERROR_MESSAGE);
                        password = JOptionPane.showInputDialog(null, "Please enter a password",
                                "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                    }

                    //Sending password to the server
                    objectOutputStream.writeObject(password);
                    objectOutputStream.flush();

                    panel.setVisible(false);

                    JPanel panel2 = new JPanel();
                    JButton teacher = new JButton("Teacher");
                    teacher.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int userRoleSelection = 1;
                            try {
                                objectOutputStream.writeObject(userRoleSelection);
                                objectOutputStream.flush();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            LMSClient lmsClient = new LMSClient(new Teacher(name, username, password));

                        }
                    });
                    panel2.add(teacher);
                    JButton student = new JButton("Student");
                    student.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int userRoleSelection = 2;
                            try {
                                objectOutputStream.writeObject(userRoleSelection);
                                objectOutputStream.flush();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            LMSClient lmsClient = new LMSClient(new Student(name, username, password));

                        }
                    });
                    panel2.add(student);

                    //jFrame = new JFrame("Learning Management System");

                    jFrame.add(panel2);

                    jFrame.setVisible(true);

                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        panel1.add(createAnAccount);

        signIn = new JButton("Sign in");
        signIn.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                int userSelection = 2;
                try {
                    objectOutputStream.writeObject(userSelection);
                    objectOutputStream.flush();
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
                String verifySignIn = "false";
                do {
                    //System.out.println("Please enter your username");

                    String username = JOptionPane.showInputDialog(null, "Please enter a username",
                            "LMS Login", JOptionPane.QUESTION_MESSAGE);


                    while (username == null || username.equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a username!", "LMS Login",
                                JOptionPane.ERROR_MESSAGE);
                        username = JOptionPane.showInputDialog(null, "Please enter a username",
                                "LMS Login", JOptionPane.QUESTION_MESSAGE);
                    }

                    try {

                        objectOutputStream.writeObject(username);
                        objectOutputStream.flush();
                    }catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    //System.out.println("Please enter your password");
                    String password = JOptionPane.showInputDialog(null, "Please enter a password",
                            "LMS SignUp", JOptionPane.QUESTION_MESSAGE);

                    while (password == null || password.equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a password!", "LMS Login",
                                JOptionPane.ERROR_MESSAGE);
                        password = JOptionPane.showInputDialog(null, "Please enter a password",
                                "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                    }

                    try {
                        objectOutputStream.writeObject(password);
                        objectOutputStream.flush();
                    }catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    //Verifying the username and password are valid
                    try {
                        verifySignIn = (String) objectInputStream.readObject();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    if (verifySignIn.equals("true")) {
                        JOptionPane.showMessageDialog(null, "You have been logged in!", "Learning Management System", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            LMSClient lmsClient = new LMSClient((Person) objectInputStream.readObject());
                        }catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                    if (verifySignIn.equals("no accounts found")) {
                        //System.out.println("No Account found with the given username. Please create an account");
                        JOptionPane.showMessageDialog(null, "No Account found with the given username. Please create an account", "Learning Management System", JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    if (verifySignIn.equals("false")) {
                        //System.out.println("Invalid username or password. Please try again.");
                        JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.", "Learning Management System", JOptionPane.ERROR_MESSAGE);
                    }

                } while (verifySignIn.equals("false"));
            }
        });
        panel1.add(signIn);

        exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userSelection = 3;
                try {
                    objectOutputStream.writeObject(userSelection);
                    objectOutputStream.flush();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
        });
        panel1.add(exit);

        panel.add(panel1);

        jFrame.add(panel);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jFrame.setVisible(true);

    }

}
