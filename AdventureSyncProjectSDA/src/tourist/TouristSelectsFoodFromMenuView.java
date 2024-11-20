package tourist;

import java.io.IOException;

import dbHandlers.ReturnObjectUtility;
import hotelModels.FoodItem;
import hotelModels.Hotel;
import hotelModels.Room;
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

public class TouristSelectsFoodFromMenuView {
	@FXML
	private TextField foodIDInput;
	@FXML
	private TextField foodQuanInput;
	@FXML
	private Button orderButton;
	
	private int touristID;
	
	Parent root;
	TouristController tController;
	hotelOwnerController hController;
	Hotel hotel;
	
	public TouristSelectsFoodFromMenuView(int id, Hotel newHotel) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristSelectsFoodFromMenu.fxml"));
		loader.setController(this);
		touristID=id;
		hotel=newHotel;
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
		foodIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		foodQuanInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> orderButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(foodIDInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for food ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			if(!isNumeric(foodQuanInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for food quantity"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int foodID=Integer.parseInt(foodIDInput.getText());					
			int foodQuantity=Integer.parseInt(foodQuanInput.getText());					

			ReturnObjectUtility<FoodItem> returnData= hController.updateFoodQuantity(foodID, foodQuantity, false);
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
		    alert.setHeaderText(null);
		    alert.setContentText(returnData.getMessage());
		    alert.showAndWait();
		};
			
		orderButton.setOnAction(orderButtonHandler);
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
	        !foodIDInput.getText().trim().isEmpty() &&
	        !foodQuanInput.getText().trim().isEmpty();

	   orderButton.setDisable(!allFieldsFilled);
	}
	
}
