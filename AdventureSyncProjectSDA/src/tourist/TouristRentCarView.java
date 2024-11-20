package tourist;

import java.io.IOException;
import java.time.LocalDate;

import accountAndPersonModels.Account;
import accountAndPersonModels.Tourist;
import dbHandlers.ReturnObjectUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import travelAgencyModels.Car;
import travelAgencyModels.TouristController;
import travelAgencyModels.travelAgencyOwnerController;

public class TouristRentCarView {
	@FXML
	private TextField carIdInput;
	@FXML
	private Button rentButton;
	
	private int touristID;
	
	Parent root;
	TouristController tController;
	travelAgencyOwnerController toaController;
	
	public TouristRentCarView(int id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristRentCars.fxml"));
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
		toaController = new travelAgencyOwnerController();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		carIdInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> rentButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(carIdInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for car ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int carID=Integer.parseInt(carIdInput.getText());
						
			ReturnObjectUtility<Car> returnData= toaController.updateCarRentalStatus(carID, true);
			boolean success=returnData.isSuccess();
			if(!success) {
				Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			}
			else {
				//mark car as rented
				ReturnObjectUtility<Tourist> returnData2=tController.addCarToRentedCars(touristID, carID);	
				success=returnData2.isSuccess();
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
				    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
				    alert.setHeaderText(null);
				    alert.setContentText(returnData2.getMessage());
				    alert.showAndWait();
				    
				if(!success)
					toaController.updateCarRentalStatus(carID, false);   
				//if transaction could not be made, set rental status as false
			}
		};
			
		rentButton.setOnAction(rentButtonHandler);
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
	        !carIdInput.getText().trim().isEmpty();

		   rentButton.setDisable(!allFieldsFilled);
	}
}
