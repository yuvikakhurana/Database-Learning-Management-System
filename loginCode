import javax.swing.*;

public class loginSystem {

    public static void main(String[] args) {
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JPasswordField();
        int loginClosed;

        Object[] login = {
                "Username:", usernameField, "Password:", passwordField
        };

        //below loops until a username and password is entered
        //todo, needs implemented with existing code
        do {
            loginClosed = JOptionPane.showConfirmDialog(null, login, "Login", JOptionPane.OK_CANCEL_OPTION);
            String username = usernameField.getText();
            String password = usernameField.getText();

            //todo if a valid username/password is entered below will be output, this needs implemented into existing code
            JOptionPane.showMessageDialog(null, usernameField.getText() + " Successfully logged in", "Discussion Page Login", JOptionPane.INFORMATION_MESSAGE,null);

            if (loginClosed == -1 || loginClosed == -2){
                    //todo <-- In this case the user closes the window and should go back to the main screen
                } else if (loginClosed == 2) {
                    //todo <-- In this case the user clicks cancel and should go back to the main screen
                }
        } while (loginClosed == -1 || loginClosed == -2 || loginClosed ==2);
    }


}
