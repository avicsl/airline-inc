import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserHomeController {

    @FXML
    private Label homelabel; // Ensure this ID matches the FXML file

    public void setUsername(String username) {
        if (homelabel != null) {
            homelabel.setText("Welcome, " + username + "!");
        }
    }
}
