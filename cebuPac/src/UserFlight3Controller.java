import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;

public class UserFlight3Controller implements Initializable {

    @FXML
    private AnchorPane anchorpane; 

    @FXML
    private ComboBox<String> payment;

    @FXML
    private ComboBox<String> seatclass;

    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate payment options
        payment.getItems().addAll("PM1 - American Express", "PM2 - GCash", "PM3 - Maya", "PM4 - PayPal", "PM5 - Visa MasterCard", "PM6 - JCB");

        // Populate seat class options
        seatclass.getItems().addAll("EXX - Economy Class", "BXX - Business Class", "FXX -First Class");
    }


    @FXML
    private void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight2.fxml"));
            AnchorPane view = loader.load();
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
