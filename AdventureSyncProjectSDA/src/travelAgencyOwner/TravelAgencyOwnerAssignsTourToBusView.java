package travelAgencyOwner;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import dbHandlers.ReturnObjectUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Car;
import travelAgencyModels.Tour;
import travelAgencyModels.travelAgencyOwnerController;

public class TravelAgencyOwnerAssignsTourToBusView {
	@FXML
	private TextField originInput;
	@FXML
	private TextField destinationInput;
	@FXML
	private DatePicker dateInput;
	@FXML
	private TextField busIDInput;
	@FXML
	private Button assignButton;
	@FXML
	private Text name;
	@FXML
	private Text id;
	@FXML
	private Text cnic;
	@FXML
	private Text dob;
	@FXML
	private Button viewBusButton;
	@FXML
	private Button backButton;
	
	Parent root;
	travelAgencyOwnerController taoController;
	public TravelAgencyOwnerAssignsTourToBusView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerAssignBusTour.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void initialize() {
		listenersAssignment();
		eventHandlersAssignment();
		taoController = new travelAgencyOwnerController();
		displayOwnerDetails();
	}
	
	public Parent getRoot() {
		return root;
	}

	//assigning buttons and listeners
	public void listenersAssignment() {
		originInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		destinationInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		busIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		dateInput.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
	
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> addButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid Input"); 
			
			//check if inputs are numeric
			if(!isNumeric(busIDInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for bus id"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			LocalDate selectedDate = dateInput.getValue(); // Get the date from DatePicker
			if(selectedDate.isBefore(LocalDate.now())) {
				alertInvalidInput.setContentText("Please enter date of future"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			Tour tour=createTourObject();
			ReturnObjectUtility<Tour> returnData=taoController.assignTour(tour);
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
		};
		assignButton.setOnAction(addButtonHandler);
        backButton.setOnMouseClicked(createButtonHandler(TravelAgencyManageBusView.class, "Manage Buses"));
        viewBusButton.setOnMouseClicked(createButtonHandler(TravelAgencyViewBusesView.class, "View Buses"));
	}
	private <T> EventHandler<MouseEvent> createButtonHandler(Class<T> viewObject, String stageTitle) {
        return event -> {
            try {
                // Dynamically create an instance of the specified class
                T controllerInstance = viewObject.getDeclaredConstructor().newInstance();

                // Assuming the controller class has a `getRoot()` method
                Parent root = (Parent) viewObject.getMethod("getRoot").invoke(controllerInstance);

                // Create a new scene and stage for the new form
                Scene newFormScene = new Scene(root);
                Stage newFormStage = new Stage();
                newFormStage.setScene(newFormScene);
                newFormStage.setTitle(stageTitle);

                // Show the new form
                newFormStage.show();

                // Close the current form
                Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                currentStage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(1);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
	
	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}

	//car related methods
	public Tour createTourObject() {
		String origin = originInput.getText();
	    String destination = destinationInput.getText();
	    
	    // Retrieve date from DatePicker (use LocalDate, convert to Date if necessary)
	    Date date = java.sql.Date.valueOf(dateInput.getValue());

	    // Retrieve the bus ID and parse it
	    int busID = Integer.parseInt(busIDInput.getText());

	    // Create and return the Tour object
	    return new Tour(0, origin, destination, date, busID);
	}
	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !originInput.getText().trim().isEmpty() &&
	        !destinationInput.getText().trim().isEmpty() &&
	        !busIDInput.getText().trim().isEmpty() &&
	        dateInput.getValue()!=null;

	    assignButton.setDisable(!allFieldsFilled);
	}
	
}
