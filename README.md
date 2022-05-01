# CS180
Project 5

# How to Compile and Run

The Learning Management System allows teachers and students to interact with discussion forums. Users running LMSClient will be able to connect to the LMSServer, and all the functions and processing will be run on the server side. Users can create an account and choose their role or login if they already have an account. Teachers are allowed to create and edit courses and discussion topics in the system. Teachers also have permission to edit, delete and reply to posts made by students and assign grades to the post. Students can make posts and reply in discussion forums. All the data will be stored in files to ensure even if the server stops, all the data will not be lost. The program also allows multiple users to connect to the server at the same time, and each user won't interrupt other users' action. All the updates of posts, comments and discussion forums will be refreshed after the user leaves and reenters the page.

The user should have 2 text files called &quot;Person.txt&quot;  and &quot;Post.txt&quot;  to run the program. The user should run both LMSServer and LMSClient. The user should enter the host name and port number through GUI pop-up in the LMSClient program. If the server and client are on the same computer, the host name should be localhost; if they are on different computers, the host name should be the IP address of the computer running the server program. The host number is &quot;2111&quot; . If connected correctly, a GUI message will pop up and say the connection is established.

Users will use GUI pop up to interact with the program. There will be buttons to help users get into the function they want to use, as well as drop-down menus for selecting courses, discussion forums, and posts.

# Parts that Submitted by Student

Benjamin Tapp - Submitted Report on Brightspace.

Yuvika Khurana - Submitted Vocareum workspace.

# Classes

## LMSServer.java

The Server side of the project, and process all the data.

### Fields

| Name             | Type                | Modifiers            | Description                                                                                    |
|------------------|---------------------|----------------------|------------------------------------------------------------------------------------------------|
| person2          | Person              | private              | A new Person object.                                                                           || lms             | LearningManagementSystem | private   | A new LearningManagementSystem object. |
| personArrayList  | ArrayList< Person > | private              | The ArrayList of all the users.                                                                |
| postArrayList    | ArrayList< Post >   | private              | The ArrayList of all the posts.                                                                |
| courses          | ArrayList< String > | private              | The ArrayList of all the courses.                                                              |
| personGateKeeper | Object              | Private static final | Make sure all actions associated with person objects run in order, won't interrupt each other. |
| postGateKeeper   | Object              | Private static final | Make sure all actions associated with posts objects run in order, won't interrupt each other.  |

### Constructor

| Parameters     | Modifier | Description                                                 |
|----------------|----------|-------------------------------------------------------------|
| Person person2 | public   | Initialize the class fields to their associated parameters. |

### Methods

| Name       | Return type | Parameters     | Modifiers | Description                                                                                                           |
|------------|-------------|----------------|-----------|-----------------------------------------------------------------------------------------------------------------------|
| getPerson2 | Person      | None           | public    | Returns the Person object person2.                                                                                    |
| setPerson2 | void        | Person person2 | public    | Sets the Person object person2.                                                                                       |
| run        | void        | None           | public    | The run method of the LMSServer class, that runs in which receives, process and sends data from and to the LMSClient. |

## LMSClient.java

The Client side of the project, user uses GUI to interact with the system. The client will send user inputs to the server and receives data from the server. 

### Fields

| Name               | Type               | Modifiers      | Description                           |
|--------------------|--------------------|----------------|---------------------------------------|
| person1            | Person             | None           | A person object.                      |
| objectOutputStream | ObjectOutputStream | private static | The object that writes objects.       |
| objectInputStream  | ObjectInputStream  | private static | The object that reads objects.        |
| socket             | Socket             | private static | A socket that connects to the server. |

### Constructor

| Parameters     | Modifier | Description                                                 |
|----------------|----------|-------------------------------------------------------------|
| Person person1 | public   | Initialize the class fields to their associated parameters. |

### Methods

