import java.io.Serializable;
import java.sql.Timestamp;
/**
 * Project 4 -- Reply
 *
 * This class represent each reply/comment made on a Post/Student Response on a DiscussionForum.
 *
 * @author Yuvika Khurana, lab sec 023
 *
 * @version April 10, 2022
 */
public class Reply implements Serializable {

    String response;    //the response/comment/reply to a Post
    Person person;      //the Person who made the comment - can be both - Student or Teacher
    String timeStamp;   //the timestamp for recording the time at which comment is made

    //Reply Constructor
    public Reply(String response, Person person) {
        this.response = response;
        this.person = person;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        this.timeStamp = ts.toString();
    }

    //Getters and Setters for each field
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
