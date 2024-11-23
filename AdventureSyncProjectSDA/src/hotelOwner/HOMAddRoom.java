package hotelOwner;

import java.io.IOException;

import dbHandlers.ReturnObjectUtility;
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
import travelAgencyModels.Car;

public class HOMAddRoom {
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
	private Button backButton;
	@FXML
	private Button addRoomButton;
	@FXML
	private TextField descInput;
	@FXML
	private TextField pricerPerNightInput;
	
	private int hotelID;
	private int hotelOwnerID;
	
	Parent root;
	hotelOwnerController hoContoller;
	public HOMAddRoom(Integer id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/HOMAddRoom.fxml"));
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
		displayOwnerDetails();
		eventHandlersAssignment();
		assignHotelID();
	}
	 public void assignHotelID(){
			hotelID=hoContoller.getHotelID(hotelOwnerID).getObject();
	}
	public Parent getRoot() {
		return root;
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoContoller.getHotelOwnerProfileDetail(hotelOwnerID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
    	
    	EventHandler<ActionEvent> addButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid Input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			//check if inputs are numeric
			if(!isNumeric(pricerPerNightInput.getText())) {
				errorMessage.append("Please enter numeric value for price per night.\n");
			}
	        if (errorMessage.length() > 0) {
	            alertInvalidInput.setContentText(errorMessage.toString());
	            alertInvalidInput.showAndWait();
	            return;
	        }
			Room room=createRoomObject();
			ReturnObjectUtility<Boolean> returnData=hoContoller.addRoom(room);
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
		};
		addRoomButton.setOnAction(addButtonHandler);
	    // Using a custom handler factory method
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
   
	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}

	public Room createRoomObject(){
	    // Retrieve inputs
	    String description = descInput.getText();
	    float pricePerNight = Float.parseFloat(pricerPerNightInput.getText());

	    // Create and return the Room object
	    return new Room(0, description, pricePerNight, false, hotelID);
	}


	//assigning buttons and listeners
	public void listenersAssignment() {
		pricerPerNightInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		descInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !pricerPerNightInput.getText().trim().isEmpty() &&
	        !descInput.getText().trim().isEmpty();

	    addRoomButton.setDisable(!allFieldsFilled);
	}
	
}
