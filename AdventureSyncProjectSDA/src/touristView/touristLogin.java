package touristView;

import java.io.IOException;

import controllers.TouristController;
import controllers.busDriverController;
import dataUtilityClasses.ReturnObjectUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class touristLogin {
	@FXML
	private TextField usernameInput;
	@FXML
	private TextField passwordInput;
	@FXML
	private Button loginButton;
	
	Parent root;
	TouristController tController;
	
	public touristLogin() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristLogin.fxml"));
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
		tController = new TouristController();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		usernameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		passwordInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> signupButtonHandler=(event)->{
			
			String username=usernameInput.getText();
			String password=passwordInput.getText();
			ReturnObjectUtility<Integer> returnData=tController.checkPassword(password, username);
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			    
			//send tourist id 
			    if (success) {
			        int touristID = returnData.getObject(); // Assuming this retrieves the tourist ID
			        try {
			            // Dynamically create an instance of the next form's controller with the touristID
			            TouristMenuView controllerInstance = new TouristMenuView(touristID);

			            // Load the next form's scene
			            Parent root = controllerInstance.getRoot();
			            Scene newFormScene = new Scene(root);
			            Stage newFormStage = new Stage();
			            newFormStage.setScene(newFormScene);
			            newFormStage.setTitle("Tourist Menu");

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
			
		loginButton.setOnAction(signupButtonHandler);
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

	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !usernameInput.getText().trim().isEmpty() &&
	        !passwordInput.getText().trim().isEmpty() ;

		   loginButton.setDisable(!allFieldsFilled);
	}
}
