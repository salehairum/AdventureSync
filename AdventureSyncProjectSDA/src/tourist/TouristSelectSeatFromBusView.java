package tourist;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.Seat;
import travelAgencyModels.TouristController;
import travelAgencyModels.busDriverController;
import travelAgencyModels.travelAgencyOwnerController;

public class TouristSelectSeatFromBusView {
	@FXML
	private TextField seatIdInput;
	@FXML
	private Button bookSeatButton;
	
	private int touristID;
	
	Parent root;
	TouristController tController;
	travelAgencyOwnerController toaController;
	busDriverController bController;
	Bus bus;
	
	public TouristSelectSeatFromBusView(int id, Bus newBus) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristSelectSeatFromBus.fxml"));
		loader.setController(this);
		touristID=id;
		bus=newBus;
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
		bController = new busDriverController();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		seatIdInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> bookSeatButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(seatIdInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for seat ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int seatID=Integer.parseInt(seatIdInput.getText());
			int busID=bus.getID();
			//seats have already been retrieved in bus, so check there
			//ReturnObjectUtility<Seat> returnData= bController.retrieveSeatObject(seatID, busID);

			//if seat exists, then it must be booked
			ReturnObjectUtility<Seat> returnData= bController.updateSeatBookingStatus(seatID, true);
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
				ReturnObjectUtility<Seat> returnData2=tController.addSeatToBookedSeats(touristID, seatID);
				success=returnData2.isSuccess();
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
				    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
				    alert.setHeaderText(null);
				    alert.setContentText(returnData2.getMessage());
				    alert.showAndWait();
				    
				if(!success)
					bController.updateSeatBookingStatus(seatID, false);
			}
		};
			
		bookSeatButton.setOnAction(bookSeatButtonHandler);
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
	        !seatIdInput.getText().trim().isEmpty();

	    bookSeatButton.setDisable(!allFieldsFilled);
	}
}
