package travelAgencyOwnerView;

import java.io.IOException;

import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelOwnerView.HOMManageKitchen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Car;

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
	@FXML
	private Button backButton;
	
	Parent root;
	travelAgencyOwnerController taoController;
	private int tOwnerID;
	
	public TravelAgencyAddCarView(Integer id) {
		tOwnerID = id;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwnerView/travelAgencyOwnerAddCar.fxml"));
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

	        int costPerKm=Integer.parseInt(costPerKmInput.getText());
			
			if(costPerKm<=0) {
				alertInvalidInput.setContentText("Cost per km should be greater than zero."); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			float rentalFee=Float.parseFloat(rentalFeeInput.getText());
				
			if(rentalFee<=0) {
					alertInvalidInput.setContentText("Rental fee should be greater than zero."); 
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
        backButton.setOnMouseClicked(createButtonHandler(TravelAgencyManageCarsView.class, "Manage Cars", tOwnerID));
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
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(tOwnerID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
}