| Name                     | Return type               | Parameters                                    | Modifiers | Description                                                                                            |
|--------------------------|---------------------------|-----------------------------------------------|-----------|--------------------------------------------------------------------------------------------------------|
| getPerson1               | Person                    | None                                          | public    | Returns the Person object person1.                                                                     |
| setPerson1               | void                      | Person person1                                | public    | Sets the Person object person1.                                                                        |
| inputHostName            | String                    | None                                          | public    | A GUI pop up to allot user enter the host name.                                                        |
| stringInLinesMethod      | String                    | String s                                      | public    | A method to break any string longer than 160 characters to smaller p                                   |
| fileImporter             | ArrayList< String > | String fileName                               | public    | Allow users to import files for discussion topics, posts and replies.                                  |
| titleGetter              | String                    | ArrayList< String >fileData            | public    | Gets the title of the discussion forums.                                                               |
| stringToPost             | Post                      | String postString                             | public    | Convert a Post object to a string, for display in GUI.                                                 |
| studentSelection1GUI     | void                      | None                                          | public    | A GUI pop up for most of student&#39;s functions.                                                      |
| createCourseGUI          | void                      | None                                          | public    | A GUI pop up for teachers to create a course.                                                          |
| createDiscussionGUI      | void                      | None                                          | public    | A GUI pop up for teachers to create a discussion forum under a course.                                 |
| noDiscussionGUI          | void                      | None                                          | public    | A warning GUI pop up to tell the user there aren&#39;t any discussion forum under the selected course. |
| gradeStudentGUI          | void                      | None                                          | public    | A GUI pop up for teachers to grade students&#39; posts.                                                |
| deleteDiscussionGUI      | void                      | boolean noDiscussionForum, String courseName1 | public    | A GUI that allows teachers to delete a discussion forum.                                               |
| editDiscussionGUI        | void                      | boolean noDiscussionForum, String courseName1 | public    | A GUI that allows teachers to edit a discussion forum.                                                 |
| commentOnStudentPostsGUI | void                      | None                                          | public    | A GUI that allows other users to comment on a student&#39;s post.                                      |
| editStudentPostGUI       | void                      | None                                          | public    | A GUI pop up that allows teachers to edit students&#39; posts.                                         |
| teacherOption2           | void                      | None                                          | public    | Most of teachers&#39; GUI pop up menus.                                                                |
| teacherMenu1             | void                      | None                                          | public    | The initial menu of teacher.                                                                           |
| invalidInputMessage      | void                      | None                                          | public    | A GUI pop up for all invalid inputs.                                                                   |

## Post.java

A class that constructs a &quot;Post&quot; object, which will be the post made by the student, under each discussion topic.

### Fields

| Name            | Type               | Modifiers | Description                                        |
|-----------------|--------------------|-----------|----------------------------------------------------|
| courseName      | String             | private   | The course name of the discussion is under.        |
| discussionTopic | DiscussionTopic    | private   | The discussion topic the student is posting under. |
| post            | String             | private   | The content of the post.                           |
| reply           | ArrayList< Reply > | private   | An ArrayList of the replies to the post.           |
| student         | Student            | private   | The student who made the post.                     |
| timeStamp       | String             | private   | The time the student made the post.                |
| grade           | double             | private   | The grade that the teacher assigns to the post.    |

### Constructor

| Parameters                                                                                                 | Modifier | Description                                                 |
|------------------------------------------------------------------------------------------------------------|----------|-------------------------------------------------------------|
| String courseName, DiscussionTopic discussionTopic, String post, ArrayList< Reply > reply, Student student | public   | Initialize the class fields to their associated parameters. |

Initialize the grade to -1, to indicate the post has not been graded.
Document the time stamp of the time and calling the toString() method to make the class field timeStamp to be the string of current time stamp. |

### Methods

| Name               | Return type        | Parameters                      | Modifiers | Description                                                                      |
|--------------------|--------------------|---------------------------------|-----------|----------------------------------------------------------------------------------|
| getGrade           | double             | None                            | public    | Returns the grade.                                                               |
| setGrade           | void               | double grade                    | public    | Sets the grade.                                                                  |
| getCourseName      | String             | None                            | public    | Returns the course name.                                                         |
| setCourseName      | void               | String courseName               | public    | Sets the course name.                                                            |
| getDiscussionTopic | DiscussionTopic    | None                            | public    | Returns the discussion topic.                                                    |
| setDiscussionTopic | void               | DiscussionTopic discussionTopic | public    | Sets the discussion topic.                                                       |
| getPost            | String             | None                            | public    | Returns the post content.                                                        |
| setPost            | void               | String post                     | public    | Sets the post content.                                                           |
| getReply           | ArrayList< Reply > | None                            | public    | Returns the ArrayList of the reply.                                              |
| setReply           | void               | ArrayList< Reply > reply        | public    | Sets the ArrayList of the reply.                                                 |
| getStudent         | Student            | None                            | public    | Returns the student who made the post.                                           |
| setStudent         | void               | Student student                 | Public    | Sets the student who made the post.                                              |
| getTimeStamp       | String             | None                            | public    | Returns the time the post being made.                                            |
| setTimeStamp       | void               | String timeStamp                | public    | Sets the time the post is being made.                                            |
| toString           | String             | None                            | public    | Formats the post to a String, which can be sent between server and client class. |


## DiscussionTopic.java

A class that constructs a &quot;DiscussionTopic&quot; object.

