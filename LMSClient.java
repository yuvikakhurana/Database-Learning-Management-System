import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class LMSClient {

    Person person1;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    //Constructor for person(Teacher or Student) who will be the user of the program running
    public LMSClient(Person person1) {

        this.person1 = person1;
    }

    //Getter and Setter for the person object
    public Person getPerson1() {

        return person1;
    }

    public void setPerson1(Person person1) {

        this.person1 = person1;
    }

    //Method for inputting host name
    public static String inputHostName(){
        String name;
        name = JOptionPane.showInputDialog(null, "Enter Host name",
                "Learning Management System", JOptionPane.QUESTION_MESSAGE);
        return name;

    }

    // Method to break Discussion/Responses greater than 160 characters in length in appropriate lines
    // to fit its respective GUI
    public static String stringInLinesMethod(String s) {
        StringBuilder sb = new StringBuilder("");
        char[] characters = {' ', ',', '.', '"', '-', ')', '(', ':', '/', '?', '<', '>', '[', ']', '{', ';', '_', '+', '=', '*', '&', '^', '%', '$', '#', '@', '!', '~'};
        if (s.length() > 160) {
            for (int i = 159; i < s.length(); i = i) {
                boolean dashNeeded = true;
                for (int j = 0; j < characters.length; j++) {
                    if (s.charAt(i + 1) == characters[j]) {
                        dashNeeded = false;
                    }
                }

                if (s.charAt(i) == ' ') {
                    dashNeeded = false;
                }
                sb.append(s.substring(0, i + 1));
                if (dashNeeded) {
                    sb.append("-");
                }
                sb.append("\n");
                s = s.substring(i + 1);
            }
            if (s.length() < 160) {
                sb.append(s);
                sb.append("\n");
            }

        } else if (s.length() < 160) {
            return s;
        }
        String result = sb.toString();
        return result;
    }

    //Method to import the content from a file provided by the User
    // Handles IOExceptions in the method this method is called
    public static ArrayList<String> fileImporter(String fileName) throws IOException {

        String line = "";
        ArrayList<String> fileData = new ArrayList<>(); //An ArrayList to store the lines in the file
        FileReader fr = new FileReader(fileName);
        BufferedReader bfr = new BufferedReader(fr);

        line = bfr.readLine();

        while (line != null) {
            fileData.add(line);
            line = bfr.readLine();
        }

        bfr.close();

        return fileData;
    }

    // Method to retrieve the topic/Header of the DiscussionForum
    // Assumption: It will be the first line of the file
    public static String titleGetter(ArrayList<String> fileData) {

        //gets title (first line) of an already imported file
        String title = fileData.get(0);

        return title;
    }

    //Method to convert String received from the Server to a Post object
    public static Post stringToPost (String postString) {
        Post post = new Post(null, null, null, new ArrayList<>(), null);

        int x = postString.indexOf("~");

        String courseName = postString.substring(0, postString.indexOf("~"));
        post.setCourseName(courseName);
        postString = postString.substring(postString.indexOf("~") + 1);

        String discussionTopic = postString.substring(0, postString.indexOf("~"));
        if(discussionTopic.equals("null")) {
            post.setDiscussionTopic(null);
        } else {
            postString = postString.substring(postString.indexOf("~") + 1);
            String timeStamp = postString.substring(0, postString.indexOf("~"));
            DiscussionTopic discussionTopic1 = new DiscussionTopic(discussionTopic);
            discussionTopic1.setTimeStamp(timeStamp);
            post.setDiscussionTopic(discussionTopic1);
        }

        postString = postString.substring(postString.indexOf("~") + 1);

        String post21 = postString.substring(0, postString.indexOf("~"));

        if(post21.equals("null")) {
            post.setPost(null);
        } else {
            post.setPost(post21);
        }

        postString = postString.substring(postString.indexOf("~") + 1);

        int arraySize =  Integer.parseInt(postString.substring(0, postString.indexOf("~")));

        for (int i = 0; i < arraySize; i++) {

            postString = postString.substring(postString.indexOf("~") + 1);
            String response = postString.substring(0, postString.indexOf("~"));

            postString = postString.substring(postString.indexOf("~") + 1);
            String name = postString.substring(0, postString.indexOf("~"));

            postString = postString.substring(postString.indexOf("~") + 1);
            String userName = postString.substring(0, postString.indexOf("~"));

            postString = postString.substring(postString.indexOf("~") + 1);
            String password = postString.substring(0, postString.indexOf("~"));

            postString = postString.substring(postString.indexOf("~") + 1);
            String timeStamp = postString.substring(0, postString.indexOf("~"));

            Reply reply = new Reply(response, new Person(name, userName, password));
            reply.setTimeStamp(timeStamp);
            post.getReply().add(reply);

        }

        postString = postString.substring(postString.indexOf("~") + 1);
        String studentName = postString.substring(0, postString.indexOf("~"));
        if (studentName.equals("null")) {
            post.setStudent(null);
        } else {
            postString = postString.substring(postString.indexOf("~") + 1);
            String username = postString.substring(0, postString.indexOf("~"));
            postString = postString.substring(postString.indexOf("~") + 1);
            String password = postString.substring(0, postString.indexOf("~"));
            post.setStudent(new Student(studentName, username, password));

        }
        postString = postString.substring(postString.indexOf("~") + 1);
        String timeStamp = postString.substring(0, postString.indexOf("~"));

        post.setTimeStamp(timeStamp);

        postString = postString.substring(postString.indexOf("~") + 1);
        double grade = Double.parseDouble(postString);

        post.setGrade(grade);

        return  post;
    }

    public static void main(String[] args)  {

        LMSClient lmsClient = null;
        String hostName;
        int portNumber = -1;
        Socket socket;

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
            //Establishing a socket with the given input
            socket = new Socket(hostName, portNumber);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to establish connection", "Learning Management System",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        JOptionPane.showMessageDialog(null, "Connection has been established!",
                "Learning Management System", JOptionPane.INFORMATION_MESSAGE);

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        //Initial Menu - 1 (Common for both- Student and Teacher)
        String[] options = { "Create an account", "Sign in", "Exit"};
        int userSelection = JOptionPane.showOptionDialog(null, "Please choose one of the following:", "Learning Management System",
                JOptionPane.PLAIN_MESSAGE, 1, null, options, null );
        while(userSelection == -1) {
            invalidInputMessage();
            userSelection = JOptionPane.showOptionDialog(null, "Please choose one of the following:", "Learning Management System",
                    JOptionPane.PLAIN_MESSAGE, 1, null, options, null );
        }
        userSelection = userSelection + 1;
        try {
            objectOutputStream.writeObject(userSelection);
            objectOutputStream.flush();

            do {
                //User selects to create an account
                if (userSelection == 1) {
                    String name = JOptionPane.showInputDialog(null, "Please enter your name",
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
                    String username = JOptionPane.showInputDialog(null, "Please enter a username",
                            "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                    // Checking for valid input
                    while (username == null || username.equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a username!", "LMS SignUp",
                                JOptionPane.ERROR_MESSAGE);
                        username = JOptionPane.showInputDialog(null, "Please enter a username",
                                "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                    }

                    //To verify the username chosen is not already taken
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
                            while (username == null || username.equals("")) {
                                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a username!", "LMS SignUp",
                                        JOptionPane.ERROR_MESSAGE);
                                username = JOptionPane.showInputDialog(null, "Please enter a username",
                                        "LMS SignUp", JOptionPane.QUESTION_MESSAGE);
                            }
                        }

                    } while (usernameVerify);

                    String password = JOptionPane.showInputDialog(null, "Please enter a password",
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

                    String[] options1 = {"Teacher", "Student"};
                    int userRoleSelection = JOptionPane.showOptionDialog(null, "Please choose your role:", "Learning Management System",
                            JOptionPane.PLAIN_MESSAGE, 1, null, options1, options1[0]);
                    while (userRoleSelection == -1) {
                        invalidInputMessage();
                        userRoleSelection = JOptionPane.showOptionDialog(null, "Please choose your role:", "Learning Management System",
                                JOptionPane.PLAIN_MESSAGE, 1, null, options1, options1[0]);
                    }
                    userRoleSelection = userRoleSelection + 1;

                    objectOutputStream.writeObject(userRoleSelection);
                    objectOutputStream.flush();

                    if (userRoleSelection == 1) {
                        lmsClient = new LMSClient(new Teacher(name, username, password));
                    } else if (userRoleSelection == 2) {
                        lmsClient = new LMSClient(new Student(name, username, password));
                    }

                    //If user chooses to Sign In
                } else if (userSelection == 2) {
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
                                String instanceOf = (String) objectInputStream.readObject();
                                if(instanceOf.equals("Teacher")) {
                                    lmsClient = new LMSClient(new Teacher((String) objectInputStream.readObject(),
                                            (String) objectInputStream.readObject(), (String) objectInputStream.readObject()));
                                } else if (instanceOf.equals("Student")) {
                                    lmsClient = new LMSClient(new Student((String) objectInputStream.readObject(),
                                            (String) objectInputStream.readObject(), (String) objectInputStream.readObject()));
                                }

                            } catch (Exception exception) {
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

                    //If user selects to exit
                } else if (userSelection == 3) {
                    //System.out.println("Thank you. Bye!");
                    JOptionPane.showMessageDialog(null, "Thank you. Bye!", "Learning Management System", JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                if (lmsClient != null && lmsClient.getPerson1() instanceof Teacher) {

                    int teacherUserSelection1 = teacherMenu1();

                    boolean exit;

                    do {

                        exit = false;

                        if (teacherUserSelection1 == 1) {
                            createCourseGUI();
                        } else if (teacherUserSelection1 == 2) {
                            //SwingUtilities.invokeLater(new LMSClient2());
                            teacherOption2();
                            //Thread.currentThread().join();
                        } else if (teacherUserSelection1 == 3) {
                            gradeStudentGUI();
                        } else if (teacherUserSelection1 == 4) {
                            String[] options8 = {"Edit Account", "Delete Account"};
                            int editOrDelete = JOptionPane.showOptionDialog(null, "Please choose one of the following",
                                    "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null,
                                    options8, options8[0]) + 1;

                            while (editOrDelete == 0) {
                                invalidInputMessage();
                                editOrDelete = JOptionPane.showOptionDialog(null, "Please choose one of the following",
                                        "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null,
                                        options8, options8[0]) + 1;
                            }

                            objectOutputStream.writeObject(editOrDelete);
                            objectOutputStream.flush();

                            if (editOrDelete == 1) {

                                String[] options9 = {"User name", "Password"};
                                int modifyAccount = JOptionPane.showOptionDialog(null, "Please select the part you want to modify: ",
                                        "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null,
                                        options9, options9[0]) + 1;

                                while (modifyAccount == 0) {
                                    invalidInputMessage();
                                    modifyAccount = JOptionPane.showOptionDialog(null, "Please select the part you want to modify: ",
                                            "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null,
                                            options9, options9[0]) + 1;
                                }

                                objectOutputStream.writeObject(modifyAccount);
                                objectOutputStream.flush();

                                //String oldUserName = lmsClient.person1.getUsername();
                                if (lmsClient.getPerson1() instanceof Student) {
                                    objectOutputStream.writeObject("Student");
                                    objectOutputStream.flush();
                                } else if (lmsClient.getPerson1() instanceof Teacher) {
                                    objectOutputStream.writeObject("Teacher");
                                    objectOutputStream.flush();
                                }

                                objectOutputStream.writeObject(lmsClient.getPerson1().getUsername());
                                objectOutputStream.flush();

                                if (modifyAccount == 1) {

                                    boolean usernameVerify = false;
                                    do {

                                        //System.out.println("Please enter the new username:");
                                        String newUserName = JOptionPane.showInputDialog(null, "Please enter the new username:",
                                                "Learning Management System", JOptionPane.QUESTION_MESSAGE );

                                        //Checking for null or empty string input
                                        while (newUserName == null || newUserName.equals("")) {
                                            //System.out.println("Invalid input! Please enter a username!");
                                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a username!",
                                                    "Learning Management System", JOptionPane.ERROR_MESSAGE);
                                            newUserName = JOptionPane.showInputDialog(null, "Please enter the new username:",
                                                    "Learning Management System", JOptionPane.QUESTION_MESSAGE );
                                        }

                                        objectOutputStream.writeObject(newUserName);
                                        objectOutputStream.flush();

                                        usernameVerify = (Boolean) objectInputStream.readObject();

                                        if (usernameVerify) {
                                            //System.out.println("Invalid. Username has already been taken. " +
                                            //      "Please enter another username.");
                                            JOptionPane.showMessageDialog(null, "Invalid. Username has already been taken. " +
                                                            "Please enter another username.",
                                                    "Learning Management System", JOptionPane.ERROR_MESSAGE);
                                        }

                                    } while (usernameVerify);

                                } else if (modifyAccount == 2) {

                                    //System.out.println("Please enter the new password:");
                                    String newPassword = JOptionPane.showInputDialog(null, "Please enter the new password:",
                                            "Learning Management System", JOptionPane.QUESTION_MESSAGE );

                                    while (newPassword == null || newPassword.equals("")) {
                                        //System.out.println("Invalid input! Please enter a password!");
                                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a password!",
                                                "Learning Management System", JOptionPane.ERROR_MESSAGE);
                                        newPassword = JOptionPane.showInputDialog(null, "Please enter the new password:",
                                                "Learning Management System", JOptionPane.QUESTION_MESSAGE );
                                    }

                                    objectOutputStream.writeObject(newPassword);
                                    objectOutputStream.flush();

                                }

                                //System.out.println("Your account has been edited!");
                                //System.out.println("Please login with your new credentials.");
                                JOptionPane.showMessageDialog(null, "Your account has been edited!" + "\n"
                                                + "Please login with your new credentials.",
                                        "Learning Management System", JOptionPane.INFORMATION_MESSAGE);


                            } else if (editOrDelete == 2) {

                                objectOutputStream.writeObject(lmsClient.person1.getUsername());
                                objectOutputStream.flush();

                                //System.out.println("Your account has been deleted!");
                                JOptionPane.showMessageDialog(null, "Your account has been deleted!",
                                        "Learning Management System", JOptionPane.INFORMATION_MESSAGE);

                            }
                            exit = true;
                        } else if (teacherUserSelection1 == 5) {
                            exit = true;
                        }

                        if (!exit) {
                            teacherUserSelection1 = teacherMenu1();
                        }

                    } while (teacherUserSelection1 > 0 && teacherUserSelection1 < 5 && !exit);
                } else if (lmsClient != null && lmsClient.getPerson1() instanceof Student) {
                    String[] studentMenu1 = {"Select a course", "View my Grades", "Edit or delete account",
                            "Exit"};
                    int studentSelection1 = JOptionPane.showOptionDialog(null, "Please choose one of the following:",
                            "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null, studentMenu1, null) + 1;

                    while (studentSelection1 == 0) {
                        invalidInputMessage();
                        studentSelection1 = JOptionPane.showOptionDialog(null, "Please choose one of the following:",
                                "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null, studentMenu1, null) + 1;
                    }

                    objectOutputStream.writeObject(studentSelection1);
                    objectOutputStream.flush();

                    boolean exit;

                    do {
                        exit = false;
                        if (studentSelection1 == 1) {
                            studentSelection1GUI();
                        } else if (studentSelection1 == 2) {
                            ArrayList<Post> posts = new ArrayList<>();
                            int postsSize = (Integer) objectInputStream.readObject();
                            for (int i = 0; i < postsSize; i++) {
                                posts.add(stringToPost((String) objectInputStream.readObject()));
                            }

                            int postNumber = 1;
                            int counter = 0;
                            // printing the posts made by student and the grade received as per the requirement
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < posts.size(); i++) {
                                if (posts.get(i).getStudent() != null
                                        && posts.get(i).getStudent().getUsername().equals(lmsClient.getPerson1().getUsername())) {
                                    sb.append("#" + postNumber++);
                                    sb.append("\n");
                                    sb.append("Course Name: " + posts.get(i).getCourseName());
                                    //System.out.println();
                                    sb.append("\n");
                                    sb.append("Discussion Forum: " +
                                            stringInLinesMethod(posts.get(i).getDiscussionTopic().getTopic()));
                                    //System.out.println(postArrayList.get(i).getDiscussionTopic().getTimeStamp());
                                    //System.out.println();
                                    sb.append("\n");
                                    sb.append("Response: " + stringInLinesMethod(posts.get(i).getPost()));
                                    sb.append("\n");
                                    sb.append(posts.get(i).getTimeStamp());
                                    //System.out.println();
                                    sb.append("\n");
                                    if (posts.get(i).getGrade() != -1) {
                                        sb.append("Grade received: " + posts.get(i).getGrade());
                                    } else if (posts.get(i).getGrade() == -1) {
                                        sb.append("Not Graded yet by the Teacher!");
                                    }
                                    sb.append("\n" + "\n");
                                    counter++;
                                }
                            }
                            // If the counter remains 0
                            // it means student has not made any posts
                            if (counter == 0) {
                                //System.out.println("You have not made any posts!");
                                JOptionPane.showMessageDialog(null, "You have not made any posts!",
                                        "Learning Management System: Grades", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, sb.toString(), "Learning Management System: Grades",
                                        JOptionPane.PLAIN_MESSAGE);
                            }
                        } else if (studentSelection1 == 3) {
                            String[] options8 = {"Edit Account", "Delete Account"};
                            int editOrDelete = JOptionPane.showOptionDialog(null, "Please choose one of the following",
                                    "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null,
                                    options8, options8[0]) + 1;

                            while (editOrDelete == 0) {
                                invalidInputMessage();
                                editOrDelete = JOptionPane.showOptionDialog(null, "Please choose one of the following",
                                        "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null,
                                        options8, options8[0]) + 1;
                            }

                            objectOutputStream.writeObject(editOrDelete);
                            objectOutputStream.flush();

                            if (editOrDelete == 1) {

                                String[] options9 = {"User name", "Password"};
                                int modifyAccount = JOptionPane.showOptionDialog(null, "Please select the part you want to modify: ",
                                        "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null,
                                        options9, options9[0]) + 1;

                                while (modifyAccount == 0) {
                                    invalidInputMessage();
                                    modifyAccount = JOptionPane.showOptionDialog(null, "Please select the part you want to modify: ",
                                            "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null,
                                            options9, options9[0]) + 1;
                                }

                                objectOutputStream.writeObject(modifyAccount);
                                objectOutputStream.flush();

                                //String oldUserName = lmsClient.person1.getUsername();
                                if (lmsClient.getPerson1() instanceof Student) {
                                    objectOutputStream.writeObject("Student");
                                    objectOutputStream.flush();
                                } else if (lmsClient.getPerson1() instanceof Teacher) {
                                    objectOutputStream.writeObject("Teacher");
                                    objectOutputStream.flush();
                                }

                                objectOutputStream.writeObject(lmsClient.getPerson1().getUsername());
                                objectOutputStream.flush();

                                if (modifyAccount == 1) {

                                    boolean usernameVerify = false;
                                    do {

                                        //System.out.println("Please enter the new username:");
                                        String newUserName = JOptionPane.showInputDialog(null, "Please enter the new username:",
                                                "Learning Management System", JOptionPane.QUESTION_MESSAGE );

                                        //Checking for null or empty string input
                                        while (newUserName == null || newUserName.equals("")) {
                                            //System.out.println("Invalid input! Please enter a username!");
                                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a username!",
                                                    "Learning Management System", JOptionPane.ERROR_MESSAGE);
                                            newUserName = JOptionPane.showInputDialog(null, "Please enter the new username:",
                                                    "Learning Management System", JOptionPane.QUESTION_MESSAGE );
                                        }

                                        objectOutputStream.writeObject(newUserName);
                                        objectOutputStream.flush();

                                        usernameVerify = (Boolean) objectInputStream.readObject();

                                        if (usernameVerify) {
                                            //System.out.println("Invalid. Username has already been taken. " +
                                            //      "Please enter another username.");
                                            JOptionPane.showMessageDialog(null, "Invalid. Username has already been taken. " +
                                                            "Please enter another username.",
                                                    "Learning Management System", JOptionPane.ERROR_MESSAGE);
                                        }

                                    } while (usernameVerify);

                                } else if (modifyAccount == 2) {

                                    //System.out.println("Please enter the new password:");
                                    String newPassword = JOptionPane.showInputDialog(null, "Please enter the new password:",
                                            "Learning Management System", JOptionPane.QUESTION_MESSAGE );

                                    while (newPassword == null || newPassword.equals("")) {
                                        //System.out.println("Invalid input! Please enter a password!");
                                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a password!",
                                                "Learning Management System", JOptionPane.ERROR_MESSAGE);
                                        newPassword = JOptionPane.showInputDialog(null, "Please enter the new password:",
                                                "Learning Management System", JOptionPane.QUESTION_MESSAGE );
                                    }

                                    objectOutputStream.writeObject(newPassword);
                                    objectOutputStream.flush();

                                }

                                JOptionPane.showMessageDialog(null, "Your account has been edited!" + "\n"
                                                + "Please login with your new credentials.",
                                        "Learning Management System", JOptionPane.INFORMATION_MESSAGE);


                            } else if (editOrDelete == 2) {

                                objectOutputStream.writeObject(lmsClient.person1.getUsername());
                                objectOutputStream.flush();

                                //System.out.println("Your account has been deleted!");
                                JOptionPane.showMessageDialog(null, "Your account has been deleted!",
                                        "Learning Management System", JOptionPane.INFORMATION_MESSAGE);

                            }
                            exit = true;
                        } else if (studentSelection1 == 4) {
                            exit = true;
                        }

                        if (!exit) {
                            studentSelection1 = JOptionPane.showOptionDialog(null, "Please choose one of the following:",
                                    "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null, studentMenu1, null) + 1;

                            while (studentSelection1 == 0) {
                                invalidInputMessage();
                                studentSelection1 = JOptionPane.showOptionDialog(null, "Please choose one of the following:",
                                        "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null, studentMenu1, null) + 1;
                            }

                            objectOutputStream.writeObject(studentSelection1);
                            objectOutputStream.flush();
                        }

                    } while (studentSelection1 > 0 && studentSelection1 < 4 && !exit);

                }

                userSelection = JOptionPane.showOptionDialog(null, "Please choose one of the following:", "Learning Management System",
                        JOptionPane.PLAIN_MESSAGE, 1, null, options, null);
                while (userSelection == -1) {
                    invalidInputMessage();
                    userSelection = JOptionPane.showOptionDialog(null, "Please choose one of the following:", "Learning Management System",
                            JOptionPane.PLAIN_MESSAGE, 1, null, options, null);
                }
                userSelection = userSelection + 1;
                objectOutputStream.writeObject(userSelection);
                objectOutputStream.flush();

            } while (userSelection > 0 && userSelection < 3);

            JOptionPane.showMessageDialog(null, "Thank you. Bye!", "Learning Management System", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    // Method for the implementation of the functions if students chooses to select a course in initial student menu
    public static void studentSelection1GUI() throws Exception {

        String coursesStatus = (String) objectInputStream.readObject();

        if (coursesStatus.equals("no courses")) {
            JOptionPane.showMessageDialog(null, "No Courses to select!",
                    "Learning Management System",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (coursesStatus.equals("courses exist")) {
            boolean noDiscussionForums;
            int coursesSize = (Integer) objectInputStream.readObject();
            String[] courseArray = new String[coursesSize];
            for (int i = 0; i < coursesSize; i++) {
                courseArray[i] = (String) objectInputStream.readObject();
            }

            String courseName1 = (String) JOptionPane.showInputDialog(null, "Select Course", "Learning Management System",
                    JOptionPane.PLAIN_MESSAGE, null, courseArray, null);

            while (courseName1 == null) {
                invalidInputMessage();
                courseName1 = (String) JOptionPane.showInputDialog(null, "Select Course", "Learning Management System",
                        JOptionPane.PLAIN_MESSAGE, null, courseArray, null);
            }

            int courseNumberSelected = -1;
            for(int i = 0; i < courseArray.length; i++) {
                if(courseArray[i].equals(courseName1)) {
                    courseNumberSelected = i + 1;
                    break;
                }
            }

            objectOutputStream.writeObject(courseNumberSelected);
            objectOutputStream.flush();

            courseName1 = (String) objectInputStream.readObject();



            noDiscussionForums = (Boolean) objectInputStream.readObject();

            if (noDiscussionForums) {
                noDiscussionGUI();
                //break;
            } else {

                String[] options13 = {"Select a Discussion Forum", "Exit"};

                int studentSelection2 = JOptionPane.showOptionDialog(null, courseName1 + ":", "Learning Management System",
                        JOptionPane.PLAIN_MESSAGE, 1, null, options13, null) + 1;

                while (studentSelection2 == 0) {
                    invalidInputMessage();
                    studentSelection2 = JOptionPane.showOptionDialog(null, courseName1 + ":", "Learning Management System",
                            JOptionPane.PLAIN_MESSAGE, 1, null, options13, null) + 1;
                }

                objectOutputStream.writeObject(studentSelection2);
                objectOutputStream.flush();

                if (studentSelection2 == 1) {
                    ArrayList<String> topic1 = new ArrayList<>();
                    // ArrayList to retrieve the DiscussionTopic corresponding to the number selected
                    ArrayList<String> ensureNoRepetition = new ArrayList<>();
                    // ArrayList to ensure a DiscussionTopic is not printed more than once
                    String topic2 = "";
                    int counter = 1;

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Course Name: " + courseName1);
                    stringBuilder.append("\n");

                    ArrayList<Post> posts = new ArrayList<>();
                    int postsSize = (Integer) objectInputStream.readObject();
                    for (int i = 0; i < postsSize; i++) {
                        posts.add(stringToPost((String) objectInputStream.readObject()));
                    }

                    ArrayList<String> discussionMenu = new ArrayList<>();
                    boolean noDisc = false;

                    for (int i = 0; i < posts.size(); i++) {

                        //If there is an object that has the selected course name and DiscussionTopic object set to null
                        //It means there are no Discussion Forums for that course
                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)
                                && posts.get(i).getDiscussionTopic() == null) {
                            stringBuilder.append("No Discussion Forums for this course.");
                            stringBuilder.append("\n");
                            noDisc = true;
                            break;
                        } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                            // Printing the Discussion Forum of the course selected
                            if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                                //To ensure a Discussion Forum is not printed more than once
                                topic2 = "Discussion #" + (counter) + " " + posts.get(i).getDiscussionTopic().getTopic();
                                discussionMenu.add("Discussion #" + (counter));
                                stringBuilder.append(stringInLinesMethod(topic2));
                                stringBuilder.append("\n");
                                stringBuilder.append(posts.get(i).getDiscussionTopic().getTimeStamp());
                                stringBuilder.append("\n" + "\n");
                                ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                                topic1.add(topic2);
                                counter++;
                            }
                        }
                    }

                    if (noDisc) {
                        JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Learning Management System",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        String discussionSelected = (String) JOptionPane.showInputDialog(null, stringBuilder.toString(), "Learning Management System",
                                JOptionPane.PLAIN_MESSAGE, null, discussionMenu.toArray(), null);

                        while (discussionSelected == null) {
                            invalidInputMessage();
                            discussionSelected = (String) JOptionPane.showInputDialog(null, stringBuilder.toString(), "Learning Management System",
                                    JOptionPane.PLAIN_MESSAGE, null, discussionMenu.toArray(), null);
                        }

                        int discussionForumSelected1 = discussionMenu.indexOf(discussionSelected) + 1;

                        String identify3 = "#" + discussionForumSelected1 + " ";


                        if (topic1 != null) {
                            for (int i = 0; i < topic1.size(); i++) {
                                if (topic1.get(i).contains("Discussion #" + discussionForumSelected1 + " ")) {
                                    topic2 = topic1.get(i).substring(
                                            topic1.get(i).indexOf("#" + discussionForumSelected1 + " ")
                                                    + identify3.length());
                                }
                            }
                        }
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1.append("Course Name: " + courseName1);
                        stringBuilder1.append("\n" + "\n");

                        // Printing the Discussion Forum selected
                        for (int i = 0; i < posts.size(); i++) {
                            if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                    posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)) {
                                stringBuilder1.append(stringInLinesMethod(posts.get(i).getDiscussionTopic().getTopic()));
                                stringBuilder1.append("\n");
                                stringBuilder1.append(posts.get(i).getDiscussionTopic().getTimeStamp());
                                stringBuilder1.append("\n" + "\n");
                                break;
                            }
                        }

                        objectOutputStream.writeObject(topic2);
                        objectOutputStream.flush();

                        int responseNumber = 1;
                        String response = "";

                        ArrayList<String> responses = new ArrayList<>();
                        ArrayList<String> responseMenu = new ArrayList<>();
                        boolean noResponses = false;

                        for (int i = 0; i < posts.size(); i++) {

                            // Checking if there are no responses
                            // If there is an object that has the selected course and discussion forum
                            // and null Post object it means there are no posts for that forum
                            if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                    posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)
                                    && posts.get(i).getPost() == null) {
                                stringBuilder1.append("No Responses for this Discussion Forum!");
                                stringBuilder1.append("\n");
                                noResponses = true;
                            } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                    posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)) {
                                response = "Response #" + responseNumber + " " + posts.get(i).getPost();
                                responseMenu.add("Response #" + responseNumber);
                                responses.add(response);
                                stringBuilder1.append(stringInLinesMethod(response));
                                stringBuilder1.append("\n");
                                stringBuilder1.append(posts.get(i).getStudent().getUsername()
                                        + "  " + posts.get(i).getTimeStamp());
                                stringBuilder1.append("\n");
                                if (posts.get(i).getReply() != null) {
                                    stringBuilder1.append("Comments on the post:");
                                    stringBuilder1.append("\n");
                                    for (int j = 0; j < posts.get(i).getReply().size(); j++) {
                                        //System.out.println(postArrayList.get(i).getReply().get(j).getResponse());
                                        stringBuilder1.append(posts.get(i).getReply().get(j).getPerson().getUsername() + "  " +
                                                posts.get(i).getReply().get(j).getTimeStamp()
                                                + ": " + posts.get(i).getReply().get(j).getResponse());
                                        stringBuilder1.append("\n");

                                    }
                                }
                                responseNumber++;
                                stringBuilder1.append("\n");
                            }
                        }

                        objectOutputStream.writeObject(noResponses);
                        objectOutputStream.flush();

                        if(noResponses) {
                            JOptionPane.showMessageDialog(null, stringBuilder1.toString(), "Learning Management System",
                                    JOptionPane.PLAIN_MESSAGE);
                        } else {
                            String responseStringSelected = (String) JOptionPane.showInputDialog(null, stringBuilder1.toString(), "Learning Management System",
                                    JOptionPane.PLAIN_MESSAGE, null, responseMenu.toArray(), null);

                            while (responseStringSelected == null) {
                                invalidInputMessage();
                                responseStringSelected = (String) JOptionPane.showInputDialog(null, stringBuilder1.toString(), "Learning Management System",
                                        JOptionPane.PLAIN_MESSAGE, null, responseMenu.toArray(), null);
                            }

                            int replyNumber = responseMenu.indexOf(responseStringSelected) + 1;

                            String identify1 = "#" + replyNumber + " ";
                            // To retrieve the post string corresponding to the number selected
                            if (responses != null) {
                                for (int i = 0; i < responses.size(); i++) {
                                    if (responses.get(i).contains("Response #" + replyNumber + " ")) {
                                        response = responses.get(i).substring(responses.get(i).indexOf("#" + replyNumber + " ")
                                                + identify1.length());
                                    }
                                }
                            }
                        }

                        String[] options14 = {"Create a Post", "Comment to the Post", "Exit"};

                        int studentSelection3 = JOptionPane.showOptionDialog(null, "Please choose one of the following:", "Learning Management System",
                                JOptionPane.PLAIN_MESSAGE, 1 , null, options14, null) + 1;

                        while (studentSelection3 == 0) {
                            invalidInputMessage();
                            studentSelection3 = JOptionPane.showOptionDialog(null, "Please choose one of the following:", "Learning Management System",
                                    JOptionPane.PLAIN_MESSAGE, 1 , null, options14, null) + 1;

                        }

                        objectOutputStream.writeObject(studentSelection3);
                        objectOutputStream.flush();

                        if (studentSelection3 == 1) {
                            String studentPost = "";
                            boolean samePost = false;
                            int choice = 0;

                            // Checking if the post provided already exists
                            do {

                                samePost = false;

                                //System.out.println("1. Manually input a reply");
                                //System.out.println("2. Import a reply from a file");

                                String[] options = {" Manually input a reply", "Import a reply from a file"};
                                int x = JOptionPane.showOptionDialog(null, "Learning Management System", "Please choose one of the following:", JOptionPane.PLAIN_MESSAGE, 1, null, options, null);

                                while (x == -1) {
                                    invalidInputMessage();
                                    x = JOptionPane.showOptionDialog(null, "Learning Management System", "Please choose one of the following:", JOptionPane.PLAIN_MESSAGE, 1, null, options, null);
                                }

                                if (x == 1) {

                                    //Import data from a file
                                    ArrayList<String> data = new ArrayList<>();
                                    boolean fileCheck;
                                    do {
                                        fileCheck = true;
                                        // ensuring the data from the file is imported
                                        // will loop until the data from file is imported properly
                                        try {
                                            String fileName = JOptionPane.showInputDialog(null,
                                                    "Please enter the name of the file you would like to import",
                                                    "Learning Management System", JOptionPane.QUESTION_MESSAGE);
                                            data = fileImporter(fileName);
                                            if (data.isEmpty()) {
                                                JOptionPane.showMessageDialog(null, "The file is empty!", "Learning Management System",
                                                        JOptionPane.ERROR_MESSAGE);
                                                fileCheck = false;
                                            }
                                        } catch (FileNotFoundException fileNotFoundException) {
                                            JOptionPane.showMessageDialog(null, "The filename entered doesn't exist!", "Learning Management System",
                                                    JOptionPane.ERROR_MESSAGE);
                                            fileCheck = false;
                                        } catch (IOException ioException) {
                                            JOptionPane.showMessageDialog(null, "There was an error in importing the file provided!", "Learning Management System",
                                                    JOptionPane.ERROR_MESSAGE);
                                            fileCheck = false;
                                        } catch (Exception exception) {
                                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter the filename.", "Learning Management System",
                                                    JOptionPane.ERROR_MESSAGE);
                                            fileCheck = false;
                                        }
                                    } while (!fileCheck);

                                    //Once file has been successfully imported,
                                    // use sb to make it useful for this scope
                                    StringBuilder sb = new StringBuilder();

                                    //this takes the entire ArrayList that was imported
                                    // from the file and makes it into a string
                                    for (int length = data.size(); length > 0; length--) {
                                        sb.append("\n");
                                        sb.append(data.get(0));
                                        data.remove(0);
                                    }

                                    studentPost = sb.toString();

                                } else if (x == 0) {
                                    // If they choose to manually input the response/post
                                    //System.out.println("Enter the content for the post below:");
                                    studentPost = JOptionPane.showInputDialog(null, "Enter the content for the post below:", "Learning Management System", JOptionPane.QUESTION_MESSAGE);
                                }
                                //Checking for valid input
                                while (studentPost == null || studentPost.equals("")) {
                                    JOptionPane.showMessageDialog(null, "Invalid input! " +
                                            "Please enter the content for the post:","Learning Management System", JOptionPane.ERROR_MESSAGE);
                                    studentPost = JOptionPane.showInputDialog(null, "Enter the content for the post below:", "Learning Management System", JOptionPane.QUESTION_MESSAGE);
                                }

                                //Checking if the post already exists

                                objectOutputStream.writeObject(studentPost);
                                objectOutputStream.flush();

                                samePost = (boolean) objectInputStream.readObject();

                                if (samePost) {
                                    //System.out.println("The post already exists!");
                                    JOptionPane.showMessageDialog(null, "The post already exists!","Learning Management System", JOptionPane.ERROR_MESSAGE);
                                }

                                // Will loop until a valid response is provided
                            } while (samePost);

                            JOptionPane.showMessageDialog(null,"Your post has been added!" ,"Learning Management System", JOptionPane.INFORMATION_MESSAGE);

                        } else if (studentSelection3 == 2) {
                            if (noResponses) {
                                JOptionPane.showMessageDialog(null, "No Responses for this discussion forum!","Learning Management System", JOptionPane.ERROR_MESSAGE);

                            } else {
                                objectOutputStream.writeObject(response);
                                objectOutputStream.flush();

                                commentOnStudentPostsGUI();
                            }
                        } else if (studentSelection3 == 3) {
                            //Exit the current menu
                        }
                    }
                } else if (studentSelection2 == 2) {
                    //Exit the current menu
                }
            }
        }
    }

    //Method to create course used by Teacher
    public static void createCourseGUI() throws Exception {
        String courseName = JOptionPane.showInputDialog(null,
                "Enter a course name", "Learning Management System", JOptionPane.QUESTION_MESSAGE);

        while (courseName == null || courseName.equals("")) {
            JOptionPane.showMessageDialog(null, "Invalid! Please enter a course name" , "Learning Management System",
                    JOptionPane.ERROR_MESSAGE);
            courseName = JOptionPane.showInputDialog(null,
                    "Enter a course name", "Learning Management System", JOptionPane.QUESTION_MESSAGE);
        }

        objectOutputStream.writeObject(courseName);
        objectOutputStream.flush();

        boolean sameCourse = (Boolean) objectInputStream.readObject();

        if (sameCourse) {
            JOptionPane.showMessageDialog(null, "Course already exists!", "Learning Management System",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Course has been created!", "Learning Management System",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }


    //Method to create discussion forum used by Teacher user.
    public static void createDiscussionGUI() throws Exception {
        boolean checkSameDiscussionForum;
        do {
            String[] options = {"Manually create discussion forum", "Import discussion forum from file"};
            int x = JOptionPane.showOptionDialog(null, "Learning Management System", "Please choose one of the following:", JOptionPane.PLAIN_MESSAGE, 1, null, options, null);
            while (x == -1) {
                invalidInputMessage();
                x = JOptionPane.showOptionDialog(null, "Learning Management System", "Please choose one of the following:", JOptionPane.PLAIN_MESSAGE, 1, null, options, null);
            }
            String topic = "";
            if (x == 0) {
                String header = JOptionPane.showInputDialog(null, "Enter the topic for Discussion Forum", "Learning Management System", JOptionPane.QUESTION_MESSAGE);

                //Checking for valid input
                while (header == null || header.equals("")) {
                    JOptionPane.showMessageDialog(null, "Invalid input! Please enter topic for discussion Forum", "Learning Management System", JOptionPane.ERROR_MESSAGE);
                    header = JOptionPane.showInputDialog(null, "Enter the topic for Discussion Forum", "Learning Management System", JOptionPane.QUESTION_MESSAGE);
                }

                //System.out.println("Enter the prompt");
                String content = JOptionPane.showInputDialog(null, "Enter the prompt", "Learning Management System", JOptionPane.QUESTION_MESSAGE);

                // Checking for valid input
                while (content == null || content.equals("")) {
                    JOptionPane.showMessageDialog(null, "Invalid input! Please enter prompt for discussion Forum", "Learning Management System", JOptionPane.ERROR_MESSAGE);
                    content = JOptionPane.showInputDialog(null, "Enter the prompt", "Learning Management System", JOptionPane.QUESTION_MESSAGE);
                }

                topic = header + "\n" + content; // Topic includes header and the prompt
            } else if (x == 1) {
                //Import data from a file
                ArrayList<String> data = new ArrayList<>();
                boolean fileCheck;
                do {
                    fileCheck = true;

                    try {
                        //System.out.println("Please enter the name of the file you would like to import");
                        String fileName = JOptionPane.showInputDialog(null,
                                "Please enter the name of the file you would like to import",
                                "Learning Management System", JOptionPane.QUESTION_MESSAGE);
                        data = fileImporter(fileName);
                        if (data.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "The file is empty!", "Learning Management System",
                                    JOptionPane.ERROR_MESSAGE);
                            fileCheck = false;
                        }
                    } catch (FileNotFoundException fileNotFoundException) {
                        //System.out.println("The filename entered doesn't exist!");
                        JOptionPane.showMessageDialog(null, "The filename entered doesn't exist!", "Learning Management System",
                                JOptionPane.ERROR_MESSAGE);
                        fileCheck = false;
                    } catch (IOException ioException) {
                        // System.out.println("There was an error in importing the file provided!");
                        JOptionPane.showMessageDialog(null, "There was an error in importing the file provided!", "Learning Management System",
                                JOptionPane.ERROR_MESSAGE);
                        fileCheck = false;
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter the filename.", "Learning Management System",
                                JOptionPane.ERROR_MESSAGE);
                        fileCheck = false;
                    }
                } while (!fileCheck);

                //Once file has been successfully imported, organize it a little and make it useful

                String header = titleGetter(data);
                StringBuilder sb = new StringBuilder();

                //this removes the title from the array since it has already been dealt with
                data.remove(0);

                //this takes the ArrayList that was imported from the file and makes it into a string
                for (int length = data.size(); length > 0; length--) {
                    sb.append("\n");
                    sb.append(data.get(0));
                    data.remove(0);
                }

                String content = sb.toString();

                topic = header + content;
            }

            objectOutputStream.writeObject(topic);
            objectOutputStream.flush();

            checkSameDiscussionForum = (Boolean) objectInputStream.readObject();

            if (checkSameDiscussionForum) {
                JOptionPane.showMessageDialog(null, "The Discussion Forum entered already exists! " +
                        "Please enter different topic and prompt!", "Learning Management System", JOptionPane.ERROR_MESSAGE);
            }

        } while (checkSameDiscussionForum);

        JOptionPane.showMessageDialog(null, "The Discussion Forum has been created!", "Learning Management System",
                JOptionPane.INFORMATION_MESSAGE);
    }

    //Method to display GUI if there are no discussion forums for the course selected
    public static void noDiscussionGUI() {
        JOptionPane.showMessageDialog(null, "No Discussion Forums for the Course selected!" ,
                "Learning Management System",
                JOptionPane.ERROR_MESSAGE);
    }

    //Method to grade students used by Teacher
    public static void gradeStudentGUI() throws Exception {
        ArrayList<String> studentUsernameArrayList = new ArrayList<>();
        int studentUsernameArrayListSize = (Integer) objectInputStream.readObject();
        for (int j = 0; j < studentUsernameArrayListSize; j++) {
            studentUsernameArrayList.add((String) objectInputStream.readObject());
        }

        if (studentUsernameArrayList.size() == 0) {
            //System.out.println("There are no student users to grade!");
            JOptionPane.showMessageDialog(null, "There are no student users to grade!",
                    "Learning Management System", JOptionPane.ERROR_MESSAGE);
        } else {
            String[] options10 = new String[studentUsernameArrayList.size()];
            for (int i = 0; i < studentUsernameArrayList.size(); i++) {
                //System.out.println((i + 1) + ". " + studentUsernameArrayList.get(i));
                options10[i] = studentUsernameArrayList.get(i);
            }
            String studentUsernameSelected =  (String) JOptionPane.showInputDialog(null, "Select the student to grade", "Learning Management System",
                    JOptionPane.PLAIN_MESSAGE, null, options10, null);

            while (studentUsernameSelected == null) {
                invalidInputMessage();
                studentUsernameSelected =  (String) JOptionPane.showInputDialog(null, "Select the student to grade", "Learning Management System",
                        JOptionPane.PLAIN_MESSAGE, null, options10, null);
            }

            int studentSelected = studentUsernameArrayList.indexOf(studentUsernameSelected) + 1;
            //System.out.println(studentSelected);
            objectOutputStream.writeObject(studentSelected);
            objectOutputStream.flush();

            int counter = 0;     // Counter to check how many posts are made by the Student
            int postNumber = 1;  // PostNumber for serializing the posts

            ArrayList<Post> studentPosts = new ArrayList<>();
            int studentPostSize = (Integer) objectInputStream.readObject();
            for (int j = 0; j < studentPostSize; j++) {
                studentPosts.add(stringToPost((String) objectInputStream.readObject()));
            }

            if (studentPosts.size() == 0) {//Print line if there are no posts made by the Student
                //System.out.println("The Student User selected has not made any posts!");
                JOptionPane.showMessageDialog(null, "The Student User selected has not made any posts!",
                        "Learning Management System", JOptionPane.ERROR_MESSAGE);
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < studentPosts.size(); i++) {
                    sb.append("#" + postNumber++);
                    sb.append("\n");
                    sb.append("Course Name: " + studentPosts.get(i).getCourseName());
                    sb.append("\n");
                    String s = "Discussion Forum: " + studentPosts.get(i).getDiscussionTopic().getTopic();
                    sb.append(stringInLinesMethod(s));
                    sb.append("\n");
                    sb.append(studentPosts.get(i).getDiscussionTopic().getTimeStamp());
                    sb.append("\n");
                    sb.append("Response: " + studentPosts.get(i).getPost());
                    sb.append("\n");
                    sb.append(studentPosts.get(i).getTimeStamp());
                    sb.append("\n" + "\n");
                }

                int postNumber1 = 1;

                for (int i = 0; i < studentPosts.size(); i++) {

                    //System.out.println("Please enter the grade for the Post #" + postNumber1++);


                    double grade1 = 0;      // The grade value will be between 0.0 and 10.0
                    boolean notAnInteger17; // To ensure valid input is put by the user


                    do {
                        notAnInteger17 = false;
                        try {
                            grade1 = Double.parseDouble(JOptionPane.showInputDialog(null, sb.toString() + "Please enter the grade for the Post #" + postNumber1,
                                    "Learning Management System", JOptionPane.PLAIN_MESSAGE));

                            while (grade1 < 0.0 || grade1 > 10.0) {
                                //System.out.println("Please enter a grade value between 0.0 to 10.0");
                                JOptionPane.showMessageDialog(null, "Please enter a grade value between 0.0 to 10.0",
                                        "Learning Management System", JOptionPane.ERROR_MESSAGE);
                                grade1 = Double.parseDouble(JOptionPane.showInputDialog(null, sb.toString() + "Please enter the grade for the Post #" + postNumber1,
                                        "Learning Management System", JOptionPane.PLAIN_MESSAGE));
                            }

                        } catch (NumberFormatException numberFormatException) {
                            //System.out.println("Invalid input! Please enter a grade value between 0.0 to 10.0");
                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a grade value between 0.0 to 10.0",
                                    "Learning Management System", JOptionPane.ERROR_MESSAGE);
                            notAnInteger17 = true;

                        } catch (Exception exception) {
                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a grade value between 0.0 to 10.0",
                                    "Learning Management System", JOptionPane.ERROR_MESSAGE);
                            notAnInteger17 = true;
                        }
                    } while (notAnInteger17);
                    postNumber1++;
                    objectOutputStream.writeObject(grade1);
                    objectOutputStream.flush();
                }

                JOptionPane.showMessageDialog(null, "The Student User selected has been graded!",
                        "Learning Management System", JOptionPane.INFORMATION_MESSAGE);

            }

        }
    }

    //Method to delete a discussion forum used by Teacher
    public static void deleteDiscussionGUI(boolean noDiscussionForums, String courseName1) throws Exception {
        if(noDiscussionForums != true) {
            ArrayList<String> deleteDiscussion = new ArrayList<String>();
            // ArrayList to retrieve the DiscussionTopic corresponding to the number selected
            ArrayList<String> ensureNoRepetition = new ArrayList<>();
            // ArrayList to ensure a DiscussionTopic is not printed more than once
            String s = "";
            int counter = 1; // counter for serializing the Discussion Forums printed

            ArrayList<String> discussionMenu = new ArrayList<>();

            ArrayList<Post> posts = new ArrayList<>();
            int postsSize = (Integer) objectInputStream.readObject();
            for (int i = 0; i < postsSize; i++) {
                posts.add(stringToPost((String) objectInputStream.readObject()));
            }
            boolean noDisc = false;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Course Name: " + courseName1);
            stringBuilder.append("\n");

            for (int i = 0; i < posts.size(); i++) {

                //If there is an object that has the selected course name and DiscussionTopic object set to null
                //It means there are no Discussion Forums for that course
                if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)
                        && posts.get(i).getDiscussionTopic() == null) {
                    stringBuilder.append("No Discussion Forums for this course.");
                    stringBuilder.append("\n");
                    noDisc = true;
                    break;
                } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                    // Printing the Discussion Forum of the course selected
                    if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                        //To ensure a Discussion Forum is not printed more than once
                        s = "Discussion #" + (counter) + " " + posts.get(i).getDiscussionTopic().getTopic();
                        discussionMenu.add("Discussion #" + (counter));
                        stringBuilder.append(stringInLinesMethod(s));
                        stringBuilder.append("\n");
                        stringBuilder.append(posts.get(i).getDiscussionTopic().getTimeStamp());
                        stringBuilder.append("\n" + "\n");
                        ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                        deleteDiscussion.add(s);
                        counter++;
                    }
                }
            }

            if(noDisc) {
                JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Learning Management System",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                String discussionSelected = (String) JOptionPane.showInputDialog(null, stringBuilder.toString(), "Learning Management System",
                        JOptionPane.PLAIN_MESSAGE, null, discussionMenu.toArray(), null);

                while (discussionSelected == null) {
                    invalidInputMessage();
                    discussionSelected = (String) JOptionPane.showInputDialog(null, stringBuilder.toString(), "Learning Management System",
                            JOptionPane.PLAIN_MESSAGE, null, discussionMenu.toArray(), null);
                }

                int discussionForumNumber = discussionMenu.indexOf(discussionSelected) + 1;

                String identify2 = "#" + discussionForumNumber + " ";

                if (deleteDiscussion != null) {
                    for (int i = 0; i < deleteDiscussion.size(); i++) {
                        if (deleteDiscussion.get(i).contains("Discussion #" + discussionForumNumber + " ")) {
                            s = deleteDiscussion.get(i).substring(
                                    deleteDiscussion.get(i).indexOf("#" + discussionForumNumber + " ")
                                            + identify2.length());
                        }
                    }
                }
                try {
                    objectOutputStream.writeObject(s);
                    objectOutputStream.flush();
                }catch (Exception exception) {
                    exception.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, "The Discussion Forum has been deleted!",
                        "Learning Management System", JOptionPane.INFORMATION_MESSAGE);

            }

        } else if (noDiscussionForums) {
            noDiscussionGUI();
        }
    }

    //Method to delete a discussion forum used by Teacher
    public static void editDiscussionGUI(boolean noDiscussionForums, String courseName1) throws Exception {

        if(noDiscussionForums != true) {
            ArrayList<String> editDiscussion = new ArrayList<>();
            // ArrayList to retrieve the DiscussionTopic corresponding to the number selected
            ArrayList<String> ensureNoRepetition = new ArrayList<>();
            // ArrayList to ensure a DiscussionTopic is not printed more than once
            ArrayList<String> discussionMenu = new ArrayList<>();
            String topic = "";
            int counter = 1;  // counter for serializing the Discussion Forums printed

            //System.out.println("Course Name: " + courseName1);
            //System.out.println();

            ArrayList<Post> posts = new ArrayList<>();
            int postsSize = (Integer) objectInputStream.readObject();
            for (int i = 0; i < postsSize; i++) {
                posts.add(stringToPost((String) objectInputStream.readObject()));
            }

            boolean noDisc = false;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Course Name: " + courseName1);
            stringBuilder.append("\n");

            for (int i = 0; i < posts.size(); i++) {

                //If there is an object that has the selected course name and DiscussionTopic object set to null
                //It means there are no Discussion Forums for that course
                if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)
                        && posts.get(i).getDiscussionTopic() == null) {
                    stringBuilder.append("No Discussion Forums for this course.");
                    stringBuilder.append("\n");
                    noDisc = true;
                    break;
                } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                    // Printing the Discussion Forum of the course selected
                    if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                        //To ensure a Discussion Forum is not printed more than once
                        topic = "Discussion #" + (counter) + " " + posts.get(i).getDiscussionTopic().getTopic();
                        discussionMenu.add("Discussion #" + (counter));
                        stringBuilder.append(stringInLinesMethod(topic));
                        stringBuilder.append("\n");
                        stringBuilder.append(posts.get(i).getDiscussionTopic().getTimeStamp());
                        stringBuilder.append("\n" + "\n");
                        ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                        editDiscussion.add(topic);
                        counter++;
                    }
                }
            }

            if (noDisc) {
                JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Learning Management System",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                String discussionSelected = (String) JOptionPane.showInputDialog(null, stringBuilder.toString(), "Learning Management System",
                        JOptionPane.PLAIN_MESSAGE, null, discussionMenu.toArray(), null);

                //Checking for valid input
                while (discussionSelected == null) {
                    invalidInputMessage();
                    discussionSelected = (String) JOptionPane.showInputDialog(null, stringBuilder.toString(), "Learning Management System",
                            JOptionPane.PLAIN_MESSAGE, null, discussionMenu.toArray(), null);
                }

                int discussionForumSelected1 = discussionMenu.indexOf(discussionSelected) + 1;

                String identify3 = "#" + discussionForumSelected1 + " ";


                if (editDiscussion != null) {
                    for (int i = 0; i < editDiscussion.size(); i++) {
                        if (editDiscussion.get(i).contains("Discussion #" + discussionForumSelected1 + " ")) {
                            topic = editDiscussion.get(i).substring(
                                    editDiscussion.get(i).indexOf("#" + discussionForumSelected1 + " ")
                                            + identify3.length());
                        }
                    }
                }

                objectOutputStream.writeObject(topic);
                objectOutputStream.flush();

                boolean checkSameDiscussionForum = false;
                String newTopic;

                do {
                    checkSameDiscussionForum = false;

                    //System.out.println("Enter the edited Discussion Topic: ");
                    String header = JOptionPane.showInputDialog(null,
                            "Enter the edited Discussion Topic:",
                            "Learning Management System", JOptionPane.QUESTION_MESSAGE);

                    while (header == null || header.equals("")) {
                        //System.out.println("Invalid input! Please enter topic for Discussion forum!");
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter topic for Discussion forum!",
                                "Learning Management System", JOptionPane.ERROR_MESSAGE);
                        header = JOptionPane.showInputDialog(null,
                                "Enter the edited Discussion Topic:",
                                "Learning Management System", JOptionPane.QUESTION_MESSAGE);
                    }

                    //System.out.println("Enter the edited Discussion Forum prompt: ");
                    String prompt = JOptionPane.showInputDialog(null,
                            "Enter the edited Discussion Forum prompt:",
                            "Learning Management System", JOptionPane.QUESTION_MESSAGE);

                    while (prompt == null || prompt.equals("")) {
                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter prompt for Discussion forum!",
                                "Learning Management System", JOptionPane.ERROR_MESSAGE);
                        prompt = JOptionPane.showInputDialog(null,
                                "Enter the edited Discussion Forum prompt:",
                                "Learning Management System", JOptionPane.QUESTION_MESSAGE);
                    }

                    newTopic = header + "\n" + prompt;
                    // Combining the header and prompt to form a Discussion Forum


                    for (int i = 0; i < posts.size(); i++) {
                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                posts.get(i).getDiscussionTopic() != null &&
                                posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(newTopic)) {
                            checkSameDiscussionForum = true;
                            //System.out.println("The Discussion Forum entered already exists! " +
                            //"Please enter a different topic and prompt!");
                            JOptionPane.showMessageDialog(null, "The Discussion Forum entered already exists! " +
                                            "Please enter a different topic and prompt!",
                                    "Learning Management System", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                } while (checkSameDiscussionForum);

                objectOutputStream.writeObject(newTopic);
                objectOutputStream.flush();

                JOptionPane.showMessageDialog(null, "The Discussion Forum has been edited!",
                        "Learning Management System", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (noDiscussionForums) {
            noDiscussionGUI();
        }

    }

    //Method to comment on a Student Post used by both Student and Teacher
    public static void commentOnStudentPostsGUI() throws Exception{
        String reply = JOptionPane.showInputDialog(null, "Enter the reply to be added:",
                "Learning Management System", JOptionPane.QUESTION_MESSAGE);

        // Checking for valid input
        while (reply == null || reply.equals("")) {
            //System.out.println("Invalid input! Please enter the reply for the post!");
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter the reply for the post!",
                    "Learning Management System" , JOptionPane.ERROR_MESSAGE);
            reply = JOptionPane.showInputDialog(null, "Enter the reply to be added:",
                    "Learning Management System", JOptionPane.QUESTION_MESSAGE);
        }

        objectOutputStream.writeObject(reply);
        objectOutputStream.flush();

        JOptionPane.showMessageDialog(null, "Your reply has been added!",
                "Learning Management System", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to edit a Student Post used by the teacher
    public static void editStudentPostGUI() throws Exception {
        boolean checkSamePost = false;
        String editedResponse;
        // checking if the edited post already exits
        // will loop until a valid input is provided
        do {
            //checkSamePost = false;

            //System.out.println("Enter the edited Response Post:");
            editedResponse = JOptionPane.showInputDialog(null,
                    "Enter the edited Response Post:",
                    "Learning Management System",
                    JOptionPane.QUESTION_MESSAGE);

            while (editedResponse == null || editedResponse.equals("")) {
                //System.out.println("Invalid input! Please enter the reply for the post!");
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter the reply for the post!",
                        "Learning Management System",
                        JOptionPane.ERROR_MESSAGE);
                editedResponse = JOptionPane.showInputDialog(null,
                        "Enter the edited Response Post:",
                        "Learning Management System",
                        JOptionPane.QUESTION_MESSAGE);
            }

            objectOutputStream.writeObject(editedResponse);
            objectOutputStream.flush();

            checkSamePost = (Boolean) objectInputStream.readObject();

            if (checkSamePost) {
                //System.out.println("The post already exists! ");
                JOptionPane.showMessageDialog(null, "The post already exists! ",
                        "Learning Management System",
                        JOptionPane.ERROR_MESSAGE);
            }

        } while (checkSamePost);

        JOptionPane.showMessageDialog(null,
                "The Student Post has been edited!",
                "Learning Management System",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Method for the implementation of the functions if teacher selects option2 in the initial Teacher menu
    public static void teacherOption2() throws Exception {
        String coursesStatus = (String) objectInputStream.readObject();
        if (coursesStatus.equals("no courses")) {
            JOptionPane.showMessageDialog(null, "No Courses to select!",
                    "Learning Management System",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (coursesStatus.equals("courses exist")) {
            boolean noDiscussionForums;
            int coursesSize = (Integer) objectInputStream.readObject();
            String[] courseArray = new String[coursesSize];
            for (int i = 0; i < coursesSize; i++) {
                courseArray[i] = (String) objectInputStream.readObject();
            }

            String courseName1 = (String) JOptionPane.showInputDialog(null, "Select Course", "Learning Management System",
                    JOptionPane.PLAIN_MESSAGE, null, courseArray, null);

            while (courseName1 == null) {
                invalidInputMessage();
                courseName1 = (String) JOptionPane.showInputDialog(null, "Select Course", "Learning Management System",
                        JOptionPane.PLAIN_MESSAGE, null, courseArray, null);
            }

            int courseNumberSelected = -1;
            for(int i = 0; i < courseArray.length; i++) {
                if(courseArray[i].equals(courseName1)) {
                    courseNumberSelected = i + 1;
                    break;
                }
            }

            objectOutputStream.writeObject(courseNumberSelected);
            objectOutputStream.flush();

            courseName1 = (String) objectInputStream.readObject();

            noDiscussionForums = (Boolean) objectInputStream.readObject();

            String[] options6 = {"Create a Discussion Forum", "Delete a Discussion Forum", "Edit a Discussion Forum",
                    "View Students responses", "Exit"};

            int teacherSelection2 = JOptionPane.showOptionDialog(null, "Course: " + courseName1, "Learning Management System",
                    JOptionPane.PLAIN_MESSAGE, 1, null, options6, options6[0]) + 1;
            while (teacherSelection2 == 0) {
                invalidInputMessage();
                teacherSelection2 = JOptionPane.showOptionDialog(null, "Course: " + courseName1, "Learning Management System",
                        JOptionPane.PLAIN_MESSAGE, 1, null, options6, options6[0]) + 1;
            }
            objectOutputStream.writeObject(teacherSelection2);
            objectOutputStream.flush();

            if (teacherSelection2 == 1) {
                createDiscussionGUI();
            } else if (teacherSelection2 == 2) {
                deleteDiscussionGUI(noDiscussionForums, courseName1);
            } else if (teacherSelection2 == 3) {
                editDiscussionGUI(noDiscussionForums, courseName1);
            } else if(teacherSelection2 == 4) {
                if(noDiscussionForums != true) {
                    ArrayList<String> topic1 = new ArrayList<>();
                    // ArrayList to retrieve the DiscussionTopic corresponding to the number selected
                    ArrayList<String> ensureNoRepetition = new ArrayList<>();
                    // ArrayList to ensure a DiscussionTopic is not printed more than once
                    ArrayList<String> discussionMenu = new ArrayList<>();
                    String topic2 = "";
                    int counter = 1;

                    //System.out.println("Course Name: " + courseName1);
                    //System.out.println();

                    ArrayList<Post> posts = new ArrayList<>();
                    int postsSize = (Integer) objectInputStream.readObject();
                    for (int i = 0; i < postsSize; i++) {
                        posts.add(stringToPost((String) objectInputStream.readObject()));
                    }

                    boolean noDisc = false;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Course Name: " + courseName1);
                    stringBuilder.append("\n");

                    for (int i = 0; i < posts.size(); i++) {

                        //If there is an object that has the selected course name and DiscussionTopic object set to null
                        //It means there are no Discussion Forums for that course
                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)
                                && posts.get(i).getDiscussionTopic() == null) {
                            stringBuilder.append("No Discussion Forums for this course.");
                            stringBuilder.append("\n");
                            noDisc = true;
                            break;
                        } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                            // Printing the Discussion Forum of the course selected
                            if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                                //To ensure a Discussion Forum is not printed more than once
                                topic2 = "Discussion #" + (counter) + " " + posts.get(i).getDiscussionTopic().getTopic();
                                discussionMenu.add("Discussion #" + (counter));
                                stringBuilder.append(stringInLinesMethod(topic2));
                                stringBuilder.append("\n");
                                stringBuilder.append(posts.get(i).getDiscussionTopic().getTimeStamp());
                                stringBuilder.append("\n" + "\n");
                                ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                                topic1.add(topic2);
                                counter++;
                            }
                        }
                    }

                    if (noDisc) {
                        JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Learning Management System",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        String discussionSelected = (String) JOptionPane.showInputDialog(null, stringBuilder.toString(), "Learning Management System",
                                JOptionPane.PLAIN_MESSAGE, null, discussionMenu.toArray(), null);

                        while (discussionSelected == null) {
                            invalidInputMessage();
                            discussionSelected = (String) JOptionPane.showInputDialog(null, stringBuilder.toString(), "Learning Management System",
                                    JOptionPane.PLAIN_MESSAGE, null, discussionMenu.toArray(), null);
                        }

                        int discussionForumSelected1 = discussionMenu.indexOf(discussionSelected) + 1;

                        String identify3 = "#" + discussionForumSelected1 + " ";


                        if (topic1 != null) {
                            for (int i = 0; i < topic1.size(); i++) {
                                if (topic1.get(i).contains("Discussion #" + discussionForumSelected1 + " ")) {
                                    topic2 = topic1.get(i).substring(
                                            topic1.get(i).indexOf("#" + discussionForumSelected1 + " ")
                                                    + identify3.length());
                                }
                            }
                        }
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1.append("Course Name: " + courseName1);
                        stringBuilder1.append("\n" +"\n");

                        // Printing the Discussion Forum selected
                        for (int i = 0; i < posts.size(); i++) {
                            if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                    posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)) {
                                stringBuilder1.append(stringInLinesMethod(posts.get(i).getDiscussionTopic().getTopic()));
                                stringBuilder1.append("\n");
                                stringBuilder1.append(posts.get(i).getDiscussionTopic().getTimeStamp());
                                stringBuilder1.append("\n" + "\n");
                                break;
                            }
                        }

                        objectOutputStream.writeObject(topic2);
                        objectOutputStream.flush();

                        int responseNumber = 1;
                        String response = "";

                        ArrayList<String> responses = new ArrayList<>();
                        ArrayList<String> responseMenu = new ArrayList<>();
                        boolean noResponses = false;

                        for (int i = 0; i < posts.size(); i++) {

                            // Checking if there are no responses
                            // If there is an object that has the selected course and discussion forum
                            // and null Post object it means there are no posts for that forum
                            if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                    posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)
                                    && posts.get(i).getPost() == null) {
                                stringBuilder1.append("No Responses for this Discussion Forum!");
                                stringBuilder1.append("\n");
                                noResponses = true;
                            } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                    posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)) {
                                response = "Response #" + responseNumber + " " + posts.get(i).getPost();
                                responseMenu.add("Response #" + responseNumber);
                                responses.add(response);
                                stringBuilder1.append(stringInLinesMethod(response));
                                stringBuilder1.append("\n");
                                stringBuilder1.append(posts.get(i).getStudent().getUsername()
                                        + "  " + posts.get(i).getTimeStamp());
                                stringBuilder1.append("\n");
                                if (posts.get(i).getReply() != null) {
                                    stringBuilder1.append("Comments on the post:");
                                    stringBuilder1.append("\n");
                                    for (int j = 0; j < posts.get(i).getReply().size(); j++) {
                                        //System.out.println(postArrayList.get(i).getReply().get(j).getResponse());
                                        stringBuilder1.append(posts.get(i).getReply().get(j).getPerson().getUsername() + "  " +
                                                posts.get(i).getReply().get(j).getTimeStamp()
                                                + ": " + posts.get(i).getReply().get(j).getResponse());
                                        stringBuilder1.append("\n");

                                    }
                                }
                                responseNumber++;
                                stringBuilder1.append("\n");
                            }
                        }

                        objectOutputStream.writeObject(noResponses);
                        objectOutputStream.flush();

                        if(noResponses) {
                            JOptionPane.showMessageDialog(null, stringBuilder1.toString(), "Learning Management System",
                                    JOptionPane.PLAIN_MESSAGE);
                        } else  {
                            String responseStringSelected = (String) JOptionPane.showInputDialog(null, stringBuilder1.toString(), "Learning Management System",
                                    JOptionPane.PLAIN_MESSAGE, null, responseMenu.toArray(), null);

                            while (responseStringSelected == null) {
                                invalidInputMessage();
                                responseStringSelected = (String) JOptionPane.showInputDialog(null, stringBuilder1.toString(), "Learning Management System",
                                        JOptionPane.PLAIN_MESSAGE, null, responseMenu.toArray(), null);
                            }

                            int replyNumber = responseMenu.indexOf(responseStringSelected) + 1;

                            String identify1 = "#" + replyNumber + " ";
                            // To retrieve the post string corresponding to the number selected
                            if (responses != null) {
                                for (int i = 0; i < responses.size(); i++) {
                                    if (responses.get(i).contains("Response #" + replyNumber + " ")) {
                                        response = responses.get(i).substring(responses.get(i).indexOf("#" + replyNumber + " ")
                                                + identify1.length());
                                    }
                                }
                            }

                            String[] options12 = {"Comment", "Edit", "Delete", "Exit"};

                            int teacherReplyStudents = JOptionPane.showOptionDialog(null, "Please choose the action for the Student Post selected:",
                                    "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null, options12, null) + 1;
                            while (teacherReplyStudents == 0) {
                                invalidInputMessage();
                                teacherReplyStudents = JOptionPane.showOptionDialog(null, "Please choose the action for the Student Post selected:",
                                        "Learning Management System", JOptionPane.PLAIN_MESSAGE, 1, null, options12, null) + 1;
                            }
                            objectOutputStream.writeObject(teacherReplyStudents);
                            objectOutputStream.flush();

                            if (teacherReplyStudents == 1) {
                                objectOutputStream.writeObject(response);
                                objectOutputStream.flush();

                                commentOnStudentPostsGUI();

                            } else if (teacherReplyStudents == 2) {
                                objectOutputStream.writeObject(response);
                                objectOutputStream.flush();

                                editStudentPostGUI();
                            } else if (teacherReplyStudents == 3) {
                                objectOutputStream.writeObject(response);
                                objectOutputStream.flush();

                                JOptionPane.showMessageDialog(null,
                                        "The Student Post has been deleted!",
                                        "Learning Management System",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }

                        }
                    }
                } else if (noDiscussionForums) {
                    noDiscussionGUI();
                }
            }
        }
    }

    // Method to display teacher's initial menu
    public static int teacherMenu1 () throws  Exception {
        String[] options2 = {"Create a course", "Select a course", "Grade a Student", "Edit or delete account",
                "Exit"};
        int teacherUserSelection1 =  JOptionPane.showOptionDialog(null, "Please choose one of the following:", "Learning Management System",
                JOptionPane.PLAIN_MESSAGE, 1, null, options2, null) + 1;
        // System.out.println(teacherUserSelection1);
        while (teacherUserSelection1 == 0) {
            invalidInputMessage();
            teacherUserSelection1 =  JOptionPane.showOptionDialog(null, "Please choose one of the following:", "Learning Management System",
                    JOptionPane.PLAIN_MESSAGE, 1, null, options2, null) + 1;
        }
        objectOutputStream.writeObject(teacherUserSelection1);
        objectOutputStream.flush();

        return teacherUserSelection1;
    }

    //Method to display invalid input where required
    public static void invalidInputMessage() {
        JOptionPane.showMessageDialog(null, "Please enter the required input/choose the required option" + "\n" +
                "or use the EXIT button where available.", "Learning Management System", JOptionPane.WARNING_MESSAGE);
    }

}
