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
		//displayOwnerDetails();
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
			
			//check if inputs are numeric
			if(!isNumeric(yearInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for year"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			if(!isNumeric(rentalFeeInput.getText())) { 
				alertInvalidInput.setContentText("Please enter numeric value for rental fee"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			if(!isNumeric(costPerKmInput.getText())) { 
				alertInvalidInput.setContentText("Please enter numeric value for cost per km"); 
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
	    String plateNumber = plateNoInput.getText();
	
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
        Text nameText = (Text) root.lookup("#name");
        Text idText = (Text) root.lookup("#id");
        Text cnicText = (Text) root.lookup("#cnic");
        Text dobText = (Text) root.lookup("#dob");
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(1);
        if (nameText != null) {
            nameText.setText(profileDetail[0]);
        }
        if (idText != null) {
            idText.setText(profileDetail[1]);
        }
        if (cnicText != null) {
        	cnicText.setText(profileDetail[2]);
        }
        if (dobText != null) {
        	dobText.setText(profileDetail[3]);
        }
    }
}
