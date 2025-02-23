import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class UserFlight2Controller {

    @FXML
    private AnchorPane anchorpane;

    @FXML
    private TextField deploc, arrivalloc, arrivaltime, IDTextField;

    @FXML
    private ComboBox<String> deptime, adultcombobox, childrencombobox, infantcombobox;

    @FXML
    private DatePicker depdate, returndate;

    @FXML
    private CheckBox yesbox, nobox;

    private final Map<String, Integer> flightDurations = new HashMap<>();

    @FXML
    private void initialize() {

        String userId = DatabaseHandler.getLoggedInUserId();
        if (userId != null) {
            IDTextField.setText(userId);
        // Restrict departure date selection
        depdate.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                LocalDate maxDate = today.plusMonths(5).withDayOfMonth(today.plusMonths(5).lengthOfMonth());
                if (date.isBefore(today) || date.isAfter(maxDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #cccccc;");
                }
            }
        
        });

        // Populate flight durations (in minutes)
        flightDurations.put("Cebu - CEB", 90);
        flightDurations.put("Puerto Princesa - PPS", 80);
        flightDurations.put("Boracay - BCY", 60);
        flightDurations.put("Bohol - BHL", 90);
        flightDurations.put("Davao - DVO", 120);
        flightDurations.put("Batanes - BAT", 105);
        flightDurations.put("Singapore - SIN", 225);
        flightDurations.put("Tokyo - NRT", 270);
        flightDurations.put("Tokyo - HND", 300);
        flightDurations.put("Seoul - ICN", 240);
        flightDurations.put("Dubai - DXB", 570);
        flightDurations.put("Sydney - SYD", 510);
        flightDurations.put("Zurich - ZRH", 840);

        deptime.getItems().addAll("7:00 AM", "1:00 PM", "8:00 PM");

        for (int i = 1; i <= 5; i++) {
            adultcombobox.getItems().add(String.valueOf(i));
        }
        for (int i = 0; i <= 3; i++) {
            childrencombobox.getItems().add(String.valueOf(i));
        }
        for (int i = 0; i <= 2; i++) {
            infantcombobox.getItems().add(String.valueOf(i));
        }

        deptime.setOnAction(event -> updateArrivalTime());

        // Add listeners for checkboxes
        yesbox.setOnAction(event -> handleReturnDate());
        nobox.setOnAction(event -> handleReturnDate());

        // Disable return date initially
        returndate.setDisable(true);

        // Listener for depdate to adjust returndate when departure changes
        depdate.setOnAction(event -> {
            if (yesbox.isSelected()) {
                updateReturnDateRestrictions();
            }
        });
    }
}

    public void setFlightLocations(String departure, String arrival) {
        deploc.setText(departure);
        arrivalloc.setText(arrival);
        deploc.setEditable(false);
        arrivalloc.setEditable(false);
        arrivaltime.setEditable(false);
        depdate.setEditable(false);
        returndate.setEditable(false);
        IDTextField.setEditable(false);

        Platform.runLater(this::updateArrivalTime);
    }

    private void updateArrivalTime() {
        String selectedTime = deptime.getValue();
        String destination = arrivalloc.getText();

        if (selectedTime != null && flightDurations.containsKey(destination)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            LocalTime departureTime = LocalTime.parse(selectedTime, formatter);

            LocalTime arrivalTime = departureTime.plusMinutes(flightDurations.get(destination));

            arrivaltime.setText(arrivalTime.format(formatter));
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight.fxml"));
            AnchorPane view = loader.load();
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void payment(ActionEvent event) {
        if (!isFormValid()) {
            showAlert("Incomplete Form", "Please complete all required fields before proceeding.");
            return; // Prevent navigation if validation fails
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userflight3.fxml"));
            AnchorPane view = loader.load();
            BorderPane parentBorderPane = (BorderPane) anchorpane.getScene().lookup("#borderpane");
            parentBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleReturnDate() {
        if (nobox.isSelected()) {
            yesbox.setSelected(false);
            returndate.setDisable(true);
            returndate.setValue(null);
        } else if (yesbox.isSelected()) {
            nobox.setSelected(false);
            returndate.setDisable(false);
            updateReturnDateRestrictions();
        }
    }

    private void updateReturnDateRestrictions() {
        if (depdate.getValue() != null) {
            LocalDate dep = depdate.getValue();
            LocalDate maxReturnDate = dep.plusMonths(6);

            returndate.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (date.isBefore(dep) || date.isAfter(maxReturnDate)) {
                        setDisable(true);
                        setStyle("-fx-background-color: #cccccc;");
                    }
                }
            });
        }
    }

    private boolean isFormValid() {
        if (deploc.getText().trim().isEmpty() || 
            arrivalloc.getText().trim().isEmpty() || 
            arrivaltime.getText().trim().isEmpty() || 
            IDTextField.getText().trim().isEmpty() ||
            deptime.getValue() == null ||
            adultcombobox.getValue() == null ||
            childrencombobox.getValue() == null ||
            infantcombobox.getValue() == null ||
            depdate.getValue() == null ||
            (!yesbox.isSelected() && !nobox.isSelected()) || 
            (yesbox.isSelected() && returndate.getValue() == null)) {
            
            return false; // If any field is missing, return false
        }

        return true; // All fields are filled
    }
    

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
