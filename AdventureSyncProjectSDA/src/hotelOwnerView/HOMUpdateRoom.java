package hotelOwnerView;

import java.io.IOException;

import controllers.hotelOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelModels.Hotel;
import hotelModels.Room;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HOMUpdateRoom {
	@FXML
	private Pane sidePanel;
	@FXML
	private Pane mainPanel;
	@FXML
	private Text name;
	@FXML
	private Text id;
	@FXML
	private Text cnic;
	@FXML
	private Text dob;
	@FXML
	private Button viewRoomButton;
	@FXML
	private Button backButton;
	@FXML
	private TextField roomIDInput;
	@FXML
	private TextField descInput;
	@FXML
	private TextField pricePerNightInput;
	@FXML
	private Button updRoomButton;	
	
	Parent root;
	hotelOwnerController hoController;
	private int hotelOwnerID;
	private int hotelID;
	public HOMUpdateRoom(Integer hID) {
		hotelOwnerID = hID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwnerView/HOMUpdateRoom.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void assignHotelID(){
		hotelID=hoController.getHotelID(hotelOwnerID).getObject();
	}
	@FXML
	private void initialize() {
		listenersAssignment();
		hoController = new hotelOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoController.getHotelOwnerProfileDetail(hotelOwnerID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }

   	//assigning buttons and listeners
   	public void listenersAssignment() {
   		descInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   		pricePerNightInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   		roomIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   	}
   	
   	public void eventHandlersAssignment() {
   	    EventHandler<ActionEvent> updateButtonHandler = (event) -> {
   	    	Alert alertInvalidInput = new Alert(AlertType.ERROR);
	        alertInvalidInput.setTitle("Invalid Input");

	        if(!roomIDInput.getText().trim().isEmpty()) {
				if(!isNumeric(roomIDInput.getText())) {
					alertInvalidInput.setContentText("Please enter numeric value for room id"); 
					alertInvalidInput.showAndWait(); 
					return;
				}
	        }
	        
	        if(!pricePerNightInput.getText().trim().isEmpty()) {
				if(!isNumeric(pricePerNightInput.getText())) {
					alertInvalidInput.setContentText("Please enter numeric value for room cost"); 
					alertInvalidInput.showAndWait(); 
					return;
				}
				//check if year of manufacture is valid
				float price= Float.parseFloat(pricePerNightInput.getText());
	
				if (price<=0.0f) {
					alertInvalidInput.setContentText("Room cost should be greater than zero"); 
					alertInvalidInput.showAndWait();
					return;
				}
			}
	        
   	        int roomID = Integer.parseInt(roomIDInput.getText());
   	        ReturnObjectUtility<Room> roomData=hoController.retrieveRoomObject(roomID);
   	        
   	        if(!roomData.isSuccess()) {
   				Alert alert = new Alert(AlertType.ERROR);
   			    alert.setTitle("Operation Failed");
   			    alert.setHeaderText(null);
   			    alert.setContentText(roomData.getMessage());
   			    alert.showAndWait();
   			    return;
   			}
   	        
   	        Room room=updateRoomObject(roomData.getObject());
   	        
   	        ReturnObjectUtility<Boolean> returnData=hoController.updateRoom(room);
   	        
   	        // Show success or failure message
   	        boolean success = returnData.isSuccess();
   	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
   	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
   	        alert.setHeaderText(null);
   	        alert.setContentText(returnData.getMessage());
   	        alert.showAndWait();
   	    };
   	    updRoomButton.setOnAction(updateButtonHandler);
	    backButton.setOnMouseClicked(createButtonHandler(HOMManageRoom.class, "Manage Room", hotelOwnerID));
	    viewRoomButton.setOnMouseClicked(createButtonHandler(HOMViewRoom.class, "View Room", hotelOwnerID));
	}
   	private <T> EventHandler<MouseEvent> createButtonHandler(Class<T> viewObject, String stageTitle, Object... params) {
	    return event -> {
	        try {
	            T controllerInstance;

	            // Check if the class has a constructor that matches the params
	            if (params != null && params.length > 0) {
	                Class<?>[] paramTypes = new Class<?>[params.length];
	                for (int i = 0; i < params.length; i++) {
	                    paramTypes[i] = params[i].getClass(); // Get parameter types
	                }

	                // Create an instance using the constructor with parameters
	                controllerInstance = viewObject.getDeclaredConstructor(paramTypes).newInstance(params);
	            } else {
	                // Default constructor
	                controllerInstance = viewObject.getDeclaredConstructor().newInstance();
	            }

	            // Assuming the controller class has a getRoot() method
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
    
	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}

	//bus related methods
	public Room updateRoomObject(Room room) {
	    if (!descInput.getText().trim().isEmpty()) {
	    	room.setDescription(descInput.getText().trim());
	    }
	    if (!pricePerNightInput.getText().trim().isEmpty()) {
	    	room.setPricePerNight(Float.parseFloat(pricePerNightInput.getText().trim()));
	    }
	    return room;
	}
	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean roomIDFilled = !roomIDInput.getText().trim().isEmpty();

	    // Check if at least one of the other fields is filled
	    boolean atLeastOneOtherFieldFilled = 
	        !pricePerNightInput.getText().trim().isEmpty() ||
	        !descInput.getText().trim().isEmpty();

	    // Enable the button if Bus ID is filled and at least one other field is filled
	    updRoomButton.setDisable(!(roomIDFilled && atLeastOneOtherFieldFilled));
	}
}
