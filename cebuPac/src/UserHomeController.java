import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserHomeController implements Initializable {

    @FXML
    private TextField IDTextfield;
 
    @FXML
    private TextField fnameTextfield;
 
    @FXML
    private TextField lnameTextfield;
 
    @FXML
    private TextField emailTextfield;
 
    @FXML
    private TextField numberTextfield;
 
    @FXML
    private TextField usernameTextfield;
 
    @FXML
    private PasswordField passPasswordfield;
 
    @FXML
    private Label timelabel;
 
    @FXML
    private Label datelabel;
 
    // Static variable to store the logged-in username
    private static String loggedInUsername;
 
    // Setter method to set the logged-in username from the LoginController
    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IDTextfield.setEditable(false);
        fnameTextfield.setEditable(false);
        lnameTextfield.setEditable(false);
        emailTextfield.setEditable(false);
        numberTextfield.setEditable(false);
        usernameTextfield.setEditable(false);
        passPasswordfield.setEditable(false);
        
        startDateTimeUpdater();
 
        // Load user data based on the logged-in username
        if (loggedInUsername != null && !loggedInUsername.isEmpty()) {
            loadUserData(loggedInUsername);
        } else {
            System.out.println("No logged-in user found.");
        }
    }
 
    // Method to start Date and Time updater
    private void startDateTimeUpdater() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                String formattedTime = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                String formattedDate = now.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
 
                javafx.application.Platform.runLater(() -> {
                    timelabel.setText(formattedTime);
                    datelabel.setText(formattedDate);
                });
            }
        }, 0, 1000);
    }
 
    // Load user data using the username
    private void loadUserData(String username) {
        String query = "SELECT * FROM admin WHERE username = ?";
 
        try (Connection conn = DatabaseHandler.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
 
            // Setting the parameter for the query
            stmt.setString(1, username);
 
            // Execute the query and get the ResultSet
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Populate the TextFields with data from the ResultSet
                    IDTextfield.setText(rs.getString("id"));
                    fnameTextfield.setText(rs.getString("first_name"));
                    lnameTextfield.setText(rs.getString("last_name"));
                    emailTextfield.setText(rs.getString("email"));
                    numberTextfield.setText(rs.getString("phone_number"));
                    usernameTextfield.setText(rs.getString("username"));
                    passPasswordfield.setText(rs.getString("password"));
                } else {
                    System.out.println("No data found for user: " + username);
                }
            }
 
        } catch (SQLException e) {
            System.out.println("Error loading user data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
 