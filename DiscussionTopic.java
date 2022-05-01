import java.io.Serializable;
import java.sql.Timestamp;
/**
 * Project 4 -- DiscussionTopic
 *
 * This class represents the Discussion Forum made under a course.
 *
 * @author Yuvika Khurana, lab sec 023
 *
 * @version April 10, 2022
 */
public class DiscussionTopic implements Serializable {

    String topic;                           // The String topic includes both -
    //1. Topic/Header of the Discussion Forum
    // and 2. its Prompt
    String timeStamp;                       // Timestamp for recording when the DiscussionForum was made


    //DiscussionTopic Constructor
    public DiscussionTopic(String topic) {
        this.topic = topic;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        this.timeStamp = ts.toString();

    }

    //Getters and Setters for DiscussionTopic

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


}
