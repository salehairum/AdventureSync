package hotelOwner;

import java.io.IOException;

import dbHandlers.ReturnObjectUtility;
import hotelModels.Hotel;
import hotelModels.Room;
import hotelModels.hotelOwnerController;
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
	private Button menuButton;
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
	public HOMUpdateRoom() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/HOMUpdateRoom.fxml"));
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
		hoController = new hotelOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoController.getHotelOwnerProfileDetail(1);
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
	    menuButton.setOnMouseClicked(createButtonHandler(HotelOwnerMenuView.class, "Menu"));
	    backButton.setOnMouseClicked(createButtonHandler(HOMManageRoom.class, "Manage Room"));
	    viewRoomButton.setOnMouseClicked(createButtonHandler(HOMViewRoom.class, "View Room"));
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
