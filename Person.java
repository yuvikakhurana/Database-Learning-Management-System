import java.io.Serializable;
/**
 * Project 4 -- Person
 *
 * This class defines the users of the program.
 *
 * @author Yuvika Khurana, lab sec 023
 *
 * @version April 10, 2022
 */
public class Person implements Serializable {

    //Fields
    String name;
    String username;
    String password;

    //Person Constructor
    public Person(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    //GETTERS AND SETTERS FOR FIELDS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
