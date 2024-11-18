package busDriver;

import java.io.IOException;
import java.util.HashMap;

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
import travelAgencyModels.Bus;
import travelAgencyModels.Seat;
import travelAgencyModels.busDriverController;

public class BusDriverUpdateBusView {
	@FXML
	private Button updateButton;
	@FXML
	private TextField brandInput;
	@FXML
	private TextField modelInput;
	@FXML
	private TextField yearInput;
	@FXML
	private TextField seatFeeInput;
	@FXML
	private TextField plateNoInput;
	@FXML
	private TextField busIdInput;
	
	Parent root;
	busDriverController bController;
	
	public BusDriverUpdateBusView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriver/busDriverUpdateBus.fxml"));
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
		bController = new busDriverController();
	}
	
	public Parent getRoot() {
		return root;
	}
	//assigning buttons and listeners
	public void listenersAssignment() {
		brandInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		modelInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		yearInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		seatFeeInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		plateNoInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
	
	public void eventHandlersAssignment() {
	    EventHandler<ActionEvent> updateButtonHandler = (event) -> {
	        // Create a single alert instance to avoid repeated showAndWait() calls
	        Alert alertInvalidInput = new Alert(AlertType.ERROR);
	        alertInvalidInput.setTitle("Invalid Input");

	        if(!yearInput.getText().trim().isEmpty()) {
				if(!isNumeric(yearInput.getText())) {
					alertInvalidInput.setContentText("Please enter numeric value for year"); 
					alertInvalidInput.showAndWait(); 
					return;
				}
				//check if year of manufacture is valid
				int year = Integer.parseInt(yearInput.getText());
	
				// Get the current year
				int currentYear = java.time.Year.now().getValue();
	
				if (year < 1900 || year > currentYear) {
					alertInvalidInput.setContentText("Please enter valid year(after 1900)"); 
					alertInvalidInput.showAndWait();
					return;
				}
			}
	        if (!seatFeeInput.getText().trim().isEmpty() && !isNumeric(seatFeeInput.getText())) { 
	            alertInvalidInput.setContentText("Please enter a numeric value for seat fee"); 
	            alertInvalidInput.showAndWait(); 
	            return;
	        }
	        
	        int busID = Integer.parseInt(busIdInput.getText());
	        ReturnObjectUtility<Bus> busData=bController.retrieveBusObject(busID);
	        
	        if(!busData.isSuccess()) {
				Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(busData.getMessage());
			    alert.showAndWait();
			    return;
			}
	        
	        Bus bus=updateBusObject(busData.getObject());
	        
	        ReturnObjectUtility<Boolean> returnData=bController.updateBus(bus);
	        
	        // Show success or failure message
	        boolean success = returnData.isSuccess();
	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
	        alert.setHeaderText(null);
	        alert.setContentText(returnData.getMessage());
	        alert.showAndWait();
	    };
	    updateButton.setOnAction(updateButtonHandler);
	}

	
	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}

	//bus related methods
	public Bus updateBusObject(Bus bus) {
	    if (!brandInput.getText().trim().isEmpty()) {
	        bus.setBrand(brandInput.getText().trim());
	    }
	    if (!modelInput.getText().trim().isEmpty()) {
	        bus.setModel(modelInput.getText().trim());
	    }
	    if (!plateNoInput.getText().trim().isEmpty()) {
	        bus.setPlateNumber(plateNoInput.getText().trim());
	    }
	    if (!yearInput.getText().trim().isEmpty()) {
	        bus.setYear(Integer.parseInt(yearInput.getText().trim()));
	    }
	    if (!seatFeeInput.getText().trim().isEmpty()) {
	        bus.setPriceOfSeats(Float.parseFloat(seatFeeInput.getText().trim()));
	    }
	    return bus;
	}
	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean busIdFilled = !busIdInput.getText().trim().isEmpty(); // Check if Bus ID is filled

	    // Check if at least one of the other fields is filled
	    boolean atLeastOneOtherFieldFilled = 
	        !brandInput.getText().trim().isEmpty() ||
	        !modelInput.getText().trim().isEmpty() ||
	        !yearInput.getText().trim().isEmpty() ||
	        !seatFeeInput.getText().trim().isEmpty() ||
	        !plateNoInput.getText().trim().isEmpty();

	    // Enable the button if Bus ID is filled and at least one other field is filled
	    updateButton.setDisable(!(busIdFilled && atLeastOneOtherFieldFilled));
	}

}
