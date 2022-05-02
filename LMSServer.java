import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
/**
 * Project 5 -- Learning Management System
 *
 * This class represents the server side of the project.
 * It accepts and establishes connection with multiple clients and processed the input received.
 * Server Port Number: 2111
 *
 * @author Yuvika Khurana, Benjamin Tapp,
 * Ruichen Liu, Himanshu Niraj Sethia, lab sec 023
 *
 * @version May 2, 2022
 */
public class LMSServer implements Runnable {

    private Person person2;
    Socket socket;

    static ArrayList<Person> personArrayList = new ArrayList<>(); //ArrayList for storing all the person objects
    static ArrayList<Post> postArrayList = new ArrayList<>();     //ArrayList for storing all the post objects
    static ArrayList<String> courses = new ArrayList<>();
    //private static final Object personGateKeeper = new Object();
    private static final Object postGateKeeper = new Object();


    

    public LMSServer(Socket socket) {
        this.person2 = person2;
        this.socket = socket;
    }

    //GETTERS and SETTERS FOR PERSON
    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }

    //METHOD TO WRITE TO THE FILE TO ENSURE DATA PERSISTENCE EVEN AFTER SERVER IS CLOSED
    public void writingToFile() {
        ObjectOutputStream oos3 = null;
        ObjectOutputStream oos4 = null;

        try {

            File f3 = new File("Posts.txt");
            FileOutputStream fos3 = new FileOutputStream(f3, false);
            oos3 = new ObjectOutputStream(fos3);


            for (int i = 0; i < postArrayList.size(); i++) {
                oos3.writeObject(postArrayList.get(i));
                oos3.flush();
            }

            File f4 = new File("Person.txt");
            FileOutputStream fos4 = new FileOutputStream(f4, false);
            oos4 = new ObjectOutputStream(fos4);

            for (int i = 0; i < personArrayList.size(); i++) {
                oos4.writeObject(personArrayList.get(i));
                oos4.flush();
            }


        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Ensure the folder has 2 files - Person.txt and Posts.txt " +
                    "to maintain the data persistence of the program");
            return;
        } catch (IOException e) {
            System.out.println("Error writing to the file!");
            e.printStackTrace();
            return;
        } finally {
            try {
                if (oos3 != null && oos4 != null) {
                    oos3.close();
                    oos4.close();
                }
            } catch (IOException ioException) {
                System.out.println("Error writing to the file!");
                ioException.printStackTrace();
                return;
            }
        }
    }

    //RUN METHOD FOR MULTIPLE THREADS
    public void run() {

        //LMSServer lmsServer = null;


        //For sending objects back and forth to the client

        ObjectInputStream objectInputStream;
        ObjectOutputStream objectOutputStream;


        try {
            //writer = new PrintWriter(socket.getOutputStream());
            //writer.flush();
            //in = new Scanner(socket.getInputStream());

            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();


            int userSelection = (Integer) objectInputStream.readObject();

            //User wants to create an account

            do {

                if (userSelection == 1) {

                    String name = (String) objectInputStream.readObject();

                    //Verifying username
                    boolean usernameVerify = false;
                    String username;
                    do {
                        usernameVerify = false;

                        username = (String) objectInputStream.readObject();


                        for (int i = 0; i < personArrayList.size(); i++) {
                            if (personArrayList.get(i).getUsername().equals(username)) {
                                usernameVerify = true;
                            }
                        }

                        objectOutputStream.writeObject(usernameVerify);
                        objectOutputStream.flush();

                    } while (usernameVerify);

                    String password = (String) objectInputStream.readObject();

                    int userRole = (Integer) objectInputStream.readObject();

                    if (userRole == 1) {
                        Teacher teacher = new Teacher(name, username, password);
                        synchronized (postGateKeeper) {
                            personArrayList.add(teacher);
                        }
                        this.setPerson2(teacher);
                    } else if (userRole == 2) {
                        Student student = new Student(name, username, password);
                        synchronized (postGateKeeper) {
                            personArrayList.add(student);
                        }
                        this.setPerson2(student);
                    }

                }
                if (userSelection == 2) {

                    String username;
                    String password;
                    String verifySignIn;

                    do {

                        username = (String) objectInputStream.readObject();

                        password = (String) objectInputStream.readObject();


                        verifySignIn = "false";

                        for (int i = 0; i < personArrayList.size(); i++) {
                            if (personArrayList.get(i).getUsername().equals(username)
                                    && personArrayList.get(i).getPassword().equals(password)) {
                                this.setPerson2(personArrayList.get(i));
                                verifySignIn = "true";
                                objectOutputStream.writeObject(verifySignIn);
                                objectOutputStream.flush();

                                //Sending the user credentials to the Client for signing in
                                if (personArrayList.get(i) instanceof Teacher) {
                                    objectOutputStream.writeObject("Teacher");
                                    objectOutputStream.flush();
                                } else if (personArrayList.get(i) instanceof Student) {
                                    objectOutputStream.writeObject("Student");
                                    objectOutputStream.flush();
                                }
                                objectOutputStream.writeObject(personArrayList.get(i).getName());
                                objectOutputStream.flush();
                                objectOutputStream.writeObject(personArrayList.get(i).getUsername());
                                objectOutputStream.flush();
                                objectOutputStream.writeObject(personArrayList.get(i).getPassword());
                                objectOutputStream.flush();
                            }
                        }

                        if (personArrayList == null || personArrayList.size() == 0) {
                            verifySignIn = "no accounts found";
                            objectOutputStream.writeObject(verifySignIn);
                            objectOutputStream.flush();
                            //break;
                        }

                        //NO ACCOUNT CREATE AN ACCOUNT IMPLEMENTATION LEFT
                        if (verifySignIn.equals("false")) {
                            objectOutputStream.writeObject(verifySignIn);
                            objectOutputStream.flush();
                        }

                    } while (verifySignIn.equals("false"));

                } else if (userSelection == 3) {
                    //System.out.println("Thank you. Bye!");
                    synchronized (postGateKeeper) {
                        writingToFile();
                    }
                    return;
                }

                if (this.getPerson2() != null && this.getPerson2() instanceof Teacher) {

                    int teacherUserSelection1 = (Integer) objectInputStream.readObject();
                    boolean exit;

                    do {

                        exit = false;

                        if (teacherUserSelection1 == 1) {
                            String courseName = (String) objectInputStream.readObject();

                            boolean sameCourse = false;
                            // Checking if the course already exists
                            for (int i = 0; i < courses.size(); i++) {
                                if (courses.get(i).equalsIgnoreCase(courseName)) {
                                    sameCourse = true;
                                }
                            }
                            objectOutputStream.writeObject(sameCourse);
                            objectOutputStream.flush();

                            if (!sameCourse) {
                                synchronized (postGateKeeper) {
                                    courses.add(courseName);
                                    postArrayList.add(new Post(courseName, null, null,
                                            new ArrayList<Reply>(), null));
                                }
                            }

                        } else if (teacherUserSelection1 == 2) {

                            if (courses == null || courses.size() == 0) {
                                objectOutputStream.writeObject("no courses");
                                objectOutputStream.flush();

                            } else {

                                boolean noDiscussionForums = false;

                                objectOutputStream.writeObject("courses exist");
                                objectOutputStream.flush();

                                objectOutputStream.writeObject(courses.size());
                                objectOutputStream.flush();
                                //sending the courseArrayList size

                                for (int i = 0; i < courses.size(); i++) {
                                    objectOutputStream.writeObject(courses.get(i));
                                    objectOutputStream.flush();
                                }

                                int courseNumberSelected = (Integer) objectInputStream.readObject();

                                String courseName = courses.get(courseNumberSelected - 1);
                                objectOutputStream.writeObject(courseName);
                                objectOutputStream.flush();

                                for (int i = 0; i < postArrayList.size(); i++) {
                                    if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName)
                                            && postArrayList.get(i).getDiscussionTopic() == null) {
                                        noDiscussionForums = true;
                                        objectOutputStream.writeObject(noDiscussionForums);
                                        objectOutputStream.flush();
                                        break;
                                    } else if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName)) {
                                        noDiscussionForums = false;
                                        objectOutputStream.writeObject(noDiscussionForums);
                                        objectOutputStream.flush();
                                        //objectOutputStream.writeObject(postArrayList.size());
                                        //objectOutputStream.flush();
                                        //for (int j = 0; j < postArrayList.size(); j++) {
                                        //  objectOutputStream.writeObject(postArrayList.get(j).toString());
                                        //objectOutputStream.flush();
                                        //}
                                        break;
                                    }
                                }

                                int teacherSelection2 = (Integer) objectInputStream.readObject();

                                if (teacherSelection2 == 1) {

                                    boolean checkSameDiscussionForum;
                                    String topic = "";
                                    boolean check = true;

                                    do {
                                        checkSameDiscussionForum = false;

                                        topic = (String) objectInputStream.readObject();

                                        for (int i = 0; i < postArrayList.size(); i++) {
                                            if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName) &&
                                                    postArrayList.get(i).getDiscussionTopic() != null &&
                                                    postArrayList.get(i).getDiscussionTopic().getTopic(
                                                    ).equalsIgnoreCase(topic)) {
                                                checkSameDiscussionForum = true;
                                                //break;
                                            }
                                        }

                                        objectOutputStream.writeObject(checkSameDiscussionForum);
                                        objectOutputStream.flush();

                                    } while (checkSameDiscussionForum);

                                    // Once a valid Discussion Forum has been input
                                    // A for loop to check if there is a Post Object
                                    // that has the selected course and DiscussionTopic set to null
                                    // If there is, it will set the null object to the Discussion Forum
                                    // that has been input and change the boolean
                                    // variable "check" to false
                                    synchronized (postGateKeeper) {
                                        for (int i = 0; i < postArrayList.size(); i++) {
                                            if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName)
                                                    && postArrayList.get(i).getDiscussionTopic() == null) {
                                                postArrayList.get(i).setDiscussionTopic(new DiscussionTopic(topic));
                                                check = false;
                                                break;
                                            }
                                        }
                                    }

                                    // If check remains true, that is there is no Post Object that
                                    // has the selected Course name and null DiscussionTopic Object
                                    // It will create a new Post Object with the selected Course name
                                    // and the Discussion Forum inputted
                                    // and set the Student User and the Post parameters to null
                                    // since no post has been made yet
                                    // For the reply parameter, it will set it to an empty ArrayList of the Reply Object
                                    if (check) {
                                        Post post1 = new Post(courseName, new DiscussionTopic(topic),
                                                null, new ArrayList<Reply>(), null);
                                        synchronized (postGateKeeper) {
                                            postArrayList.add(post1);
                                        }
                                    }


                                } else if (teacherSelection2 == 2) {

                                    if (!noDiscussionForums) {

                                        objectOutputStream.writeObject(postArrayList.size());
                                        objectOutputStream.flush();
                                        for (int j = 0; j < postArrayList.size(); j++) {
                                            objectOutputStream.writeObject(postArrayList.get(j).toString());
                                            objectOutputStream.flush();
                                        }

                                        String s = (String) objectInputStream.readObject();

                                        int counter1 = 0;

                                        // Checking if there is only one post object that has the selected CourseName
                                        for (int i = 0; i < postArrayList.size(); i++) {
                                            if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName)) {
                                                counter1++;
                                            }
                                        }

                                        // If there is only one Post object with the selected CourseName,
                                        // it will set the DiscussionTopic to null
                                        // and re-initiate other values as well
                                        if (counter1 == 1) {
                                            synchronized (postGateKeeper) {
                                                for (int i = 0; i < postArrayList.size(); i++) {
                                                    if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                            courseName)
                                                            && postArrayList.get(i).getDiscussionTopic().getTopic(
                                                    ).equalsIgnoreCase(s)) {
                                                        postArrayList.get(i).setDiscussionTopic(null);
                                                        postArrayList.get(i).setPost(null);
                                                        postArrayList.get(i).setReply(new ArrayList<Reply>());
                                                        postArrayList.get(i).setStudent(null);

                                                    }
                                                }
                                            }
                                        } else { // If there are multiple Objects that has the selected CourseName
                                            // It will remove all the Post Object that has the selected discussionForum
                                            boolean verifyDeletion = true;
                                            do {
                                                verifyDeletion = true;
                                                synchronized (postGateKeeper) {
                                                    for (int i = 0; i < postArrayList.size(); i++) {
                                                        if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                                courseName) && postArrayList.get(i).getDiscussionTopic(
                                                        ).getTopic().equalsIgnoreCase(s)) {
                                                            postArrayList.remove(i);
                                                        }
                                                    }
                                                }

                                                //remove method of ArrayList won't consider the next element
                                                // even if fulfils the condition mentioned
                                                // since it uses pushing up/down approach

                                                for (int i = 0; i < postArrayList.size(); i++) {
                                                    if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                            courseName) && postArrayList.get(i).getDiscussionTopic(
                                                    ).getTopic().equalsIgnoreCase(s)) {
                                                        verifyDeletion = false;
                                                    }
                                                }
                                            } while (!verifyDeletion);

                                        }

                                        // Ensuring the Course Name doesn't get deleted with the post objects
                                        // Checking if there is a course name in courses ArrayList
                                        // that is not present in teh PostArraylist
                                        // Making an object with the course name if there isn't

                                        for (int i = 0; i < courses.size(); i++) {
                                            boolean verify = false;

                                            for (int j = 0; j < postArrayList.size(); j++) {
                                                if (courses.get(i).equals(postArrayList.get(j).getCourseName())) {
                                                    verify = true;
                                                    break;

                                                }
                                            }

                                            if (!verify) {
                                                Post post1 = new Post(courses.get(i), null, null,
                                                        new ArrayList<Reply>(), null);
                                                synchronized (postGateKeeper) {
                                                    postArrayList.add(post1);
                                                }
                                            }
                                        }

                                    }

                                } else if (teacherSelection2 == 3) {

                                    if (!noDiscussionForums) {

                                        objectOutputStream.writeObject(postArrayList.size());
                                        objectOutputStream.flush();
                                        for (int j = 0; j < postArrayList.size(); j++) {
                                            objectOutputStream.writeObject(postArrayList.get(j).toString());
                                            objectOutputStream.flush();
                                        }


                                        String topic = (String) objectInputStream.readObject();

                                        String newTopic = (String) objectInputStream.readObject();
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < postArrayList.size(); i++) {
                                                if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName) &&
                                                        postArrayList.get(i).getDiscussionTopic().getTopic(
                                                        ).equalsIgnoreCase(topic)) {
                                                    postArrayList.get(i).getDiscussionTopic().setTopic(newTopic);
                                                }
                                            }
                                        }
                                    }

                                } else if (teacherSelection2 == 4) {

                                    if (!noDiscussionForums) {

                                        objectOutputStream.writeObject(postArrayList.size());
                                        objectOutputStream.flush();
                                        for (int j = 0; j < postArrayList.size(); j++) {
                                            objectOutputStream.writeObject(postArrayList.get(j).toString());
                                            objectOutputStream.flush();
                                        }

                                        String topic2 = (String) objectInputStream.readObject();
                                        boolean noResponses = (Boolean) objectInputStream.readObject();

                                        if (!noResponses) {

                                            int teacherReplyStudents = (Integer) objectInputStream.readObject();

                                            if (teacherReplyStudents == 1) {

                                                String response = (String) objectInputStream.readObject();

                                                String reply = (String) objectInputStream.readObject();
                                                synchronized (postGateKeeper) {
                                                    for (int i = 0; i < postArrayList.size(); i++) {
                                                        if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                                courseName)
                                                                && postArrayList.get(i).getDiscussionTopic().getTopic(
                                                        ).equalsIgnoreCase(topic2)
                                                                && postArrayList.get(i).getPost() != null
                                                                && postArrayList.get(i).getPost().equals(response)) {
                                                            postArrayList.get(i).getReply().add(0,
                                                                    new Reply(reply, this.person2));

                                                        }
                                                    }
                                                }

                                            } else if (teacherReplyStudents == 2) {

                                                String response = (String) objectInputStream.readObject();
                                                boolean checkSamePost;
                                                String editedResponse;

                                                do {

                                                    checkSamePost = false;

                                                    editedResponse = (String) objectInputStream.readObject();


                                                    for (int i = 0; i < postArrayList.size(); i++) {
                                                        if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                                courseName)
                                                                && postArrayList.get(i).getDiscussionTopic().getTopic(
                                                        ).equalsIgnoreCase(topic2)
                                                                && postArrayList.get(i).getPost() != null
                                                                && postArrayList.get(i).getPost().equalsIgnoreCase(
                                                                editedResponse)) {
                                                            checkSamePost = true;
                                                            break;
                                                        }
                                                    }

                                                    objectOutputStream.writeObject(checkSamePost);
                                                    objectOutputStream.flush();

                                                } while (checkSamePost);

                                                synchronized (postGateKeeper) {
                                                    for (int i = 0; i < postArrayList.size(); i++) {
                                                        if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                                courseName)
                                                                && postArrayList.get(i).getDiscussionTopic().getTopic(
                                                        ).equalsIgnoreCase(topic2)
                                                                && postArrayList.get(i).getPost() != null
                                                                && postArrayList.get(i).getPost().equals(response)) {
                                                            postArrayList.get(i).setPost(editedResponse);
                                                        }
                                                    }
                                                }

                                            } else if (teacherReplyStudents == 3) {

                                                String response = (String) objectInputStream.readObject();

                                                int postsCounter = 0;
                                                // Checking how many posts that have the
                                                // selected course and discussion forum exists
                                                for (int i = 0; i < postArrayList.size(); i++) {
                                                    if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                            courseName)
                                                            && postArrayList.get(i).getDiscussionTopic(
                                                    ).getTopic().equalsIgnoreCase(topic2)
                                                            && postArrayList.get(i).getPost() != null) {
                                                        postsCounter += 1;
                                                    }
                                                }

                                                // If there is only one post that has
                                                // the selected course name and discussion topic
                                                // It would set the post to null
                                                // and re-initiate other parameters
                                                // so that the course name and discussionTopic
                                                // in postArrayList is preserved
                                                if (postsCounter == 1) {
                                                    synchronized (postGateKeeper) {
                                                        for (int i = 0; i < postArrayList.size(); i++) {
                                                            if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                                    courseName)
                                                                    && postArrayList.get(i).getDiscussionTopic(
                                                            ).getTopic().equalsIgnoreCase(topic2)
                                                                    && postArrayList.get(i).getPost() != null
                                                                    && postArrayList.get(i).getPost().equalsIgnoreCase(
                                                                    response)) {
                                                                postArrayList.get(i).setPost(null);
                                                                postArrayList.get(i).setReply(new ArrayList<Reply>());
                                                                postArrayList.get(i).setStudent(null);
                                                                postArrayList.get(i).setGrade(-1);
                                                                postArrayList.get(i).setTimeStamp("deleted");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    // If the discussion forum has multiple posts,
                                                    // it would remove that Post Object from the ArrayList
                                                    synchronized (postGateKeeper) {
                                                        for (int i = 0; i < postArrayList.size(); i++) {
                                                            if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                                    courseName)
                                                                    && postArrayList.get(i).getDiscussionTopic(
                                                            ).getTopic().equalsIgnoreCase(topic2)
                                                                    && postArrayList.get(i).getPost() != null
                                                                    && postArrayList.get(i).getPost().equalsIgnoreCase(
                                                                    response)) {
                                                                postArrayList.remove(i);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (teacherUserSelection1 == 3) {

                            ArrayList<String> studentUsernameArrayList = new ArrayList<>();

                            synchronized (postGateKeeper) {
                                for (int i = 0; i < personArrayList.size(); i++) {
                                    if (personArrayList.get(i) instanceof Student) {
                                        studentUsernameArrayList.add(personArrayList.get(i).getUsername());
                                    }
                                }
                            }

                            objectOutputStream.writeObject(studentUsernameArrayList.size());
                            objectOutputStream.flush();
                            for (int j = 0; j < studentUsernameArrayList.size(); j++) {
                                objectOutputStream.writeObject(studentUsernameArrayList.get(j));
                                objectOutputStream.flush();
                            }

                            if (studentUsernameArrayList.size() != 0) {

                                int studentSelected = (Integer) objectInputStream.readObject();

                                String studentUsernameSelected = studentUsernameArrayList.get(studentSelected - 1);
                                ArrayList<Post> studentPosts = new ArrayList<>();

                                synchronized (postGateKeeper) {
                                    for (int i = 0; i < postArrayList.size(); i++) {
                                        if (postArrayList.get(i).getStudent() != null &&
                                                postArrayList.get(i).getStudent().getUsername().equals(
                                                        studentUsernameSelected)) {
                                            studentPosts.add(postArrayList.get(i));
                                        }
                                    }
                                }

                                objectOutputStream.writeObject(studentPosts.size());
                                objectOutputStream.flush();
                                for (int j = 0; j < studentPosts.size(); j++) {
                                    objectOutputStream.writeObject(studentPosts.get(j).toString());
                                    objectOutputStream.flush();
                                }

                                if (studentPosts.size() != 0) {
                                    synchronized (postGateKeeper) {
                                        for (int i = 0; i < postArrayList.size(); i++) {
                                            if (postArrayList.get(i).getStudent() != null
                                                    && postArrayList.get(i).getStudent().getUsername().equals(
                                                    studentUsernameSelected)) {
                                                postArrayList.get(i).setGrade((double) objectInputStream.readObject());
                                            }
                                        }
                                    }
                                }
                            }


                        } else if (teacherUserSelection1 == 4) {

                            int editOrDelete = (Integer) objectInputStream.readObject();

                            if (editOrDelete == 1) {

                                int modifyAccount = (Integer) objectInputStream.readObject();
                                String instanceOf = (String) objectInputStream.readObject();
                                String oldUserName = (String) objectInputStream.readObject();
                                String newUserName;

                                if (modifyAccount == 1) {

                                    boolean usernameVerify = false;
                                    do {
                                        usernameVerify = false;

                                        newUserName = (String) objectInputStream.readObject();

                                        for (int i = 0; i < personArrayList.size(); i++) {
                                            if (personArrayList.get(i).getUsername().equals(newUserName)) {
                                                usernameVerify = true;
                                            }
                                        }

                                        objectOutputStream.writeObject(usernameVerify);
                                        objectOutputStream.flush();


                                    } while (usernameVerify);

                                    if (instanceOf.equals("Student")) {

                                        //Changing the username for every Post the Student user has made
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < postArrayList.size(); i++) {
                                                if (postArrayList.get(i).getStudent() != null
                                                        && postArrayList.get(i).getStudent().getUsername().equals(
                                                        oldUserName)) {
                                                    postArrayList.get(i).getStudent().setUsername(newUserName);
                                                }
                                            }
                                        }

                                        //Changing the username is the account details Person ArrayList
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < personArrayList.size(); i++) {
                                                if (personArrayList.get(i).getUsername().equals(oldUserName)) {
                                                    personArrayList.get(i).setUsername(newUserName);
                                                }
                                            }
                                        }

                                    } else if (instanceOf.equals("Teacher")) {

                                        //Changing the username is the account details Person ArrayList
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < personArrayList.size(); i++) {
                                                if (personArrayList.get(i).getUsername().equals(oldUserName)) {
                                                    personArrayList.get(i).setUsername(newUserName);
                                                }
                                            }
                                        }

                                    }

                                } else if (modifyAccount == 2) {

                                    String newPassword = (String) objectInputStream.readObject();


                                    if (instanceOf.equals("Student")) {
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < postArrayList.size(); i++) {
                                                if (postArrayList.get(i).getStudent() != null
                                                        && postArrayList.get(i).getStudent().getUsername().equals(
                                                        oldUserName)) {
                                                    postArrayList.get(i).getStudent().setPassword(newPassword);
                                                }
                                            }
                                        }

                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < personArrayList.size(); i++) {
                                                if (personArrayList.get(i).getUsername().equals(oldUserName)) {
                                                    personArrayList.get(i).setPassword(newPassword);
                                                }
                                            }
                                        }
                                    } else if (instanceOf.equals("Teacher")) {
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < personArrayList.size(); i++) {
                                                if (personArrayList.get(i).getUsername().equals(oldUserName)) {
                                                    personArrayList.get(i).setPassword(newPassword);
                                                }
                                            }
                                        }

                                    }


                                }

                            } else if (editOrDelete == 2) {

                                String deletingUsername = (String) objectInputStream.readObject();


                                synchronized (postGateKeeper) {
                                    for (int i = 0; i < personArrayList.size(); i++) {

                                        if (deletingUsername.equals(personArrayList.get(i).getUsername())) {
                                            personArrayList.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }

                            exit = true;

                        } else if (teacherUserSelection1 == 5) {
                            exit = true;
                        }

                        if (!exit) {

                            teacherUserSelection1 = (Integer) objectInputStream.readObject();

                        }


                    } while (teacherUserSelection1 > 0 && teacherUserSelection1 < 5 && !exit);

                } else if (this.getPerson2() != null && this.person2 instanceof Student) {

                    int studentSelection1 = (Integer) objectInputStream.readObject();
                    boolean exit;

                    do {

                        exit = false;

                        if (studentSelection1 == 1) {

                            if (courses == null || courses.size() == 0) {
                                objectOutputStream.writeObject("no courses");
                                objectOutputStream.flush();

                            } else {

                                boolean noDiscussionForums = false;

                                objectOutputStream.writeObject("courses exist");
                                objectOutputStream.flush();

                                objectOutputStream.writeObject(courses.size());
                                objectOutputStream.flush();
                                //sending the courseArrayList size

                                for (int i = 0; i < courses.size(); i++) {
                                    objectOutputStream.writeObject(courses.get(i));
                                    objectOutputStream.flush();
                                }

                                int courseNumberSelected = (Integer) objectInputStream.readObject();

                                String courseName = courses.get(courseNumberSelected - 1);
                                objectOutputStream.writeObject(courseName);
                                objectOutputStream.flush();

                                for (int i = 0; i < postArrayList.size(); i++) {
                                    if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName)
                                            && postArrayList.get(i).getDiscussionTopic() == null) {
                                        noDiscussionForums = true;
                                        objectOutputStream.writeObject(noDiscussionForums);
                                        objectOutputStream.flush();
                                        break;
                                    } else if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName)) {
                                        noDiscussionForums = false;
                                        objectOutputStream.writeObject(noDiscussionForums);
                                        objectOutputStream.flush();
                                        break;
                                    }
                                }

                                if (!noDiscussionForums) {

                                    int studentSelection2 = (Integer) objectInputStream.readObject();

                                    if (studentSelection2 == 1) {

                                        objectOutputStream.writeObject(postArrayList.size());
                                        objectOutputStream.flush();
                                        for (int j = 0; j < postArrayList.size(); j++) {
                                            objectOutputStream.writeObject(postArrayList.get(j).toString());
                                            objectOutputStream.flush();
                                        }

                                        String topic2 = (String) objectInputStream.readObject();
                                        boolean noResponses = (Boolean) objectInputStream.readObject();

                                        int studentSelection3 = (Integer) objectInputStream.readObject();

                                        if (studentSelection3 == 1) {

                                            String studentPost;
                                            boolean samePost;

                                            do {
                                                samePost = false;
                                                studentPost = (String) objectInputStream.readObject();

                                                for (int i = 0; i < postArrayList.size(); i++) {
                                                    if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                            courseName)
                                                            && postArrayList.get(i).getDiscussionTopic().getTopic(
                                                    ).equalsIgnoreCase(
                                                            topic2)
                                                            && postArrayList.get(i).getPost() != null
                                                            && postArrayList.get(i).getPost().equalsIgnoreCase(
                                                            studentPost)) {
                                                        samePost = true;
                                                        break;
                                                    }
                                                }

                                                objectOutputStream.writeObject(samePost);
                                                objectOutputStream.flush();


                                            } while (samePost);

                                            boolean checked = true;
                                            // checking if there is a Post Object with the selected course name
                                            // and discussionTopic
                                            // and null Post string
                                            // if there is, it will set the Post String to the new post created
                                            // it will also check for the timestamp
                                            // It will re-initialize the value of timestamp
                                            synchronized (postGateKeeper) {
                                                for (int i = 0; i < postArrayList.size(); i++) {
                                                    if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                            courseName)
                                                            && postArrayList.get(i).getDiscussionTopic().getTopic(
                                                    ).equals(topic2)
                                                            && postArrayList.get(i).getPost() == null) {
                                                        postArrayList.get(i).setPost(studentPost);
                                                        postArrayList.get(i).setStudent((Student) this.getPerson2());
                                                        postArrayList.get(i).setTimeStamp(
                                                                new Timestamp(System.currentTimeMillis()).toString());
                                                        checked = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            DiscussionTopic dt = null;
                                            // Retrieving the discussion topic object
                                            // to create a new Post Object if needed
                                            for (int i = 0; i < postArrayList.size(); i++) {
                                                if (postArrayList.get(i).getCourseName().equalsIgnoreCase(courseName)
                                                        && postArrayList.get(i).getDiscussionTopic() != null
                                                        && postArrayList.get(i).getDiscussionTopic().getTopic(
                                                ).equals(topic2)) {
                                                    dt = postArrayList.get(i).getDiscussionTopic();
                                                }
                                            }
                                            // If there is no object which has Post String set to null
                                            // It will create a new Post object as per the requirements and add it to
                                            // the zeroth index of the postArrayList to ensure
                                            //  the newest response is printed first
                                            if (checked) {
                                                Post post1 = new Post(courseName, dt, studentPost,
                                                        new ArrayList<Reply>(), (Student) this.getPerson2());
                                                synchronized (postGateKeeper) {
                                                    postArrayList.add(0, post1);
                                                }
                                            }

                                        } else if (studentSelection3 == 2) {
                                            if (!noResponses) {

                                                String response = (String) objectInputStream.readObject();

                                                String reply = (String) objectInputStream.readObject();
                                                synchronized (postGateKeeper) {
                                                    for (int i = 0; i < postArrayList.size(); i++) {
                                                        if (postArrayList.get(i).getCourseName().equalsIgnoreCase(
                                                                courseName)
                                                                && postArrayList.get(i).getDiscussionTopic().getTopic(
                                                        ).equalsIgnoreCase(topic2)
                                                                && postArrayList.get(i).getPost() != null
                                                                && postArrayList.get(i).getPost().equals(response)) {
                                                            postArrayList.get(i).getReply().add(0,
                                                                    new Reply(reply, this.person2));

                                                        }
                                                    }
                                                }

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

                            objectOutputStream.writeObject(postArrayList.size());
                            objectOutputStream.flush();
                            for (int j = 0; j < postArrayList.size(); j++) {
                                objectOutputStream.writeObject(postArrayList.get(j).toString());
                                objectOutputStream.flush();
                            }

                        } else if (studentSelection1 == 3) {
                            int editOrDelete = (Integer) objectInputStream.readObject();

                            if (editOrDelete == 1) {

                                int modifyAccount = (Integer) objectInputStream.readObject();
                                String instanceOf = (String) objectInputStream.readObject();
                                String oldUserName = (String) objectInputStream.readObject();
                                String newUserName;

                                if (modifyAccount == 1) {

                                    boolean usernameVerify = false;
                                    do {
                                        usernameVerify = false;

                                        newUserName = (String) objectInputStream.readObject();

                                        for (int i = 0; i < personArrayList.size(); i++) {
                                            if (personArrayList.get(i).getUsername().equals(newUserName)) {
                                                usernameVerify = true;
                                            }
                                        }

                                        objectOutputStream.writeObject(usernameVerify);
                                        objectOutputStream.flush();


                                    } while (usernameVerify);

                                    if (instanceOf.equals("Student")) {

                                        //Changing the username for every Post the Student user has made
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < postArrayList.size(); i++) {
                                                if (postArrayList.get(i).getStudent() != null
                                                        && postArrayList.get(i).getStudent().getUsername().equals(
                                                        oldUserName)) {
                                                    postArrayList.get(i).getStudent().setUsername(newUserName);
                                                }
                                            }
                                        }

                                        //Changing the username is the account details Person ArrayList
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < personArrayList.size(); i++) {
                                                if (personArrayList.get(i).getUsername().equals(oldUserName)) {
                                                    personArrayList.get(i).setUsername(newUserName);
                                                }
                                            }
                                        }

                                    } else if (instanceOf.equals("Teacher")) {

                                        //Changing the username is the account details Person ArrayList
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < personArrayList.size(); i++) {
                                                if (personArrayList.get(i).getUsername().equals(oldUserName)) {
                                                    personArrayList.get(i).setUsername(newUserName);
                                                }
                                            }
                                        }

                                    }

                                } else if (modifyAccount == 2) {

                                    String newPassword = (String) objectInputStream.readObject();


                                    if (instanceOf.equals("Student")) {
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < postArrayList.size(); i++) {
                                                if (postArrayList.get(i).getStudent() != null
                                                        && postArrayList.get(i).getStudent().getUsername().equals(
                                                        oldUserName)) {
                                                    postArrayList.get(i).getStudent().setPassword(newPassword);
                                                }
                                            }
                                        }

                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < personArrayList.size(); i++) {
                                                if (personArrayList.get(i).getUsername().equals(oldUserName)) {
                                                    personArrayList.get(i).setPassword(newPassword);
                                                }
                                            }
                                        }
                                    } else if (instanceOf.equals("Teacher")) {
                                        synchronized (postGateKeeper) {
                                            for (int i = 0; i < personArrayList.size(); i++) {
                                                if (personArrayList.get(i).getUsername().equals(oldUserName)) {
                                                    personArrayList.get(i).setPassword(newPassword);
                                                }
                                            }
                                        }

                                    }


                                }

                            } else if (editOrDelete == 2) {

                                String deletingUsername = (String) objectInputStream.readObject();


                                synchronized (postGateKeeper) {
                                    for (int i = 0; i < personArrayList.size(); i++) {

                                        if (deletingUsername.equals(personArrayList.get(i).getUsername())) {
                                            personArrayList.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                            exit = true;
                        } else if (studentSelection1 == 4) {
                            exit = true;
                        }

                        if (!exit) {

                            studentSelection1 = (Integer) objectInputStream.readObject();

                        }
                    } while (studentSelection1 > 0 && studentSelection1 < 4 && !exit);
                }

                userSelection = (Integer) objectInputStream.readObject();

            } while (userSelection > 0 && userSelection < 3);

        } catch (Exception exception) {
            System.out.println("There was an error in the writing/reading objects from the client " +
                    "or the client closed the program abruptly");
            exception.printStackTrace();
        }

        synchronized (postGateKeeper) {
            writingToFile();
        }
    }

    public static void main(String[] args) {

        //For data persistence even after server ends
        File f1;
        FileInputStream fis1;
        ObjectInputStream ois1 = null;

        File f;
        FileInputStream fis;
        ObjectInputStream ois = null;

        Person person;
        Post post;


        //READING ALL THE PERSON OBJECTS FROM A FILE CALLED "Person.txt" AND ADDING IT TO AN ARRAYLIST(personArrayList)

        try {
            f = new File("Person.txt");
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            person = (Person) ois.readObject();

            while (person != null) {

                personArrayList.add(person);

                person = (Person) ois.readObject();
            }

        } catch (EOFException e) {
            person = null;
        } catch (ClassNotFoundException e) {
            System.out.println("Error reading the File: Class Not Found Exception!");
            e.printStackTrace();
            return;
        } catch (FileNotFoundException e) {
            System.out.println(" Please have a file named : Person.txt to ensure data persistence for the program");
            return;
        } catch (IOException e) {
            System.out.println("Error reading the file!");
            e.printStackTrace();
            return;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                System.out.println("Error reading the file!");
                e.printStackTrace();
                return;
            }
        }


        //READING ALL THE POSTS AND ADDING IT TO 2 ARRAYLIST - postArrayList, courses

        try {
            f1 = new File("Posts.txt");
            fis1 = new FileInputStream(f1);
            ois1 = new ObjectInputStream(fis1);
            post = (Post) ois1.readObject();

            while (post != null) {
                postArrayList.add(post);

                if (!courses.contains(post.getCourseName())) {
                    courses.add(post.getCourseName());
                }

                post = (Post) ois1.readObject();
            }
        } catch (EOFException e) {
            post = null;
        } catch (ClassNotFoundException e) {
            System.out.println("Error reading the File: Class Not Found Exception!");
            e.printStackTrace();
            return;
        } catch (FileNotFoundException e) {
            System.out.println(" Please have a file named : Posts.txt to ensure data persistence for the program");
            return;
        } catch (IOException e) {
            System.out.println("Error reading the file!");
            e.printStackTrace();
            return;
        } finally {
            try {
                if (ois1 != null) {
                    ois1.close();
                }
            } catch (IOException e) {
                System.out.println("Error reading the file!");
                e.printStackTrace();
                return;
            }
        }

        //Establishing a server socket with the port number 2111
        //running an infinite loop as it accepts connection from clients
        try {
            ServerSocket serverSocket = new ServerSocket(2111);

            while (true) {
                Socket socket = serverSocket.accept();
                LMSServer lmsServer = new LMSServer(socket);
                new Thread(lmsServer).start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();

        }

    }
}
