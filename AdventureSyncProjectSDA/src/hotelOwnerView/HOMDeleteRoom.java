package hotelOwnerView;

import java.io.IOException;

import controllers.hotelOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
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

public class HOMDeleteRoom {
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
	private Button delRoomButton;
	@FXML
	private TextField roomIDInput;
	
	Parent root;
	hotelOwnerController hoController;
	private int hotelOwnerID;
	private int hotelID;
	public HOMDeleteRoom(Integer id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwnerView/HOMDeleteRoom.fxml"));
		loader.setController(this);
		hotelOwnerID=id;
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
    
    // Method for button handling
    public void eventHandlersAssignment() {
    	EventHandler<ActionEvent> deleteButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid Input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			//check if inputs are numeric
			if(!isNumeric(roomIDInput.getText())) {
				errorMessage.append("Please enter numeric value for room ID.\n");
			}
			
	        if (errorMessage.length() > 0) {
	            alertInvalidInput.setContentText(errorMessage.toString());
	            alertInvalidInput.showAndWait();
	            return;
	        }
	        
	        int roomID=Integer.parseInt(roomIDInput.getText());

			ReturnObjectUtility<Boolean> returnData=hoController.deleteRoom(roomID);
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
		};
		delRoomButton.setOnAction(deleteButtonHandler);
        
        // Assign handlers with parameters for specific FXMLs and classes
        viewRoomButton.setOnMouseClicked(createButtonHandler(HOMViewRoom.class, "View Room", hotelOwnerID));
        backButton.setOnMouseClicked(createButtonHandler(HOMManageRoom.class, "Manage Room", hotelOwnerID));
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
    
    //assigning buttons and listeners
    public void listenersAssignment() {
    	roomIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
    }

    public boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
    }
    	
    //check if all inputs have been given
   	private void validateInputs() {
   	    boolean allFieldsFilled = 
   	        !roomIDInput.getText().trim().isEmpty();
   		    delRoomButton.setDisable(!allFieldsFilled);
   	}

}