### Fields

| Name      | Type   | Modifiers | Description                                       |
|-----------|--------|-----------|---------------------------------------------------|
| topic     | String | private   | The content of the topic.                         |
| timeStamp | String | private   | Record the time the discusstion topic being made. |

### Constructor

| Parameters                                                                                                                                      | Modifier | Description                                                |
|-------------------------------------------------------------------------------------------------------------------------------------------------|----------|------------------------------------------------------------|
| String topic                                                                                                                                    | public   | Initialize the class field to their associated parameters. |
| Document the time stamp of the time and calling the toString() method to make the class field timeStamp to be the string of current time stamp. |          |                                                            |

### Methods

| Name         | Return type | Parameters       | Modifiers | Description                                          |
|--------------|-------------|------------------|-----------|------------------------------------------------------|
| getTopic     | String      | None             | public    | Returns the content of the topic.                    |
| setTopic     | void        | String topic     | public    | Sets the content of the topic.                       |
| getTimeStamp | String      | None             | public    | Returns the time the discussion topic is being made. |
| setTimeStamp | void        | String timeStamp | public    | Sets the time the discussion topic is being made.    |

## Reply.java

A class that constructs a &quot;Reply&quot; object.

### Fields

| Name      | Type   | Modifiers | Description                                       |
|-----------|--------|-----------|---------------------------------------------------|
| response  | String | private   | Record the content of the response.               |
| person    | Person | private   | Record the person who made the reply.             |
| timeStamp | String | private   | Record the time the discusstion topic being made. |

### Constructor

| Parameters                     | Modifier | Description                                                |
|--------------------------------|----------|------------------------------------------------------------|
| String response, Person person | public   | Initialize the class field to their associated parameters. |

Document the time stamp of the time and calling the toString() method to make the class field timeStamp to be the string of current time stamp.
|

### Methods

| Name         | Return type | Parameters       | Modifiers | Description                                          |
|--------------|-------------|------------------|-----------|------------------------------------------------------|
| getResponse  | String      | None             | public    | Returns the content of the reply.                    |
| setResponse  | void        | String response  | public    | Sets the content of the reply.                       |
| getPerson    | Person      | None             | public    | Returns the person how made the reply.               |
| setPerson    | void        | Person person    | public    | Sets the person how made the reply.                  |
| getTimeStamp | String      | None             | public    | Returns the time the discussion topic is being made. |
| setTimeStamp | void        | String timeStamp | public    | Sets the time the discussion topic is being made.    |

## Person.java

A class that constructs &quot;Person&quot; object.

### Fields

| Name     | Type   | Modifiers | Description               |
|----------|--------|-----------|---------------------------|
| name     | String | private   | The name of the user.     |
| username | String | private   | The username of the user. |
| password | String | private   | The password of the user. |

### Constructor

| Parameters                                    | Modifier | Description                                                |
|-----------------------------------------------|----------|------------------------------------------------------------|
| String name, String username, String password | public   | Initialize the class field to their associated parameters. |

### Methods

| Name        | Return type | Parameters      | Modifiers | Description                       |
|-------------|-------------|-----------------|-----------|-----------------------------------|
| getName     | String      | String name     | public    | Returns the name of the user.     |
| setName     | void        | None            | public    | Sets the name of the user.        |
| getUsername | String      | String username | public    | Returns the username of the user. |
| setUsername | void        | None            | public    | Sets the username of the user.    |
| getPassword | String      | String password | public    | Returns the password of the user. |
| setPassword | void        | None            | public    | Sets the password of the user.    |

## Teacher.java

### The &quot;Teacher&quot; class is a child class of &quot;Person&quot; class. Fields

### Fields

| Name     | Type   | Modifiers | Description               |
|----------|--------|-----------|---------------------------|
| name     | String | private   | The name of the user.     |
| username | String | private   | The username of the user. |
| password | String | private   | The password of the user. |

### Constructor

| Parameters                                    | Modifier | Description                                                |
|-----------------------------------------------|----------|------------------------------------------------------------|
| String name, String username, String password | public   | Initialize the class field to their associated parameters. |

## Student.java

The &quot;Student&quot; class is a child class of &quot;Person&quot; class.

### Fields

| Name     | Type   | Modifiers | Description               |
|----------|--------|-----------|---------------------------|
| name     | String | private   | The name of the user.     |
| username | String | private   | The username of the user. |
| password | String | private   | The password of the user. |

### Constructor

| Parameters                                    | Modifier | Description                                                |
|-----------------------------------------------|----------|------------------------------------------------------------|
| String name, String username, String password | public   | Initialize the class field to their associated parameters. |

