package signupForms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import travelAgencyModels.Car;
import travelAgencyModels.Seat;
import travelAgencyModels.busDriverController;
import travelAgencyModels.travelAgencyOwnerController;

public class BusDriverAddsBus {
	@FXML
	private Button addButton;
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
	private TextField nSeatsInput;
	@FXML
	private TextField nRowsInput;
	Parent root;
	busDriverController bController;
	
	public BusDriverAddsBus() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupForms/busDriverAddsBus.fxml"));
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
		displayOwnerDetails();
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
		nSeatsInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		nRowsInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
	
	public void eventHandlersAssignment() {
	    EventHandler<ActionEvent> addButtonHandler = (event) -> {
	        // Create a single alert instance to avoid repeated showAndWait() calls
	        Alert alertInvalidInput = new Alert(AlertType.ERROR);
	        alertInvalidInput.setTitle("Invalid Input");

	        // Consolidate validation messages
	        StringBuilder errorMessage = new StringBuilder();

	        if (!isNumeric(yearInput.getText())) {
	            errorMessage.append("Please enter numeric value for year.\n");
	        }
	        if (!isNumeric(seatFeeInput.getText())) {
	            errorMessage.append("Please enter numeric value for seat fee.\n");
	        }
	        if (!isNumeric(nSeatsInput.getText())) {
	            errorMessage.append("Please enter numeric value for number of seats.\n");
	        }
	        if (!isNumeric(nRowsInput.getText())) {
	            errorMessage.append("Please enter numeric value for number of rows.\n");
	        }

	        if (errorMessage.length() > 0) {
	            alertInvalidInput.setContentText(errorMessage.toString());
	            alertInvalidInput.showAndWait();
	            return;
	        }

	        // Check if year is valid
	        int year = Integer.parseInt(yearInput.getText());
	        int currentYear = java.time.Year.now().getValue();
	        if (year < 1900 || year > currentYear) {
	            alertInvalidInput.setContentText("Please enter a valid year (after 1900).");
	            alertInvalidInput.showAndWait();
	            return;
	        }

	        // Perform the operation
	        Bus bus = createBusObject();

	        ReturnObjectUtility<Boolean> returnData = bController.addBus(bus);

	        // Show success or failure message
	        boolean success = returnData.isSuccess();
	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
	        alert.setHeaderText(null);
	        alert.setContentText(returnData.getMessage());
	        alert.showAndWait();
	    };
	    addButton.setOnAction(addButtonHandler);
	}

	
	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}

	//bus related methods
	public Bus createBusObject() {

		String brand = brandInput.getText();
	    String model = modelInput.getText();
	    String plateNumber = plateNoInput.getText();
	
	    // Parse numeric values
	    int year = Integer.parseInt(yearInput.getText());
	    int nSeats= Integer.parseInt(nSeatsInput.getText());
	    int nRows= Integer.parseInt(nRowsInput.getText());
	    float seatFee= Float.parseFloat(seatFeeInput.getText());

		HashMap<Integer, Seat> seats=createSeats(nRows, nSeats);
	    // Create the Car object
	    return new Bus(0, brand, model, year, plateNumber, nSeats,nRows, seatFee, seats); //does not have tour at start
	}
	
	public HashMap<Integer, Seat> createSeats(int nRows, int nSeats) {
		HashMap<Integer, Seat> seats=new HashMap<Integer, Seat>();
		
		int k=0;
		for(int i=0;i<nRows;i++)
		{
			for(int j=0;j<nSeats;j++) {
				Seat seat=new Seat(0, 0, i+1);
				seats.put(k, seat);
				k++;
			}
		}

		return seats;
	}
	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !brandInput.getText().trim().isEmpty() &&
	        !modelInput.getText().trim().isEmpty() &&
	        !yearInput.getText().trim().isEmpty() &&
	        !seatFeeInput.getText().trim().isEmpty() &&
	        !plateNoInput.getText().trim().isEmpty() &&
	        !nSeatsInput.getText().trim().isEmpty();

	    addButton.setDisable(!allFieldsFilled);
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
//        String profileDetail[] = bController.getTravelAgencyOwnerProfileDetail(1);
//           
//        name.setText(profileDetail[0]);
//        id.setText(profileDetail[1]);
//        cnic.setText(profileDetail[2]);
//        dob.setText(profileDetail[3]);
    }
}
