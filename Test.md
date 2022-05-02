## Test 1: Connecting to Server

Steps:

1. A GUI will pop up and ask for the host name.

2. Enter &quot;localhost&quot; if the server is on the same computer running the client; enter the IP address of the computer running the server if the client is running on a different computer with the server.

3. A GUI will pop up and ask for the port name.

4. Enter &quot;2111&quot;.

Expected result:

GUI will pop up &quot;Connection has been established!&quot;, and the client can communicate with the server.

GUI pop up &quot;Invalid host name&quot; or &quot;Invalid port number&quot;. There was an error when entering the host name or port number, enter it again.

GUI pop up &quot;Failure to establish connection&quot;. There was an error connecting to the server, rerun the program.

Test Status: Passed.

## Test 2: Create and Account in the System

Steps:

1. After connecting to the server A GUI will pop up with 3 options: create an account, sign in, or exit.

2. Click the Create an account button.

3. Enter your name, if you hit cancel or try to exit the program you will be asked to try again and reenter your name. Multiple users can have the same names.

4. Enter a username, if you hit cancel or try to exit the program you will be asked to try again and reenter your username. If the username has already been taken a GUI will pop up and ask you to enter a different username.

5. Enter a password, if you hit cancel or try to exit the program you will be asked to try again and reenter your password.

6. Choose either the student or teacher role depending on which type of user you would like to use the program as.

Expected result:

If the account is created as a teacher a GUI will popup allowing you to create courses, select courses, grade a student, edit or delete your account, and exit the program.

If the account is created as a student a GUI will popup allowing you to select a course, view your grades, edit or delete your account, or exit the program.

Test Status: Passed.

## Test 3: Sign into the System

Steps:

1. After connecting to the server GUI will pop up with 3 options: create an account, sign in, or exit.

2. Click the sign in button.

3. Enter your account username and select ok.

4. Enter your account password and select ok.

5. If your username or password is incorrect, a GUI will pop up and ask you to reenter your username and password

Expected result:

If you have entered a valid username and password, a GUI will popup stating &quot;You have been logged in!&quot;

Test Status: Passed.

## Test 4: Delete Account

Steps:

1. Once signed in, select the edit or delete account button.

2. A GUI will popup, to delete your account select the delete account button

Expected result:

A GUI will pop up stating &quot;Your account has been deleted&quot; and you will be taken back to the original GUI where you can login to a different account or create a new one.

Test Status: Passed.

## Test 5: Modify Account

Steps:

1. Once signed in, select the edit or delete account button.

2. A GUI will popup, to delete your account select the Edit Account button

3. Choose to modify Password or User name

4. Enter the new Password or User name accordingly

Expected result:

If the account name is valid, a GUI pop-up will show the account being edited, and the system will take the user back to the login page, and the user will use the new account information to login. All the post made by the user will also have the new name if the username is modified.

Test Status: Passed.

## Test 6: Make a New Course (Teacher)

Steps:

1. From the main teacher GUI, select the Create a course button.
2. A GUI will pop up and you will be prompted to enter the name of the course.

Expected result:

If the course does not yet exist, a GUI will pop up and state &quot;Course has been created!&quot;. If a course already exists with the course name, a GUI will pop up and state, &quot;Course already exists&quot;. Once one of these GUI&#39;s pops up you will be taken back to the main teacher GUI.

Test Status: Passed.

## Test 7: Manually Create a New Discussion Forum (Teacher)

Steps:

1. From the main teacher GUI, select the Select a course button.
2. A GUI will popup, use the dropdown menu to select a course you wish to modify or view data for and press ok.
3. A GUI will popup, select the Create a Discussion Forum button.
4. Another GUI will pop up, select the option to manually create a discussion forum.
5. A GUI will pop up and you will be asked to enter the topic for your discussion forum.
6. A final GUI will popup, this time asking you to enter the prompt for your discussion forum.

Expected result:

If the discussion forum already exists, a GUI will popup stating &quot;The Discussion Forum entered already exists! Please enter different topic and prompt!&quot; and will ask the user to enter (different) topic again. 
If the discussion forum has been successfully created, a GUI will popup stating: &quot;The Discussion Forum has been created!&quot;

Test Status: Passed

## Test 8: Import a New Discussion Forum (Teacher)

Steps:

1. From the main teacher GUI, select the Select a course button.
2. A GUI will popup, use the dropdown menu to select a course you wish to modify or view data for and press ok.
3. A GUI will popup, select the Create a Discussion Forum button.
4. Another GUI will pop up, select the option to import a new discussion forum from a file.
5. a GUI will popup and you will be asked to enter the name of the file you would like to import (This should be a .txt file). 

Expected result:

