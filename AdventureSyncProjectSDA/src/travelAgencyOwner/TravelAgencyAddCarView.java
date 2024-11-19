package travelAgencyOwner;

import java.io.IOException;

import dbHandlers.ReturnObjectUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import travelAgencyModels.Car;
import travelAgencyModels.travelAgencyOwnerController;

public class TravelAgencyAddCarView {
	
	@FXML
	private Button addButton;
	@FXML
	private TextField brandInput;
	@FXML
	private TextField modelInput;
	@FXML
	private TextField yearInput;
	@FXML
	private TextField rentalFeeInput;
	@FXML
	private TextField plateNoInput;
	@FXML
	private TextField costPerKmInput;
	@FXML
	private Text name;
	@FXML
	private Text id;
	@FXML
	private Text cnic;
	@FXML
	private Text dob;
	
	Parent root;
	travelAgencyOwnerController taoController;
	
	public TravelAgencyAddCarView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerAddCar.fxml"));
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
		taoController = new travelAgencyOwnerController();
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
		rentalFeeInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		plateNoInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		costPerKmInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
	
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> addButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid Input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			//check if inputs are numeric
			if(!isNumeric(yearInput.getText())) {
				errorMessage.append("Please enter numeric value for year.\n");
			}
			if(!isNumeric(rentalFeeInput.getText())) { 
	            errorMessage.append("Please enter numeric value for rental fee.\n");
			}
			if(!isNumeric(costPerKmInput.getText())) { 
	            errorMessage.append("Please enter numeric value for cost per km.\n");
			}

	        if (errorMessage.length() > 0) {
	            alertInvalidInput.setContentText(errorMessage.toString());
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
			
			Car car=createCarObject();
			ReturnObjectUtility<Boolean> returnData=taoController.addCar(car);
			
			boolean success=returnData.isSuccess();
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

	//car related methods
	public Car createCarObject() {
		String brand = brandInput.getText();
	    String model = modelInput.getText();
	    String plateNumber = plateNoInput.getText().toUpperCase();
	
	    // Parse numeric values
	    int year = Integer.parseInt(yearInput.getText());
	    float rentalFee = Float.parseFloat(rentalFeeInput.getText());
	    float costPerKm = Float.parseFloat(costPerKmInput.getText());
	
	    // Create the Car object
	    return new Car(0, brand, model, year, plateNumber, rentalFee, costPerKm);
	}
	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !brandInput.getText().trim().isEmpty() &&
	        !modelInput.getText().trim().isEmpty() &&
	        !yearInput.getText().trim().isEmpty() &&
	        !rentalFeeInput.getText().trim().isEmpty() &&
	        !plateNoInput.getText().trim().isEmpty() &&
	        !costPerKmInput.getText().trim().isEmpty();

	    addButton.setDisable(!allFieldsFilled);
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(1);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
}
