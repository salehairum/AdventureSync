package signupForms;

import java.io.IOException;
import java.time.LocalDate;

import accountAndPersonModels.Account;
import accountAndPersonModels.BusDriver;
import accountAndPersonModels.TravelAgencyOwner;
import busDriverView.busDriverLogin;
import controllers.busDriverController;
import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelOwnerView.HotelOwnerMenuView;
import hotelOwnerView.hotelOwnerLogin;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BusDriverSignUpView {
	@FXML
	private Button signupButton;
	@FXML
	private TextField nameInput;
	@FXML
	private TextField emailInput;
	@FXML
	private TextField usernameInput;
	@FXML
	private TextField passwordInput;
	@FXML
	private TextField cnicInput;
	@FXML
	private TextField balanceInput;
	@FXML
	private DatePicker dobInput;
	@FXML
	private Text loginLabel;
	@FXML
	private Button menuButton;
	
	Parent root;
	busDriverController bController;
	int busID;
	
	public BusDriverSignUpView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupForms/busDriverSignup.fxml"));
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
		nameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		emailInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		usernameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		passwordInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		balanceInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		cnicInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		dobInput.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs()); 
	}
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> signupButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Weak Password"); 
			
			StringBuilder errorMessage = new StringBuilder();
			if (!isValidPassword(passwordInput.getText())) {
				alertInvalidInput.setContentText("Please enter password fulfilling the conditions specified."); 
				alertInvalidInput.showAndWait();
				return;
			}
			
			if(!isNumeric(balanceInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for balance"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			if(!isValidEmail(emailInput.getText())) {
				alertInvalidInput.setContentText("Please enter valid email"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			if(!isValidCNIC(cnicInput.getText())) {
				alertInvalidInput.setContentText("Please enter valid cnic of the format XXXXX-XXXXXXX-X"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			LocalDate currentDate = LocalDate.now();

			// Calculate age by comparing current year with birth year
			int age = currentDate.getYear() - dobInput.getValue().getYear();

			if(age<10) {
				alertInvalidInput.setContentText("User should be at least 10 years old."); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			BusDriver busDriver=createBusDriverObject();
			ReturnObjectUtility<BusDriver> returnData=bController.addBusDriver(busDriver);				
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			  
			if(success)
			{
				//send bus driver id 
				int busDriverID=returnData.getObject().getBusDriverID();
				try {
		            // Dynamically create an instance of the next form's controller with the touristID
		            BusDriverAddsBus controllerInstance = new BusDriverAddsBus(busDriverID);

		            // Load the next form's scene
		            Parent root = controllerInstance.getRoot();
		            Scene newFormScene = new Scene(root);
		            Stage newFormStage = new Stage();
		            newFormStage.setScene(newFormScene);
		            newFormStage.setTitle("Add Bus");

		            // Show the new form
		            newFormStage.show();

		            // Close the current form
		            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		            currentStage.close();

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		};
			
		signupButton.setOnAction(signupButtonHandler);
		loginLabel.setOnMouseClicked(createButtonHandler(busDriverLogin.class, "Driver Login"));
		menuButton.setOnMouseClicked(createButtonHandler(AccountMenuView.class, "Account Menu"));
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
	public boolean isValidPassword(String password) {
	    if (password == null || password.isEmpty()) {
	        return false;
	    }

	    // Regex explanation:
	    // (?=.*[A-Z]): At least one uppercase letter
	    // (?=.*[a-z]): At least one lowercase letter
	    // (?=.*\\d): At least one digit
	    // (?=.*[@#$%^&+=!]): At least one special character (adjust as needed)
	    // .{8,}: At least 8 characters long
		String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

		return password.matches(passwordRegex);
	}

	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}

	private boolean isValidEmail(String email) {
	    return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
	}

	
	public BusDriver createBusDriverObject() {
	    // Retrieve inputs from the FXML fields
	    String name = nameInput.getText();
	    String email = emailInput.getText();
	    String username = usernameInput.getText();
	    String password = passwordInput.getText();
	    String cnic = cnicInput.getText();
	    LocalDate dob = dobInput.getValue(); // DatePicker returns a LocalDate
	    float balance = Float.parseFloat(balanceInput.getText());

	    // Create Account object
	    Account account = new Account(0, username, password, email, balance);

	    // Create TravelAgencyOwner object with the provided details
	    BusDriver busDriver = new BusDriver(0, name, dob, cnic);
	    busDriver.setAccount(account);

	    return busDriver;
	}
	public boolean isValidCNIC(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    
	    // Regular expression for CNIC format: XXXXX-XXXXXXX-X
	    return str.matches("\\d{5}-\\d{7}-\\d{1}");
	}
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !nameInput.getText().trim().isEmpty() &&
	        !emailInput.getText().trim().isEmpty() &&
	        !cnicInput.getText().trim().isEmpty() &&
	        !balanceInput.getText().trim().isEmpty() &&
	        !usernameInput.getText().trim().isEmpty() &&
	        !passwordInput.getText().trim().isEmpty() &&
	        dobInput.getValue()!=null;

		   signupButton.setDisable(!allFieldsFilled);
	}
}