If the file path entered doesn't exist or the file is empty, a GUI will pop up with the respective descriptive error and will allow the users to enter the filename again. 
If the discussion forum has been successfully imported, a GUI will popup stating: &quot;The Discussion Forum has been created!&quot;

Test Status: Passed

## Test 9: Editing a Discussion Forum (Teacher)

Steps:

1. From the main teacher GUI, select the Select a course button.
2. A GUI will popup, use the dropdown menu to select a course you wish to modify or view data for and press ok.
3. A GUI will popup, select the Edit a Discussion Forum button.
4. Another GUI will pop up, this GUI displays every discussion under the selected course. Use the dropdown menu to select which Discussion you wish to edit.
5. A GUI will prompt you to enter a new topic for your discussion.
6. A GUI will ask you to enter the new prompt for your discussion.

Expected result:

If the edited discussion topic and prompt already exists, a GUI will popup &quot;The Discussion Forum entered already exists! Please enter a different topic and prompt!&quot; and will allow the user to enter a (different) topic and prompt again. 
If the discussion forum has been successfully edited, a GUI will popup stating: &quot;The Discussion Forum has been edited!&quot;

Test Status: Passed

## Test 10: Deleting a Discussion Forum (Teacher)

Steps:

1. From the main teacher GUI, select the Select a course button.
2. A GUI will popup, use the dropdown menu to select a course you wish to modify or view data for and press ok.
3. A GUI will popup, select the Delete a Discussion Forum button.
4. Another GUI will pop up, this GUI displays every discussion under the selected course. Use the dropdown menu to select which Discussion you wish to delete.

Expected result:

If the discussion forum has been successfully deleted, a GUI will popup stating: &quot;The Discussion Forum has been deleted!&quot;

Test Status: Passed


## Test 11: Grade Student&#39;s Response (Teacher)

Steps:

1. In the main menu, the teacher can find a button to grade the student
2. The GUI will provide a drop down menu for the teacher to select the student they want to grade
3. All of the post the student made will be displayed, the teacher will enter the grade for each post from #1 to the last post the student made
4. The teacher is allowed to enter a number from 0.0 to 10.0

Expected result:

If the teacher entered valid grade number for each post of the student selected, the GUI will show the student selected has been graded.

Test Status: Passed.

## Test 12: Make a Response Under a Discussion Forum (Student)

Steps:

1. After choosing Select a course under the main menu, a GUI will Guide the student to select the course via a drop-down menu
2. After selecting the course, the student will be allowed to choose exit the course or select a discussion forum
3. Pressing the discussion forum button, the GUI will show the details of each discussion forum, the student can choose the discussion forum through a drop-down menu
4. Then the GUI will show all the post under the forum, a drop-down menu will allow students to choose a post
5. The student is then allowed to create a post under the forum

Expected result:

If the post already exists, a GUI will popup stating that it alreading exists and will ask the users for a (different) input again. 
After entering the post through the GUI, the GUI will show that the post has been added. All other teachers and students will see the post after they entered into this discussion forum.

Test Status: Passed.

Two or More Users Make Response Under the Same Discussion Forum

Steps:

1. Two or more users connecting to the LMSServer at the same time
2. Users randomly repeat the process in Make a Response Under a Discussion Forum

Expected result:

New posts will shown under each discussion forum, and they will be ordered from the newest to oldest.

Test Status: Passed

## Test 13: View the Score (Student)

Steps:

1. In the main menu, there will be a button called View My Grades
2. Click the button

Expected result:

If the student haven&#39;t made any post, a warning message will be displayed.

If the student had made posts, there will be a GUI displaying all the posts made by the student, associated with the score given by the teacher. If the teacher haven&#39;t grade the post, the post will display a line indicating the post is not yet graded under the post. 

Test Status: Passed.

## Test 14: Make a Comment to a Response

Steps:

1. After choosing Select a course under the main menu, a GUI will GUIde the student to select the course via a drop-down menu
2. After selecting the course, the student will be allowed to choose exit the course or select a discussion forum
3. Pressing the discussion forum button, the GUI will show the details of each discussion forum, the student can choose the discussion forum through a drop-down menu
4. Then the GUI will show all the post under the forum, a drop-down menu will allow students to choose the post to be replied
5. The student is then allowed to create a post under the forum

Expected result:

After entering the post through the GUI, the GUI will show that the comment has been added. All other teachers and students will see the comment under the post being replied, after they entered into this discussion forum.

Test Status: Passed.


## Test 15: Concurrency Test

Description:

One team member runs the server; the other three team members connect to the server through the IP address. Then the three team members create several student and teachers accounts, make several courses, discussion forums, response under discussion forums, and teacher also grades students&#39; responses. All these will be done randomly and simultaneously.

Expected Results:

All functions should work properly, no matter how many users are accessing the server.

Test Status: Passed.
