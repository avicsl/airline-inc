import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameTextfield;

    @FXML
    private PasswordField passwordPasswordfield;

    @FXML
    private Button loginButton;

    @FXML
    private Button adminbtn;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // LOGIN BUTTON HANDLER
    @FXML
    public void loginbuttonHandler(ActionEvent event) throws IOException {
        String uname = usernameTextfield.getText();
        String pword = passwordPasswordfield.getText();

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        if (dbHandler.validateLogin(uname, pword)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user.fxml"));
            root = loader.load();
        
            UserController userController = loader.getController();
            userController.setUsername(uname);  // Set username BEFORE showing UI
        
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Please check your username and password.");
            alert.showAndWait();
        }
    }           

    // ADMIN BUTTON HANDLER
    @FXML
    public void adminButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("validation.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
