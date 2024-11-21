package tourist;

import java.io.IOException;

import dbHandlers.ReturnObjectUtility;
import hotelModels.Hotel;
import hotelModels.hotelOwnerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import travelAgencyModels.TouristController;

public class TouristOrdersFoodView {
	@FXML
	private TextField hotelIDInput;
	@FXML
	private Button selectHotelButton;
	
	private int touristID;
	
	Parent root;
	TouristController tController;
	hotelOwnerController hController;
	
	public TouristOrdersFoodView(int id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristOrdersFood.fxml"));
		loader.setController(this);
		touristID=id;
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
		tController = new TouristController();
		hController = new hotelOwnerController();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		hotelIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	//afsah tumne yahan doosre form par lekar jana at the end-->this is just like book seat
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> selectBusButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(hotelIDInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for hotel ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int hotelID=Integer.parseInt(hotelIDInput.getText());
						
			ReturnObjectUtility<Hotel> returnData=tController.retrieveHotelObject(hotelID);
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
		    alert.setHeaderText(null);
		    alert.setContentText(returnData.getMessage());
		    alert.showAndWait();
		    
		    if(success) {
		    	//go to next form
		    	//just send poora hotel since ab woh rooms aa chuke hain uskay 
		    	//pass this room
		    	Hotel hotel=returnData.getObject();
		    }
		};
			
		selectHotelButton.setOnAction(selectBusButtonHandler);
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
	        !hotelIDInput.getText().trim().isEmpty();

	    selectHotelButton.setDisable(!allFieldsFilled);
	}
}
