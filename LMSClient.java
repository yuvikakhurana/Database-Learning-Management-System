import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class LMSClient {

    Person person1;

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

    public static String inputHostName(){
        String name;
        name = JOptionPane.showInputDialog(null, "Enter Host name",
                "Learning Management System", JOptionPane.QUESTION_MESSAGE);
        return name;

    }

    //Method to import the content from a file provided by the User
    // Handles IOExceptions in the method this method is called
    public ArrayList<String> fileImporter(String fileName) throws IOException {

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
    public String titleGetter(ArrayList<String> fileData) {

        //gets title (first line) of an already imported file
        String title = fileData.get(0);

        return title;
    }

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



    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);

        String hostName;
        int portNumber = -1; //TODO: CHECK
        //PrintWriter writer;
        //Scanner in;
        Socket socket;
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


        //writer = new PrintWriter(socket.getOutputStream());
        //writer.flush();
        //in = new Scanner(socket.getInputStream());

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.flush();
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


        int userSelection = -1;
        boolean notAnInteger;

        System.out.println("Welcome to Discussion Forums");
        System.out.println("1. Create an account");
        System.out.println("2. Sign in");
        System.out.println("3. Exit");
        // Checking for valid input
        do {
            notAnInteger = false;
            try {
                userSelection = Integer.parseInt(scanner.nextLine());

                while (userSelection < 1 || userSelection > 3) {

                    System.out.println("Please enter a valid number!");
                    userSelection = Integer.parseInt(scanner.nextLine());

                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Invalid input! Please enter a valid number!");
                notAnInteger = true;
            }

        } while (notAnInteger);

        objectOutputStream.writeObject(userSelection);
        objectOutputStream.flush();

        do {

            if (userSelection == 1) {


                System.out.println("Please enter your name");
                String name = scanner.nextLine();

                while (name == null || name.equals("")) {
                    System.out.println("Invalid input! Please enter your name!");
                    name = scanner.nextLine();
                }

                objectOutputStream.writeObject(name);
                objectOutputStream.flush();

                System.out.println("Please enter a username");
                String username = scanner.nextLine();
                // Checking for valid input
                while (username == null || username.equals("")) {
                    System.out.println("Invalid input! Please enter a username!");
                    username = scanner.nextLine();
                }

                boolean usernameVerify = false;
                do {
                    usernameVerify = false;

                    objectOutputStream.writeObject(username);
                    objectOutputStream.flush();

                    usernameVerify = (Boolean) objectInputStream.readObject();

                    if (usernameVerify) {
                        System.out.println("Invalid. Username has already been taken. Please enter another username.");
                        username = scanner.nextLine();
                    }

                } while (usernameVerify);

                System.out.println("Please enter a password");
                String password = scanner.nextLine();

                while (password == null || password.equals("")) {
                    System.out.println("Invalid input! Please enter a username!");
                    password = scanner.nextLine();
                }

                //Sending password to the server
                objectOutputStream.writeObject(password);
                objectOutputStream.flush();

                System.out.println("Please enter your role");
                System.out.println("1.Teacher");
                System.out.println("2.Student");

                //CHECKING IF THE USER INPUTS A VALID INPUT (AN INTEGER AND A NUMBER EITHER 1 OR 2)
                boolean validNumber0 = false;
                int userRoleSelection = -1;

                do {
                    try {
                        userRoleSelection = Integer.parseInt(scanner.nextLine());
                        validNumber0 = false;

                        while (userRoleSelection < 1 || userRoleSelection > 2) {
                            System.out.println("Please enter a valid number!");
                            userRoleSelection = Integer.parseInt(scanner.nextLine());
                        }

                    } catch (NumberFormatException ime) {
                        System.out.println("Please enter a number");
                        validNumber0 = true;
                    }
                } while (validNumber0);

                objectOutputStream.writeObject(userRoleSelection);
                objectOutputStream.flush();

                if (userRoleSelection == 1) {
                    lmsClient = new LMSClient(new Teacher(name, username, password));
                } else if (userRoleSelection == 2) {
                    lmsClient = new LMSClient(new Student(name, username, password));
                }

            } else if (userSelection == 2) {
                String verifySignIn = "false";
                do {
                    System.out.println("Please enter your username");
                    String username = scanner.nextLine();


                    while (username == null || username.equals("")) {
                        System.out.println("Invalid input! Please enter a username!");
                        username = scanner.nextLine();
                    }

                    objectOutputStream.writeObject(username);
                    objectOutputStream.flush();

                    System.out.println("Please enter your password");
                    String password = scanner.nextLine();

                    while (password == null || password.equals("")) {
                        System.out.println("Invalid input! Please enter a username!");
                        password = scanner.nextLine();
                    }

                    objectOutputStream.writeObject(password);
                    objectOutputStream.flush();

                    //Verifying the username and password are valid

                    verifySignIn = (String) objectInputStream.readObject();

                    if (verifySignIn.equals("true")) {
                        System.out.println("You have been logged in!");
                        lmsClient = new LMSClient((Person) objectInputStream.readObject());
                    }

                    if (verifySignIn.equals("no accounts found")) {
                        System.out.println("No Account found with the given username. Please create an account");
                        break;
                    }

                    if (verifySignIn.equals("false"))
                        System.out.println("Invalid username or password. Please try again.");


                } while (verifySignIn.equals("false"));


            } else if (userSelection == 3) {
                System.out.println("Thank you. Bye!");
                return;
            }

            if (lmsClient != null && lmsClient.getPerson1() instanceof Teacher) {

                int teacherUserSelection1 = 0;
                boolean notAnInteger1;

                System.out.println("1. Create a course");
                System.out.println("2. Select a course");
                System.out.println("3. Grade a Student");
                System.out.println("4. Edit or delete account");
                System.out.println("5. Exit");
                // Checking for valid input
                do {
                    notAnInteger1 = false;
                    try {
                        teacherUserSelection1 = Integer.parseInt(scanner.nextLine());

                        while (teacherUserSelection1 > 5 || teacherUserSelection1 < 1) {
                            System.out.println("Please enter a valid number");
                            teacherUserSelection1 = Integer.parseInt(scanner.nextLine());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a valid number!");
                        notAnInteger1 = true;
                    }
                } while (notAnInteger1);

                objectOutputStream.writeObject(teacherUserSelection1);
                objectOutputStream.flush();

                boolean exit;

                //TODO: DO - WHILE LOOP + EXIT
                do {

                    exit = false;

                    if (teacherUserSelection1 == 1) {
                        System.out.println("Enter a course name");
                        String courseName = scanner.nextLine();

                        // Checking for valid input
                        while (courseName == null || courseName.equals("")) {
                            System.out.println("Invalid! Please enter a course name");
                            courseName = scanner.nextLine();
                        }

                        objectOutputStream.writeObject(courseName);
                        objectOutputStream.flush();

                        boolean sameCourse = (Boolean) objectInputStream.readObject();

                        if (sameCourse) {
                            System.out.println("Course already exists!");
                        } else {
                            System.out.println("Course has been created!");
                        }
                    } else if (teacherUserSelection1 == 2) {

                        String coursesStatus = (String) objectInputStream.readObject();

                        if (coursesStatus.equals("no courses")) {
                            System.out.println("No Courses to select!");
                        } else if (coursesStatus.equals("courses exist")) {

                            boolean noDiscussionForums = false;

                            int coursesSize = (Integer) objectInputStream.readObject();

                            for (int i = 0; i < coursesSize; i++) {
                                System.out.println((i + 1) + ". " + (String) objectInputStream.readObject());
                            }

                            int courseNumberSelected = 0;
                            boolean notAnInteger2;
                            // Checking for valid input
                            do {
                                notAnInteger2 = false;
                                try {
                                    courseNumberSelected = Integer.parseInt(scanner.nextLine());

                                    while (courseNumberSelected < 1 || courseNumberSelected > coursesSize) {
                                        System.out.println("Please enter a valid course number.");
                                        courseNumberSelected = Integer.parseInt(scanner.nextLine());
                                    }
                                } catch (NumberFormatException numberFormatException) {
                                    System.out.println("Invalid input! Please enter a valid course number.");
                                    notAnInteger2 = true;
                                }
                            } while (notAnInteger2);

                            objectOutputStream.writeObject(courseNumberSelected);
                            objectOutputStream.flush();

                            String courseName1 = (String) objectInputStream.readObject();

                            System.out.println("Course Name: " + courseName1);
                            System.out.println();

                            noDiscussionForums = (Boolean) objectInputStream.readObject();

                            if (noDiscussionForums) {
                                System.out.println("No Discussion Forums for this course.");
                                System.out.println();
                                //break;
                            } else {
                                ArrayList<Post> posts = new ArrayList<>();
                                int postsSize = (Integer) objectInputStream.readObject();
                                for (int i = 0; i < postsSize; i++) {
                                    posts.add(stringToPost((String) objectInputStream.readObject()));
                                }
                                int discussionNumber = 1;
                                ArrayList<String> ensureNoRepetition = new ArrayList<>();

                                for (int i = 0; i < posts.size(); i++) {

                                    if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                                        if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                                            System.out.println("Discussion #" + discussionNumber + " "
                                                    + posts.get(i).getDiscussionTopic().getTopic());
                                            System.out.println(posts.get(i).getDiscussionTopic().getTimeStamp());
                                            System.out.println();
                                            ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                                            discussionNumber++;
                                        }
                                    }
                                }

                            }

                            System.out.println("1. Create a Discussion Forum");
                            System.out.println("2. Delete a Discussion Forum");
                            System.out.println("3. Edit a Discussion Forum");
                            System.out.println("4. View/Reply/Edit/Delete Students responses");
                            System.out.println("5. Exit");


                            int teacherSelection2 = 0;
                            boolean notAnInteger3;
                            // Checking for valid input
                            do {
                                notAnInteger3 = false;
                                try {
                                    teacherSelection2 = Integer.parseInt(scanner.nextLine());

                                    while (teacherSelection2 < 1 || teacherSelection2 > 5) {
                                        System.out.println("Please enter a valid number!");
                                        teacherSelection2 = Integer.parseInt(scanner.nextLine());
                                    }
                                } catch (NumberFormatException numberFormatException) {
                                    System.out.println("Invalid input! Please enter a valid number!");
                                    notAnInteger3 = true;
                                }
                            } while (notAnInteger3);

                            objectOutputStream.writeObject(teacherSelection2);
                            objectOutputStream.flush();

                            //Create Discussion Forum
                            if (teacherSelection2 == 1) {

                                String header = "";
                                String content = "";
                                String topic = "";

                                boolean checkSameDiscussionForum;

                                do {

                                    checkSameDiscussionForum = false;

                                    //Allowing the user to choose the way they wish to input the file
                                    System.out.println("Please choose one of the following:");
                                    System.out.println("1. Manually create discussion forum");
                                    System.out.println("2. Import discussion forum from file");

                                    String inputString = scanner.nextLine();

                                    // Checking for valid input
                                    while (!(inputString.equals("1") || inputString.equals("2"))) {

                                        System.out.println("Invalid input! Please enter a valid number.");
                                        inputString = scanner.nextLine();

                                    }

                                    int input = Integer.parseInt(inputString);

                                    if (input == 2) {

                                        //Import data from a file
                                        ArrayList<String> data = new ArrayList<>();
                                        boolean fileCheck;
                                        do {
                                            fileCheck = true;

                                            try {
                                                System.out.println("Please enter the name of the file you would like to import");
                                                String fileName = scanner.nextLine();
                                                data = lmsClient.fileImporter(fileName);
                                                if (data.isEmpty()) {
                                                    System.out.println("The file is empty!");
                                                    fileCheck = false;
                                                }
                                            } catch (FileNotFoundException fileNotFoundException) {
                                                System.out.println("The filename entered doesn't exist!");
                                                fileCheck = false;
                                            } catch (IOException ioException) {
                                                System.out.println("There was an error in importing the file provided!");
                                                fileCheck = false;
                                            }
                                        } while (!fileCheck);

                                        //Once file has been successfully imported, organize it a little and make it useful
                                        //Todo : Referenced FileImporter and TitleGetter with the current object

                                        header = lmsClient.titleGetter(data);
                                        StringBuilder sb = new StringBuilder();

                                        //this removes the title from the array since it has already been dealt with
                                        data.remove(0);

                                        //this takes the ArrayList that was imported from the file and makes it into a string
                                        for (int length = data.size(); length > 0; length--) {
                                            sb.append("\n");
                                            sb.append(data.get(0));
                                            data.remove(0);
                                        }

                                        content = sb.toString();

                                        topic = header + content;       // Topic includes header and the prompt

                                    } else if (input == 1) {

                                        System.out.println("Enter the topic for Discussion Forum");
                                        header = scanner.nextLine();

                                        //Checking for valid input
                                        while (header == null || header.equals("")) {
                                            System.out.println("Invalid input! Please enter topic for discussion Forum");
                                            header = scanner.nextLine();
                                        }

                                        System.out.println("Enter the prompt");
                                        content = scanner.nextLine();

                                        // Checking for valid input
                                        while (content == null || content.equals("")) {
                                            System.out.println("Invalid input! Please enter prompt for discussion Forum");
                                            content = scanner.nextLine();
                                        }

                                        topic = header + "\n" + content; // Topic includes header and the prompt

                                    }

                                    objectOutputStream.writeObject(topic);
                                    objectOutputStream.flush();

                                    checkSameDiscussionForum = (Boolean) objectInputStream.readObject();

                                    if (checkSameDiscussionForum) {
                                        System.out.println("The Discussion Forum entered already exists! " +
                                                "Please enter different topic and prompt!");
                                    }

                                } while (checkSameDiscussionForum);

                                System.out.println("The Discussion Forum has been created!");

                            } else if (teacherSelection2 == 2) {

                                if (noDiscussionForums != true) {

                                    ArrayList<String> deleteDiscussion = new ArrayList<String>();
                                    // ArrayList to retrieve the DiscussionTopic corresponding to the number selected
                                    ArrayList<String> ensureNoRepetition = new ArrayList<>();
                                    // ArrayList to ensure a DiscussionTopic is not printed more than once
                                    String s = "";
                                    int counter = 1; // counter for serializing the Discussion Forums printed

                                    ArrayList<Post> posts = new ArrayList<>();
                                    int postsSize = (Integer) objectInputStream.readObject();
                                    for (int i = 0; i < postsSize; i++) {
                                        posts.add(stringToPost((String) objectInputStream.readObject()));
                                    }

                                    System.out.println("Course Name: " + courseName1);
                                    System.out.println();

                                    for (int i = 0; i < posts.size(); i++) {

                                        //If there is an object that has the selected course name and DiscussionTopic object set to null
                                        //It means there are no Discussion Forums for that course
                                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)
                                                && posts.get(i).getDiscussionTopic() == null) {
                                            System.out.println("No Discussion Forums for this course.");
                                            System.out.println();
                                            break;
                                        } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                                            // Printing the Discussion Forum of the course selected
                                            if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                                                //To ensure a Discussion Forum is not printed more than once
                                                s = "Discussion #" + (counter) + " " + posts.get(i).getDiscussionTopic().getTopic();
                                                System.out.println(s);
                                                System.out.println(posts.get(i).getDiscussionTopic().getTimeStamp());
                                                System.out.println();
                                                ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                                                deleteDiscussion.add(s);
                                                counter++;
                                            }
                                        }
                                    }

                                    System.out.println("Enter the number of Discussion Forum to be deleted");

                                    int discussionForumNumber = 0;
                                    boolean notAnInteger9;

                                    // Checking for valid input
                                    do {
                                        notAnInteger9 = false;
                                        try {
                                            discussionForumNumber = Integer.parseInt(scanner.nextLine());

                                            while (discussionForumNumber < 1 || discussionForumNumber > (counter - 1)) {
                                                System.out.println("Please enter a valid Discussion Forum number!");
                                                discussionForumNumber = Integer.parseInt(scanner.nextLine());
                                            }
                                        } catch (NumberFormatException numberFormatException) {
                                            System.out.println("Invalid input! Please enter a valid Discussion Forum number!");
                                            notAnInteger9 = true;
                                        }
                                    } while (notAnInteger9);

                                    // Checking for the number with # in the ArrayList to retrieve
                                    // the corresponding String of the Discussion Forum chosen
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

                                    objectOutputStream.writeObject(s);
                                    objectOutputStream.flush();

                                    System.out.println("The Discussion Forum has been deleted!");


                                } else if (noDiscussionForums)
                                    System.out.println("No Discussion Forums for the Course selected!");

                            } else if (teacherSelection2 == 3) {
                                if (noDiscussionForums != true) {


                                    ArrayList<String> editDiscussion = new ArrayList<>();
                                    // ArrayList to retrieve the DiscussionTopic corresponding to the number selected
                                    ArrayList<String> ensureNoRepetition = new ArrayList<>();
                                    // ArrayList to ensure a DiscussionTopic is not printed more than once
                                    String topic = "";
                                    int counter = 1;  // counter for serializing the Discussion Forums printed

                                    System.out.println("Course Name: " + courseName1);
                                    System.out.println();

                                    ArrayList<Post> posts = new ArrayList<>();
                                    int postsSize = (Integer) objectInputStream.readObject();
                                    for (int i = 0; i < postsSize; i++) {
                                        posts.add(stringToPost((String) objectInputStream.readObject()));
                                    }


                                    //If there is an object that has the selected course name and DiscussionTopic object set to null
                                    //It means there are no Discussion Forums for that course
                                    for (int i = 0; i < posts.size(); i++) {
                                        //int counter = 1;
                                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)
                                                && posts.get(i).getDiscussionTopic() == null) {
                                            System.out.println("No Discussion Forums for this course.");
                                            System.out.println();
                                            break;
                                        } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                                            if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                                                //To ensure a Discussion Forum is not printed more than once
                                                topic = "Discussion #" + (counter) + " " + posts.get(i).getDiscussionTopic().getTopic();
                                                System.out.println(topic);
                                                System.out.println(posts.get(i).getDiscussionTopic().getTimeStamp());
                                                System.out.println();
                                                ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                                                editDiscussion.add(topic);
                                                counter++;
                                            }
                                        }
                                    }

                                    System.out.println("Enter the number of Discussion Forum to be edited");

                                    int discussionForumSelected1 = 0;
                                    boolean notAnInteger10;
                                    // Checking for valid input
                                    do {
                                        notAnInteger10 = false;
                                        try {
                                            discussionForumSelected1 = Integer.parseInt(scanner.nextLine());

                                            while (discussionForumSelected1 < 1 || discussionForumSelected1 > (counter - 1)) {
                                                System.out.println("Please enter a valid Discussion Forum number!");
                                                discussionForumSelected1 = Integer.parseInt(scanner.nextLine());
                                            }
                                        } catch (NumberFormatException numberFormatException) {
                                            System.out.println("Invalid input! Please enter a valid Discussion Forum number!");
                                            notAnInteger10 = true;
                                        }
                                    } while (notAnInteger10);

                                    // Checking for the number with # in the ArrayList to retrieve
                                    // the corresponding String of the Discussion Forum chosen
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

                                    //TODO: NEED TO CHECK SAME DISCUSSION IN SERVER SIDE OR CLIENT SIDE WOULD WORK?

                                    do {
                                        checkSameDiscussionForum = false;

                                        System.out.println("Enter the edited Discussion Topic: ");
                                        String header = scanner.nextLine();

                                        while (header == null || header.equals("")) {
                                            System.out.println("Invalid input! Please enter topic for Discussion forum!");
                                            header = scanner.nextLine();
                                        }

                                        System.out.println("Enter the edited Discussion Forum prompt: ");
                                        String prompt = scanner.nextLine();

                                        while (prompt == null || prompt.equals("")) {
                                            System.out.println("Invalid input! Please enter topic for Discussion forum!");
                                            prompt = scanner.nextLine();
                                        }

                                        newTopic = header + '\n' + prompt;
                                        // Combining the header and prompt to form a Discussion Forum


                                        for (int i = 0; i < posts.size(); i++) {
                                            if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                                    posts.get(i).getDiscussionTopic() != null &&
                                                    posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(newTopic)) {
                                                checkSameDiscussionForum = true;
                                                System.out.println("The Discussion Forum entered already exists! " +
                                                        "Please enter a different topic and prompt!");
                                                break;
                                            }
                                        }
                                    } while (checkSameDiscussionForum);

                                    objectOutputStream.writeObject(newTopic);
                                    objectOutputStream.flush();

                                    System.out.println("The Discussion Forum has been edited!");

                                } else if (noDiscussionForums)
                                    System.out.println("No Discussion Forums for the Course selected!");

                            } else if (teacherSelection2 == 4) {

                                if (noDiscussionForums != true) {

                                    ArrayList<String> topic1 = new ArrayList<>();
                                    // ArrayList to retrieve the DiscussionTopic corresponding to the number selected
                                    ArrayList<String> ensureNoRepetition = new ArrayList<>();
                                    // ArrayList to ensure a DiscussionTopic is not printed more than once
                                    String topic2 = "";
                                    int counter = 1;

                                    System.out.println("Course Name: " + courseName1);
                                    System.out.println();

                                    ArrayList<Post> posts = new ArrayList<>();
                                    int postsSize = (Integer) objectInputStream.readObject();
                                    for (int i = 0; i < postsSize; i++) {
                                        posts.add(stringToPost((String) objectInputStream.readObject()));
                                    }

                                    //If there is an object that has the selected course name and DiscussionTopic object set to null
                                    //It means there are no Discussion Forums for that course
                                    for (int i = 0; i < posts.size(); i++) {
                                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)
                                                && posts.get(i).getDiscussionTopic() == null) {
                                            System.out.println("No Discussion Forums for this course.");
                                            System.out.println();
                                            break;
                                        } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                                            if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                                                //To ensure a Discussion Forum is not printed more than once
                                                topic2 = "Discussion #" + counter + " " + posts.get(i).getDiscussionTopic().getTopic();
                                                System.out.println(topic2);
                                                System.out.println(posts.get(i).getDiscussionTopic().getTimeStamp());
                                                System.out.println();
                                                topic1.add(topic2);
                                                ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                                                counter++;
                                            }
                                        }
                                    }


                                    System.out.println("Enter the number of Discussion Forum whose " +
                                            "student responses needs to be viewed and replied.");

                                    int discussionNumberSelected2 = 0;
                                    boolean notAnInteger11;
                                    // Checking for valid input
                                    do {
                                        notAnInteger11 = false;
                                        try {
                                            discussionNumberSelected2 = Integer.parseInt(scanner.nextLine());

                                            while (discussionNumberSelected2 < 1 || discussionNumberSelected2 > (counter - 1)) {
                                                System.out.println("Please enter a valid Discussion Forum number!");
                                                discussionNumberSelected2 = Integer.parseInt(scanner.nextLine());
                                            }
                                        } catch (NumberFormatException numberFormatException) {
                                            System.out.println("Invalid input! Please enter a valid Discussion Forum number!");
                                            notAnInteger11 = true;
                                        }
                                    } while (notAnInteger11);

                                    // Checking for the number with # in the ArrayList to
                                    // retrieve the corresponding String of the Discussion Forum chosen
                                    String identify = "#" + discussionNumberSelected2 + " ";

                                    if (topic1 != null) {
                                        for (int i = 0; i < topic1.size(); i++) {
                                            if (topic1.get(i).contains("Discussion #" + discussionNumberSelected2 + " ")) {
                                                topic2 = topic1.get(i).substring(topic1.get(i).indexOf("#" + discussionNumberSelected2 + " ")
                                                        + identify.length());
                                            }
                                        }
                                    }

                                    System.out.println("Course Name: " + courseName1);
                                    System.out.println();

                                    // Printing the Discussion Forum selected
                                    for (int i = 0; i < posts.size(); i++) {
                                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                                posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)) {
                                            System.out.println(posts.get(i).getDiscussionTopic().getTopic());
                                            System.out.println(posts.get(i).getDiscussionTopic().getTimeStamp());
                                            System.out.println();
                                            break;
                                        }
                                    }

                                    objectOutputStream.writeObject(topic2);
                                    objectOutputStream.flush();

                                    int responseNumber = 1;
                                    String response = "";

                                    ArrayList<String> responses = new ArrayList<>();
                                    boolean noResponses = false;

                                    for (int i = 0; i < posts.size(); i++) {

                                        // Checking if there are no responses
                                        // If there is an object that has the selected course and discussion forum
                                        // and null Post object it means there are no posts for that forum
                                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                                posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)
                                                && posts.get(i).getPost() == null) {
                                            System.out.println("No Responses for this Discussion Forum!");
                                            System.out.println();
                                            noResponses = true;
                                        } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                                posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)) {
                                            response = "Response #" + responseNumber++ + " " + posts.get(i).getPost();
                                            responses.add(response);
                                            System.out.println(response);
                                            System.out.println(posts.get(i).getStudent().getUsername()
                                                    + "  " + posts.get(i).getTimeStamp());
                                            System.out.println();
                                            if (posts.get(i).getReply() != null) {
                                                System.out.println("Comments on the post:");
                                                System.out.println();
                                                for (int j = 0; j < posts.get(i).getReply().size(); j++) {
                                                    //System.out.println(postArrayList.get(i).getReply().get(j).getResponse());
                                                    System.out.println(posts.get(i).getReply().get(j).getPerson().getUsername() + "  " +
                                                            posts.get(i).getReply().get(j).getTimeStamp()
                                                            + ": " + posts.get(i).getReply().get(j).getResponse());
                                                    System.out.println();

                                                }
                                            }
                                        }
                                    }

                                    objectOutputStream.writeObject(noResponses);
                                    objectOutputStream.flush();

                                    if (noResponses == false) {

                                        System.out.println("1. Comment on a Student Response");
                                        System.out.println("2. Edit a Student Response");
                                        System.out.println("3. Delete a Student Response");
                                        System.out.println("4. Exit");

                                        int teacherReplyStudents = 0;
                                        boolean notAnInteger12 = false;
                                        // Checking for valid input
                                        do {
                                            notAnInteger12 = false;
                                            try {

                                                teacherReplyStudents = Integer.parseInt(scanner.nextLine());

                                                while (teacherReplyStudents < 1 || teacherReplyStudents > 4) {
                                                    System.out.println("Please enter a valid number!");
                                                    teacherReplyStudents = Integer.parseInt(scanner.nextLine());
                                                }
                                            } catch (NumberFormatException numberFormatException) {
                                                System.out.println("Invalid input! Please enter a valid number!");
                                                notAnInteger12 = true;
                                            }
                                        } while (notAnInteger12);

                                        objectOutputStream.writeObject(teacherReplyStudents);
                                        objectOutputStream.flush();

                                        if (teacherReplyStudents == 1) {

                                            System.out.println("Enter the Response number");

                                            int replyNumber = 0;
                                            boolean notAnInteger13;
                                            // Checking for valid input
                                            do {
                                                notAnInteger13 = false;
                                                try {
                                                    replyNumber = Integer.parseInt(scanner.nextLine());

                                                    while (replyNumber < 1 || replyNumber > (responseNumber - 1)) {
                                                        System.out.println("Please enter a valid response number!");
                                                        replyNumber = Integer.parseInt(scanner.nextLine());

                                                    }
                                                } catch (NumberFormatException numberFormatException) {
                                                    System.out.println("Invalid input! Please enter a valid response number!");
                                                    notAnInteger13 = true;
                                                }
                                            } while (notAnInteger13);

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

                                            objectOutputStream.writeObject(response);
                                            objectOutputStream.flush();

                                            System.out.println("Enter the reply to be added:");
                                            String reply = scanner.nextLine();

                                            // Checking for valid input
                                            while (reply == null || reply.equals("")) {
                                                System.out.println("Invalid input! Please enter the reply for the post!");
                                                reply = scanner.nextLine();
                                            }

                                            objectOutputStream.writeObject(reply);
                                            objectOutputStream.flush();

                                            System.out.println("Your reply has been added!");


                                        } else if (teacherReplyStudents == 2) {

                                            System.out.println("Enter the Response number");

                                            int replyNumber = 0;
                                            boolean notAnInteger13;
                                            // Checking for valid input
                                            do {
                                                notAnInteger13 = false;
                                                try {
                                                    replyNumber = Integer.parseInt(scanner.nextLine());

                                                    while (replyNumber < 1 || replyNumber > (responseNumber - 1)) {
                                                        System.out.println("Please enter a valid response number!");
                                                        replyNumber = Integer.parseInt(scanner.nextLine());

                                                    }
                                                } catch (NumberFormatException numberFormatException) {
                                                    System.out.println("Invalid input! Please enter a valid response number !");
                                                    notAnInteger13 = true;
                                                }
                                            } while (notAnInteger13);

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

                                            objectOutputStream.writeObject(response);
                                            objectOutputStream.flush();

                                            boolean checkSamePost = false;
                                            String editedResponse;
                                            // checking if the edited post already exits
                                            // will loop until a valid input is provided
                                            do {
                                                //checkSamePost = false;

                                                System.out.println("Enter the edited Response Post:");
                                                editedResponse = scanner.nextLine();

                                                while (editedResponse == null || editedResponse.equals("")) {
                                                    System.out.println("Invalid input! Please enter the reply for the post!");
                                                    editedResponse = scanner.nextLine();
                                                }

                                                objectOutputStream.writeObject(editedResponse);
                                                objectOutputStream.flush();

                                                checkSamePost = (Boolean) objectInputStream.readObject();

                                                if (checkSamePost) {
                                                    System.out.println("The post already exists! ");
                                                }

                                            } while (checkSamePost);

                                            System.out.println("The Student Post has been edited!");

                                        } else if (teacherReplyStudents == 3) {

                                            System.out.println("Enter the Response number");

                                            int replyNumber = 0;
                                            boolean notAnInteger15;
                                            // Checking for valid input
                                            do {
                                                notAnInteger15 = false;
                                                try {
                                                    replyNumber = Integer.parseInt(scanner.nextLine());

                                                    while (replyNumber < 1 || replyNumber > (responseNumber - 1)) {
                                                        System.out.println("Please enter a valid response number!");
                                                        replyNumber = Integer.parseInt(scanner.nextLine());

                                                    }
                                                } catch (NumberFormatException numberFormatException) {
                                                    System.out.println("Invalid input! Please enter a valid response number !");
                                                    notAnInteger15 = true;
                                                }
                                            } while (notAnInteger15);

                                            String identify1 = "#" + replyNumber + " ";
                                            // retrieving the post string corresponding to the chosen number
                                            if (responses != null) {
                                                for (int i = 0; i < responses.size(); i++) {
                                                    if (responses.get(i).contains("Response #" + replyNumber + " ")) {
                                                        response = responses.get(i).substring(responses.get(i).indexOf("#" + replyNumber + " ")
                                                                + identify1.length());
                                                    }
                                                }
                                            }

                                            objectOutputStream.writeObject(response);
                                            objectOutputStream.flush();

                                            System.out.println("The Post has been deleted!");

                                        }

                                    }

                                } else if (noDiscussionForums)
                                    System.out.println("No Discussion Forums for the Course selected!");

                            }
                        }

                    } else if (teacherUserSelection1 == 3) {

                        ArrayList<String> studentUsernameArrayList = new ArrayList<>();
                        int studentUsernameArrayListSize = (Integer) objectInputStream.readObject();
                        for (int j = 0; j < studentUsernameArrayListSize; j++) {
                            studentUsernameArrayList.add((String) objectInputStream.readObject());
                        }

                        if (studentUsernameArrayList.size() == 0) {
                            System.out.println("There are no student users to grade!");
                        } else {
                            //Printing the Student users' usernames
                            for (int i = 0; i < studentUsernameArrayList.size(); i++) {
                                System.out.println((i + 1) + ". " + studentUsernameArrayList.get(i));
                            }

                            int studentSelected = -1; //the number of the student username selected, initialised to -1
                            boolean notAnInteger16;     // boolean to check if a valid integer is inputted by the user

                            // will loop until the user enters a valid input
                            do {
                                notAnInteger16 = false;
                                try {
                                    studentSelected = Integer.parseInt(scanner.nextLine());

                                    while (studentSelected < 1 || studentSelected > studentUsernameArrayList.size()) {
                                        System.out.println("Please enter a valid number.");
                                        studentSelected = Integer.parseInt(scanner.nextLine());
                                    }
                                } catch (NumberFormatException numberFormatException) {
                                    System.out.println("Invalid input! Please enter a valid number.");
                                    notAnInteger16 = true;
                                }
                            } while (notAnInteger16);

                            //Obtaining the username of the Student user selected
                            String studentUsernameSelected = studentUsernameArrayList.get(studentSelected - 1);
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
                                System.out.println("The Student User selected has not made any posts!");
                            } else {

                                // Printing the Posts that are made by the selected Student user
                                for (int i = 0; i < studentPosts.size(); i++) {
                                    System.out.println("#" + postNumber++);
                                    System.out.println("Course Name: " + studentPosts.get(i).getCourseName());
                                    System.out.println();
                                    System.out.println("Discussion Forum: " + studentPosts.get(i).getDiscussionTopic().getTopic());
                                    System.out.println(studentPosts.get(i).getDiscussionTopic().getTimeStamp());
                                    System.out.println();
                                    System.out.println("Response: " + studentPosts.get(i).getPost());
                                    System.out.println(studentPosts.get(i).getTimeStamp());
                                    System.out.println();
                                }
                                int postNumber1 = 1;

                                for (int i = 0; i < studentPosts.size(); i++) {

                                    System.out.println("Please enter the grade for the Post #" + postNumber1++);

                                    double grade1 = 0;      // The grade value will be between 0.0 and 10.0
                                    boolean notAnInteger17; // To ensure valid input is put by the user


                                    do {
                                        notAnInteger17 = false;
                                        try {
                                            grade1 = Double.parseDouble(scanner.nextLine());

                                            while (grade1 < 0.0 || grade1 > 10.0) {
                                                System.out.println("Please enter a grade value between 0.0 to 10.0");
                                                grade1 = Double.parseDouble(scanner.nextLine());
                                            }

                                        } catch (NumberFormatException numberFormatException) {
                                            System.out.println("Invalid input! Please enter a grade value between 0.0 to 10.0");
                                            notAnInteger17 = true;
                                        }
                                    } while (notAnInteger17);

                                    objectOutputStream.writeObject(grade1);
                                    objectOutputStream.flush();


                                }
                                System.out.println("The Student User selected has been graded!");
                            }


                        }

                    } else if (teacherUserSelection1 == 4) {

                        System.out.println("1. Edit Account");
                        System.out.println("2. Delete Account");


                        int editOrDelete = 0;
                        boolean notAnInteger4;
                        // checking for valid input
                        do {

                            notAnInteger4 = false;

                            try {
                                editOrDelete = Integer.parseInt(scanner.nextLine());

                                while (editOrDelete < 1 || editOrDelete > 2) {
                                    System.out.println("Please enter a valid number!");
                                    editOrDelete = Integer.parseInt(scanner.nextLine());
                                }
                            } catch (NumberFormatException numberFormatException) {
                                System.out.println("Invalid input! Please enter a valid number!");
                                notAnInteger4 = true;
                            }
                        } while (notAnInteger4);

                        objectOutputStream.writeObject(editOrDelete);
                        objectOutputStream.flush();

                        if (editOrDelete == 1) {
                            System.out.println("Please select the part you want to modify: " +
                                    "\n1. User name" +
                                    "\n2. Password");

                            boolean notAnInteger18 = false;
                            int modifyAccount = 0;
                            //Checking if the input provided is valid and meets the requirements
                            do {
                                notAnInteger18 = false;
                                try {
                                    modifyAccount = Integer.parseInt(scanner.nextLine());

                                    while (modifyAccount < 1 || modifyAccount > 2) {
                                        System.out.println("Please enter a valid number!");
                                        modifyAccount = Integer.parseInt(scanner.nextLine());
                                    }
                                } catch (NumberFormatException numberFormatException) {
                                    System.out.println("Invalid input! Please enter a valid number!");
                                    notAnInteger18 = true;
                                }
                            } while (notAnInteger18);

                            objectOutputStream.writeObject(modifyAccount);
                            objectOutputStream.flush();

                            //String oldUserName = lmsClient.person1.getUsername();
                            objectOutputStream.writeObject(lmsClient.person1);
                            objectOutputStream.flush();

                            if (modifyAccount == 1) {

                                boolean usernameVerify = false;
                                do {

                                    System.out.println("Please enter the new username:");
                                    String newUserName = scanner.nextLine();

                                    //Checking for null or empty string input
                                    while (newUserName == null || newUserName.equals("")) {
                                        System.out.println("Invalid input! Please enter a username!");
                                        newUserName = scanner.nextLine();
                                    }

                                    objectOutputStream.writeObject(newUserName);
                                    objectOutputStream.flush();

                                    usernameVerify = (Boolean) objectInputStream.readObject();

                                    if (usernameVerify) {
                                        System.out.println("Invalid. Username has already been taken. " +
                                                "Please enter another username.");
                                    }

                                } while (usernameVerify);

                            } else if (modifyAccount == 2) {

                                System.out.println("Please enter the new password:");
                                String newPassword = scanner.nextLine();
                                //person1.setPassword(newPassword);

                                while (newPassword == null || newPassword.equals("")) {
                                    System.out.println("Invalid input! Please enter a password!");
                                    newPassword = scanner.nextLine();
                                }

                                objectOutputStream.writeObject(newPassword);
                                objectOutputStream.flush();

                            }

                            System.out.println("Your account has been edited!");
                            System.out.println("Please login with your new credentials.");

                        } else if (editOrDelete == 2) {

                            objectOutputStream.writeObject(lmsClient.person1);
                            objectOutputStream.flush();

                            System.out.println("Your account has been deleted!");

                        }
                        exit = true;
                    } else if (teacherUserSelection1 == 5) {
                        exit = true;
                    }

                    if (!exit) {
                        System.out.println("1. Create a course");
                        System.out.println("2. Select a course");
                        System.out.println("3. Grade a Student");
                        System.out.println("4. Edit or delete account");
                        System.out.println("5. Exit");

                        do {
                            notAnInteger1 = false;
                            try {
                                teacherUserSelection1 = Integer.parseInt(scanner.nextLine());

                                while (teacherUserSelection1 > 5 || teacherUserSelection1 < 1) {
                                    System.out.println("Please enter a valid number");
                                    teacherUserSelection1 = Integer.parseInt(scanner.nextLine());
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input! Please enter a valid number!");
                                notAnInteger1 = true;
                            }
                        } while (notAnInteger1);

                        objectOutputStream.writeObject(teacherUserSelection1);
                        objectOutputStream.flush();

                    }

                } while (teacherUserSelection1 > 0 && teacherUserSelection1 < 5 && !exit);

            } else if (lmsClient != null && lmsClient.getPerson1() instanceof Student) {

                boolean exit;

                System.out.println("1. Select a course");
                System.out.println("2. View my Grades");
                System.out.println("3. Edit or delete account");
                System.out.println("4. Exit");


                String studentSelection1String = scanner.nextLine();
                // Checking for valid input
                while (!(studentSelection1String.equals("1") || studentSelection1String.equals("2") ||
                        studentSelection1String.equals("3") || studentSelection1String.equals("4"))) {
                    System.out.println("Invalid input! Please enter a valid number!");
                    studentSelection1String = scanner.nextLine();
                }

                int studentSelection1 = Integer.parseInt(studentSelection1String);

                objectOutputStream.writeObject(studentSelection1);
                objectOutputStream.flush();

                do {

                    exit = false;

                    if (studentSelection1 == 1) {
                        String coursesStatus = (String) objectInputStream.readObject();

                        if (coursesStatus.equals("no courses")) {
                            System.out.println("No Courses to select!");
                        } else if (coursesStatus.equals("courses exist")) {

                            boolean noDiscussionForums = false;

                            int coursesSize = (Integer) objectInputStream.readObject();

                            for (int i = 0; i < coursesSize; i++) {
                                System.out.println((i + 1) + ". " + (String) objectInputStream.readObject());
                            }

                            int courseNumberSelected = 0;
                            boolean notAnInteger2;
                            // Checking for valid input
                            do {
                                notAnInteger2 = false;
                                try {
                                    courseNumberSelected = Integer.parseInt(scanner.nextLine());

                                    while (courseNumberSelected < 1 || courseNumberSelected > coursesSize) {
                                        System.out.println("Please enter a valid course number.");
                                        courseNumberSelected = Integer.parseInt(scanner.nextLine());
                                    }
                                } catch (NumberFormatException numberFormatException) {
                                    System.out.println("Invalid input! Please enter a valid course number.");
                                    notAnInteger2 = true;
                                }
                            } while (notAnInteger2);

                            objectOutputStream.writeObject(courseNumberSelected);
                            objectOutputStream.flush();

                            String courseName1 = (String) objectInputStream.readObject();

                            System.out.println("Course Name: " + courseName1);
                            System.out.println();

                            noDiscussionForums = (Boolean) objectInputStream.readObject();

                            if (noDiscussionForums) {
                                System.out.println("No Discussion Forums for this course.");
                                System.out.println();
                                //break;
                            } else {
                                ArrayList<Post> posts = new ArrayList<>();
                                int postsSize = (Integer) objectInputStream.readObject();
                                for (int i = 0; i < postsSize; i++) {
                                    posts.add(stringToPost((String) objectInputStream.readObject()));
                                }
                                int discussionNumber = 1;
                                ArrayList<String> ensureNoRepetition = new ArrayList<>();

                                for (int i = 0; i < posts.size(); i++) {

                                    if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                                        if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                                            System.out.println("Discussion #" + discussionNumber + " "
                                                    + posts.get(i).getDiscussionTopic().getTopic());
                                            System.out.println(posts.get(i).getDiscussionTopic().getTimeStamp());
                                            System.out.println();
                                            ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                                            discussionNumber++;
                                        }
                                    }
                                }
                            }

                            if (noDiscussionForums != true) {

                                System.out.println("1. Select a Discussion Forum");
                                System.out.println("2. Exit");

                                int studentSelection2 = 0;
                                boolean notAnInteger5;
                                // Checking for valid input
                                do {
                                    notAnInteger5 = false;
                                    try {
                                        studentSelection2 = Integer.parseInt(scanner.nextLine());

                                        while (studentSelection2 < 1 || studentSelection2 > 2) {
                                            System.out.println("Please enter a valid number!");
                                            studentSelection2 = Integer.parseInt(scanner.nextLine());
                                        }
                                    } catch (NumberFormatException numberFormatException) {
                                        System.out.println("Invalid input! Please enter a valid number! ");
                                        notAnInteger5 = true;
                                    }
                                } while (notAnInteger5);

                                objectOutputStream.writeObject(studentSelection2);
                                objectOutputStream.flush();

                                if (studentSelection2 == 1) {

                                    ArrayList<String> topic1 = new ArrayList<>();
                                    // ArrayList to retrieve the DiscussionTopic corresponding to the number selected
                                    ArrayList<String> ensureNoRepetition = new ArrayList<>();
                                    // ArrayList to ensure a DiscussionTopic is not printed more than once
                                    String topic2 = "";
                                    int counter = 1;

                                    System.out.println("Course Name: " + courseName1);
                                    System.out.println();

                                    ArrayList<Post> posts = new ArrayList<>();
                                    int postsSize = (Integer) objectInputStream.readObject();
                                    for (int i = 0; i < postsSize; i++) {
                                        posts.add(stringToPost((String) objectInputStream.readObject()));
                                    }

                                    //If there is an object that has the selected course name and DiscussionTopic object set to null
                                    //It means there are no Discussion Forums for that course
                                    for (int i = 0; i < posts.size(); i++) {
                                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)
                                                && posts.get(i).getDiscussionTopic() == null) {
                                            System.out.println("No Discussion Forums for this course.");
                                            System.out.println();
                                            break;
                                        } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1)) {
                                            if (!ensureNoRepetition.contains(posts.get(i).getDiscussionTopic().getTopic())) {
                                                //To ensure a Discussion Forum is not printed more than once
                                                topic2 = "Discussion #" + counter + " " + posts.get(i).getDiscussionTopic().getTopic();
                                                System.out.println(topic2);
                                                System.out.println(posts.get(i).getDiscussionTopic().getTimeStamp());
                                                System.out.println();
                                                topic1.add(topic2);
                                                ensureNoRepetition.add(posts.get(i).getDiscussionTopic().getTopic());
                                                counter++;
                                            }
                                        }
                                    }

                                    System.out.println("Enter the number of Discussion Forum");

                                    int discussionForumSelected2 = 0;
                                    boolean notAnInteger6;
                                    // Checking for valid input
                                    do {
                                        notAnInteger6 = false;
                                        try {
                                            discussionForumSelected2 = Integer.parseInt(scanner.nextLine());

                                            while (discussionForumSelected2 < 1 ||
                                                    discussionForumSelected2 > (counter - 1)) {
                                                System.out.println("Please enter a valid Discussion Forum number!");
                                                discussionForumSelected2 = Integer.parseInt(scanner.nextLine());
                                            }
                                        } catch (NumberFormatException numberFormatException) {
                                            System.out.println("Invalid input! " +
                                                    "Please enter a valid Discussion Forum number!");
                                            notAnInteger6 = true;
                                        }
                                    } while (notAnInteger6);

                                    String identify3 = "#" + discussionForumSelected2 + " ";
                                    // retrieving the discussion topic string
                                    if (topic1 != null) {
                                        for (int i = 0; i < topic1.size(); i++) {
                                            if (topic1.get(i).contains("Discussion #" +
                                                    discussionForumSelected2 + " ")) {
                                                topic2 = topic1.get(i).substring(
                                                        topic1.get(i).indexOf("#" + discussionForumSelected2 + " ")
                                                                + identify3.length());
                                            }
                                        }
                                    }

                                    System.out.println("Course Name: " + courseName1);
                                    System.out.println();

                                    // Printing the Discussion Forum selected
                                    for (int i = 0; i < posts.size(); i++) {
                                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                                posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)) {
                                            System.out.println(posts.get(i).getDiscussionTopic().getTopic());
                                            System.out.println(posts.get(i).getDiscussionTopic().getTimeStamp());
                                            System.out.println();
                                            break;
                                        }
                                    }

                                    objectOutputStream.writeObject(topic2);
                                    objectOutputStream.flush();

                                    int responseNumber = 1;
                                    String response = "";

                                    ArrayList<String> responses = new ArrayList<>();
                                    boolean noResponses = false;

                                    for (int i = 0; i < posts.size(); i++) {

                                        // Checking if there are no responses
                                        // If there is an object that has the selected course and discussion forum
                                        // and null Post object it means there are no posts for that forum
                                        if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                                posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)
                                                && posts.get(i).getPost() == null) {
                                            System.out.println("No Responses for this Discussion Forum!");
                                            System.out.println();
                                            noResponses = true;
                                        } else if (posts.get(i).getCourseName().equalsIgnoreCase(courseName1) &&
                                                posts.get(i).getDiscussionTopic().getTopic().equalsIgnoreCase(topic2)) {
                                            response = "Response #" + responseNumber++ + " " + posts.get(i).getPost();
                                            responses.add(response);
                                            System.out.println(response);
                                            System.out.println(posts.get(i).getStudent().getUsername()
                                                    + "  " + posts.get(i).getTimeStamp());
                                            System.out.println();
                                            if (posts.get(i).getReply() != null) {
                                                System.out.println("Comments on the post:");
                                                System.out.println();
                                                for (int j = 0; j < posts.get(i).getReply().size(); j++) {
                                                    //System.out.println(postArrayList.get(i).getReply().get(j).getResponse());
                                                    System.out.println(posts.get(i).getReply().get(j).getPerson().getUsername() + "  " +
                                                            posts.get(i).getReply().get(j).getTimeStamp()
                                                            + ": " + posts.get(i).getReply().get(j).getResponse());
                                                    System.out.println();

                                                }
                                            }
                                        }
                                    }

                                    objectOutputStream.writeObject(noResponses);
                                    objectOutputStream.flush();

                                    System.out.println("1. Create a Post");
                                    System.out.println("2. Reply to a Post");
                                    System.out.println("3. Exit");


                                    int studentSelection3 = 0;
                                    boolean notAnInteger7;
                                    // Checking for valid input
                                    do {
                                        notAnInteger7 = false;
                                        try {
                                            studentSelection3 = Integer.parseInt(scanner.nextLine());

                                            while (studentSelection3 < 1 || studentSelection3 > 3) {
                                                System.out.println("Please enter a valid number!");
                                                studentSelection3 = Integer.parseInt(scanner.nextLine());
                                            }
                                        } catch (NumberFormatException numberFormatException) {
                                            System.out.println("Invalid input! Please enter a valid number!");
                                            notAnInteger7 = true;
                                        }
                                    } while (notAnInteger7);

                                    objectOutputStream.writeObject(studentSelection3);
                                    objectOutputStream.flush();

                                    if (studentSelection3 == 1) {

                                        String studentPost = "";
                                        boolean samePost = false;
                                        int choice = 0;

                                        // Checking if the post provided already exists
                                        do {

                                            samePost = false;

                                            System.out.println("1. Manually input a reply");
                                            System.out.println("2. Import a reply from a file");

                                            boolean notAnInteger8;
                                            // Checking for valid input
                                            do {
                                                notAnInteger8 = false;
                                                try {
                                                    choice = Integer.parseInt(scanner.nextLine());

                                                    while (choice < 1 || choice > 2) {
                                                        System.out.println("Please enter a valid number.");
                                                        choice = Integer.parseInt(scanner.nextLine());
                                                    }
                                                } catch (NumberFormatException numberFormatException) {
                                                    System.out.println("Invalid input! Please enter a valid number.");
                                                    notAnInteger8 = true;
                                                }
                                            } while (notAnInteger8);

                                            if (choice == 2) {

                                                //Import data from a file
                                                ArrayList<String> data = new ArrayList<>();
                                                boolean fileCheck;
                                                do {
                                                    fileCheck = true;
                                                    // ensuring the data from the file is imported
                                                    // will loop until the data from file is imported properly
                                                    try {
                                                        System.out.println("Please enter the name of the " +
                                                                "file you would like to import");
                                                        String fileName = scanner.nextLine();
                                                        data = lmsClient.fileImporter(fileName);
                                                        if (data.isEmpty()) {
                                                            System.out.println("The file is empty!");
                                                            fileCheck = false;
                                                        }
                                                    } catch (FileNotFoundException fileNotFoundException) {
                                                        System.out.println("The filename entered doesn't exist!");
                                                        fileCheck = false;
                                                    } catch (IOException ioException) {
                                                        System.out.println("There was an error in importing " +
                                                                "the file provided!");
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

                                            } else if (choice == 1) {
                                                // If they choose to manually input the response/post
                                                System.out.println("Enter the content for the post below:");
                                                studentPost = scanner.nextLine();

                                            }
                                            //Checking for valid input
                                            while (studentPost == null || studentPost.equals("")) {
                                                System.out.println("Invalid input! " +
                                                        "Please enter the content for the post:");
                                                studentPost = scanner.nextLine();
                                            }

                                            //Checking if the post already exists

                                            objectOutputStream.writeObject(studentPost);
                                            objectOutputStream.flush();

                                            samePost = (boolean) objectInputStream.readObject();

                                            if (samePost) {
                                                System.out.println("The post already exists!");
                                            }

                                            // Will loop until a valid response is provided
                                        } while (samePost);

                                        System.out.println("Your post has been added!");

                                    } else if (studentSelection3 == 2) {

                                        if (noResponses) {
                                            System.out.println("No Responses for this discussion forum!");
                                        } else {

                                            System.out.println("Enter the Response number");

                                            int replyNumber = 0;
                                            boolean notAnInteger13;
                                            // Checking for valid input
                                            do {
                                                notAnInteger13 = false;
                                                try {
                                                    replyNumber = Integer.parseInt(scanner.nextLine());

                                                    while (replyNumber < 1 || replyNumber > (responseNumber - 1)) {
                                                        System.out.println("Please enter a valid response number!");
                                                        replyNumber = Integer.parseInt(scanner.nextLine());

                                                    }
                                                } catch (NumberFormatException numberFormatException) {
                                                    System.out.println("Invalid input! Please enter a valid response number!");
                                                    notAnInteger13 = true;
                                                }
                                            } while (notAnInteger13);

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

                                            objectOutputStream.writeObject(response);
                                            objectOutputStream.flush();

                                            System.out.println("Enter the reply to be added:");
                                            String reply = scanner.nextLine();

                                            // Checking for valid input
                                            while (reply == null || reply.equals("")) {
                                                System.out.println("Invalid input! Please enter the reply for the post!");
                                                reply = scanner.nextLine();
                                            }

                                            objectOutputStream.writeObject(reply);
                                            objectOutputStream.flush();

                                            System.out.println("Your reply has been added!");
                                        }

                                    } else if (studentSelection3 == 3) {
                                        //Exit the current menu
                                    }
                                } else if (studentSelection2 == 2) {
                                    //Exit the current menu
                                }
                            }
                        }
                    } else if (studentSelection1 == 2) {

                        ArrayList<Post> posts = new ArrayList<>();
                        int postsSize = (Integer) objectInputStream.readObject();
                        for (int i = 0; i < postsSize; i++) {
                            posts.add(stringToPost((String) objectInputStream.readObject()));
                        }

                        int postNumber = 1;
                        int counter = 0;
                        // printing the posts made by student and the grade received as per the requirement
                        for (int i = 0; i < posts.size(); i++) {
                            if (posts.get(i).getStudent() != null
                                    && posts.get(i).getStudent().getUsername().equals(
                                    lmsClient.getPerson1().getUsername())) {
                                System.out.println("#" + postNumber++);
                                System.out.println("Course Name: " + posts.get(i).getCourseName());
                                //System.out.println();
                                System.out.println("Discussion Forum: " +
                                        posts.get(i).getDiscussionTopic().getTopic());
                                //System.out.println(postArrayList.get(i).getDiscussionTopic().getTimeStamp());
                                //System.out.println();
                                System.out.println("Response: " + posts.get(i).getPost());
                                System.out.println(posts.get(i).getTimeStamp());
                                //System.out.println();
                                if (posts.get(i).getGrade() != -1) {
                                    System.out.println("Grade received: " + posts.get(i).getGrade());
                                } else if (posts.get(i).getGrade() == -1) {
                                    System.out.println("Not Graded yet by the Teacher!");
                                }
                                System.out.println();
                                counter++;
                            }

                        }
                        // If the counter remains 0
                        // it means student has not made any posts
                        if (counter == 0) {
                            System.out.println("You have not made any posts!");
                        }

                    } else if (studentSelection1 == 3) {
                        System.out.println("1. Edit Account");
                        System.out.println("2. Delete Account");


                        int editOrDelete = 0;
                        boolean notAnInteger4;
                        // checking for valid input
                        do {

                            notAnInteger4 = false;

                            try {
                                editOrDelete = Integer.parseInt(scanner.nextLine());

                                while (editOrDelete < 1 || editOrDelete > 2) {
                                    System.out.println("Please enter a valid number!");
                                    editOrDelete = Integer.parseInt(scanner.nextLine());
                                }
                            } catch (NumberFormatException numberFormatException) {
                                System.out.println("Invalid input! Please enter a valid number!");
                                notAnInteger4 = true;
                            }
                        } while (notAnInteger4);

                        objectOutputStream.writeObject(editOrDelete);
                        objectOutputStream.flush();

                        if (editOrDelete == 1) {
                            System.out.println("Please select the part you want to modify: " +
                                    "\n1. User name" +
                                    "\n2. Password");

                            boolean notAnInteger18 = false;
                            int modifyAccount = 0;
                            //Checking if the input provided is valid and meets the requirements
                            do {
                                notAnInteger18 = false;
                                try {
                                    modifyAccount = Integer.parseInt(scanner.nextLine());

                                    while (modifyAccount < 1 || modifyAccount > 2) {
                                        System.out.println("Please enter a valid number!");
                                        modifyAccount = Integer.parseInt(scanner.nextLine());
                                    }
                                } catch (NumberFormatException numberFormatException) {
                                    System.out.println("Invalid input! Please enter a valid number!");
                                    notAnInteger18 = true;
                                }
                            } while (notAnInteger18);

                            objectOutputStream.writeObject(modifyAccount);
                            objectOutputStream.flush();

                            //String oldUserName = lmsClient.person1.getUsername();
                            objectOutputStream.writeObject(lmsClient.person1);
                            objectOutputStream.flush();

                            if (modifyAccount == 1) {

                                boolean usernameVerify = false;
                                do {

                                    System.out.println("Please enter the new username:");
                                    String newUserName = scanner.nextLine();

                                    //Checking for null or empty string input
                                    while (newUserName == null || newUserName.equals("")) {
                                        System.out.println("Invalid input! Please enter a username!");
                                        newUserName = scanner.nextLine();
                                    }

                                    objectOutputStream.writeObject(newUserName);
                                    objectOutputStream.flush();

                                    usernameVerify = (Boolean) objectInputStream.readObject();

                                    if (usernameVerify) {
                                        System.out.println("Invalid. Username has already been taken. " +
                                                "Please enter another username.");
                                    }

                                } while (usernameVerify);

                            } else if (modifyAccount == 2) {

                                System.out.println("Please enter the new password:");
                                String newPassword = scanner.nextLine();
                                //person1.setPassword(newPassword);

                                while (newPassword == null || newPassword.equals("")) {
                                    System.out.println("Invalid input! Please enter a password!");
                                    newPassword = scanner.nextLine();
                                }

                                objectOutputStream.writeObject(newPassword);
                                objectOutputStream.flush();

                            }

                            System.out.println("Your account has been edited!");
                            System.out.println("Please login with your new credentials.");

                        } else if (editOrDelete == 2) {
                            objectOutputStream.writeObject(lmsClient.person1);
                            objectOutputStream.flush();
                            System.out.println("Your account has been deleted!");
                        }
                        exit = true;
                    } else if (studentSelection1 == 4) {
                        exit = true;
                    }

                    if (!exit) {

                        System.out.println("1. Select a course");
                        System.out.println("2. View my Grades");
                        System.out.println("3. Edit or delete account");
                        System.out.println("4. Exit");


                        studentSelection1String = scanner.nextLine();

                        while (!(studentSelection1String.equals("1") || studentSelection1String.equals("2") ||
                                studentSelection1String.equals("3") || studentSelection1String.equals("4"))) {
                            System.out.println("Invalid input! Please enter a valid number!");
                            studentSelection1String = scanner.nextLine();
                        }
                        studentSelection1 = Integer.parseInt(studentSelection1String);

                        objectOutputStream.writeObject(studentSelection1);
                        objectOutputStream.flush();
                    }

                } while (studentSelection1 > 0 && studentSelection1 < 4 && !exit);

            }

            System.out.println("1. Create an account");
            System.out.println("2. Sign in");
            System.out.println("3. Exit");
            // Checking for valid input
            do {
                notAnInteger = false;
                try {
                    userSelection = Integer.parseInt(scanner.nextLine());

                    while (userSelection < 1 || userSelection > 3) {

                        System.out.println("Please enter a valid number!");
                        userSelection = Integer.parseInt(scanner.nextLine());

                    }

                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Invalid input! Please enter a valid number!");
                    notAnInteger = true;
                }
            } while (notAnInteger);

            objectOutputStream.writeObject(userSelection);
            objectOutputStream.flush();


        } while (userSelection > 0 && userSelection < 3);

        System.out.println("Thank you for your time! Bye!");

    }
}
