package hotelOwnerView;

import java.io.IOException;

import controllers.hotelOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelModels.Hotel;
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
import travelAgencyModels.Bus;

public class HOMUpdateHotel {
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
	private TextField nameInput;
	@FXML
	private TextField locationInput;
	@FXML
	private Button updHotelButton;
	@FXML
	private TextField hotelIDInput;
	
	private int hotelOwnerID;
	Parent root;
	hotelOwnerController hoController;
	public HOMUpdateHotel(int id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwnerView/HOMUpdateHotel.fxml"));
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
		hoController = new hotelOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoController.getHotelOwnerProfileDetail(1);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
   	public Parent getRoot() {
   		return root;
   	}
   	//assigning buttons and listeners
   	public void listenersAssignment() {
   		nameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   		locationInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   	}
   	
   	public void eventHandlersAssignment() {
   	    EventHandler<ActionEvent> updateButtonHandler = (event) -> {
   	    	Alert alertInvalidInput = new Alert(AlertType.ERROR);
	        alertInvalidInput.setTitle("Invalid Input");

	        if(!hotelIDInput.getText().trim().isEmpty()) {
				if(!isNumeric(hotelIDInput.getText())) {
					alertInvalidInput.setContentText("Please enter numeric value for hotel id"); 
					alertInvalidInput.showAndWait(); 
					return;
				}
	        }
				
   	        int hotelID = Integer.parseInt(hotelIDInput.getText());
   	        ReturnObjectUtility<Hotel> hotelData=hoController.retrieveHotelObject(hotelID);
   	        
   	        if(!hotelData.isSuccess()) {
   				Alert alert = new Alert(AlertType.ERROR);
   			    alert.setTitle("Operation Failed");
   			    alert.setHeaderText(null);
   			    alert.setContentText(hotelData.getMessage());
   			    alert.showAndWait();
   			    return;
   			}
   	        
   	        Hotel hotel=updateHotelObject(hotelData.getObject());
   	        
   	        ReturnObjectUtility<Boolean> returnData=hoController.updateHotel(hotel);
   	        
   	        // Show success or failure message
   	        boolean success = returnData.isSuccess();
   	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
   	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
   	        alert.setHeaderText(null);
   	        alert.setContentText(returnData.getMessage());
   	        alert.showAndWait();
   	    };
   	    updHotelButton.setOnAction(updateButtonHandler);
        menuButton.setOnMouseClicked(createButtonHandler(HOMManageHotel.class, "Manage Hotel"));
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
	public Hotel updateHotelObject(Hotel hotel) {
	    if (!nameInput.getText().trim().isEmpty()) {
	        hotel.setHotelName(nameInput.getText().trim());
	    }
	    if (!locationInput.getText().trim().isEmpty()) {
	    	hotel.setLocation(locationInput.getText().trim());
	    }
	    return hotel;
	}
	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean hotelIdFilled = !hotelIDInput.getText().trim().isEmpty();

	    // Check if at least one of the other fields is filled
	    boolean atLeastOneOtherFieldFilled = 
	        !nameInput.getText().trim().isEmpty() ||
	        !locationInput.getText().trim().isEmpty();

	    // Enable the button if Bus ID is filled and at least one other field is filled
	    updHotelButton.setDisable(!(hotelIdFilled && atLeastOneOtherFieldFilled));
	}

}
