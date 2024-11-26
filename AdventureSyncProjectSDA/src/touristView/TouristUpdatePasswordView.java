package touristView;

import java.io.IOException;

import accountAndPersonModels.Tourist;
import controllers.TouristController;
import dataUtilityClasses.ReturnObjectUtility;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TouristUpdatePasswordView {
	@FXML
	private Text name;
	@FXML
	private Text id;
	@FXML
	private Text cnic;
	@FXML
	private Text dob;
	@FXML
	private TextField passwordInput;
	@FXML
	private TextField oldPasswordInput;
	@FXML
	private Button updateButton;
	@FXML
	private Button backButton;
	
	Parent root;
	TouristController tController;
	private int touristID;
	
	public TouristUpdatePasswordView(int id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/touristView/touristUpdatePassword.fxml"));
		loader.setController(this);
		touristID=id;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public Parent getRoot() {
		return root;
	}
	@FXML
	private void initialize() {
		listenersAssignment();
		tController = new TouristController();
		displayOwnerDetails();
		eventHandlersAssignment();
	}
	public void eventHandlersAssignment() {	    
		EventHandler<ActionEvent> updateButtonHandler = (event) -> {
	        // Create a single alert instance to avoid repeated showAndWait() calls
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			if(!isValidPassword(passwordInput.getText().trim())) {
				alertInvalidInput.setContentText("Please enter password fulfilling all conditions."); 
				alertInvalidInput.showAndWait(); 
				return;
			}
	        
			String oldPassword=oldPasswordInput.getText().trim();
			String password=passwordInput.getText().trim();
	        ReturnObjectUtility<Integer> accountData=tController.compareOldPassword(oldPassword, touristID);
	     
	        if(!accountData.isSuccess()) {
				Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(accountData.getMessage());
			    alert.showAndWait();
			    return;
			}
	        
	        //else update your password
	        ReturnObjectUtility<Tourist> returnData=tController.updatePassword(password, accountData.getObject());
	        
	        // Show success or failure message
	        boolean success = returnData.isSuccess();
	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
	        alert.setHeaderText(null);
	        alert.setContentText(returnData.getMessage());
	        alert.showAndWait();
	    };
	    updateButton.setOnAction(updateButtonHandler);
	    backButton.setOnMouseClicked(createButtonHandler(TouristManagesAccountView.class, "Manage Account", touristID));
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
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(touristID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }

	//assigning buttons and listeners
	public void listenersAssignment() {
		passwordInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
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
	
	private void validateInputs() {
	    // Check if at least one of the other fields is filled
	    boolean allFieldsFilled = 
	        !passwordInput.getText().trim().isEmpty() && 
	        !oldPasswordInput.getText().trim().isEmpty();

	    // Enable the button if Bus ID is filled and at least one other field is filled
	    updateButton.setDisable(!allFieldsFilled);
	}

}
