import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
/**
 * Project 5 -- Post
 *
 * This class represents each post made by a student
 * and has attributes of a post set as a field
 *
 * @author Yuvika Khurana, lab sec 023
 *
 * @version May 2, 2022
 */
public class Post implements Serializable {

    String courseName;               // Name of the course under which post is made
    DiscussionTopic discussionTopic; // The Discussion Topic object of the discussion forum under which post is made
    String post;                     // The Post Content
    ArrayList<Reply> reply;         // An ArrayList of replies / comments made on the post
    Student student;                 // The Student who made the post
    String timeStamp;                // The timestamp for recording the time when the post is made
    double grade;                    // The grade received on the post


    //Post Constructor
    public Post(String courseName, DiscussionTopic discussionTopic, String post,
                ArrayList<Reply> reply, Student student) {
        this.courseName = courseName;
        this.discussionTopic = discussionTopic;
        this.post = post;
        this.reply = reply;
        this.student = student;
        this.grade = -1;                        // Grade initialised to -1
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        this.timeStamp = ts.toString();

    }

    //Getters and Setters for each field

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public DiscussionTopic getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(DiscussionTopic discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public ArrayList<Reply> getReply() {
        return reply;
    }

    public void setReply(ArrayList<Reply> reply) {
        this.reply = reply;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        String postString = courseName + "~";
        if (discussionTopic != null) {
            postString += discussionTopic.getTopic() + "~" + discussionTopic.getTimeStamp();
        } else {
            postString += "null";
        }

        if (post != null) {
            postString += "~" + post;
        } else {
            postString += "~" + "null";
        }

        postString += "~" + reply.size();

        for (int i = 0; i < reply.size(); i++) {
            postString += "~" + reply.get(i).getResponse();
            postString += "~" + reply.get(i).getPerson().getName();
            postString += "~" + reply.get(i).getPerson().getUsername();
            postString += "~" + reply.get(i).getPerson().getPassword();
            postString += "~" + reply.get(i).getTimeStamp();

        }

        if (student != null) {

            postString += "~" + student.getName();
            postString += "~" + student.getUsername();
            postString += "~" + student.getPassword();
        } else {
            postString += "~" + "null";
        }

        postString += "~" + timeStamp;

        postString += "~" + grade;

        return postString;

    }
}
