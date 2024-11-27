package signupForms;

import java.io.IOException;

import controllers.hotelOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelModels.Hotel;
import hotelModels.Room;
import hotelOwnerView.HOMManageRoom;
import hotelOwnerView.HotelOwnerMenuView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HotelOwnerAddsHotel {
	
	@FXML
	private Button addHotelButton;
	@FXML
	private TextField nameInput;
	@FXML
	private TextField locationInput;

	Parent root;
	hotelOwnerController hoContoller;
	int hotelOwnerID;
	
	public HotelOwnerAddsHotel(Integer id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupForms/hotelOwnerAddsHotel.fxml"));
		loader.setController(this);
		hotelOwnerID=id;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	@FXML
	private void initialize() {
		listenersAssignment();
		hoContoller = new hotelOwnerController();
		eventHandlersAssignment();
	}
	
	public Parent getRoot() {
		return root;
	}
    
    // Method for button handling
    public void eventHandlersAssignment() {
    	
    	EventHandler<ActionEvent> addButtonHandler=(event)->{
    		
    		
    		Hotel hotel=createHotelObject();
			ReturnObjectUtility<Boolean> returnData=hoContoller.addHotel(hotel, hotelOwnerID);
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			    if(success)
			    {
			    	try {
			            // Dynamically create an instance of the next form's controller with the touristID
			            HotelOwnerMenuView controllerInstance = new HotelOwnerMenuView(hotelOwnerID);

			            // Load the next form's scene
			            Parent root = controllerInstance.getRoot();
			            Scene newFormScene = new Scene(root);
			            Stage newFormStage = new Stage();
			            newFormStage.setScene(newFormScene);
			            newFormStage.setTitle("Hotel Owner Menu");

			            // Show the new form
			            newFormStage.show();

			            // Close the current form
			            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			            currentStage.close();

			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }
		};
		addHotelButton.setOnAction(addButtonHandler);
    }

	public Hotel createHotelObject(){
	    // Retrieve inputs
	    String location = locationInput.getText();
	    String name = nameInput.getText();

	    // Create and return the Room object
	    return new Hotel(0, location, name);
	}


	//assigning buttons and listeners
	public void listenersAssignment() {
		locationInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		nameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !locationInput.getText().trim().isEmpty() &&
	        !nameInput.getText().trim().isEmpty();

	    addHotelButton.setDisable(!allFieldsFilled);
	}
}
