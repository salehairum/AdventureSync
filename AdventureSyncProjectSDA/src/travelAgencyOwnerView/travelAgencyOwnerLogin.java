package travelAgencyOwnerView;

import java.io.IOException;

import controllers.TouristController;
import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelOwnerView.HotelOwnerMenuView;
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
import signupForms.HotelOwnerSignUpView;
import signupForms.TravelAgencyOwnerSignUpView;

public class travelAgencyOwnerLogin {
	@FXML
	private TextField usernameInput;
	@FXML
	private TextField passwordInput;
	@FXML
	private Button loginButton;
	@FXML
	private Button signupButton;
	
	Parent root;
	travelAgencyOwnerController tController;
	
	public travelAgencyOwnerLogin() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwnerView/travelAgencyOwnerLogin.fxml"));
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
		tController = new travelAgencyOwnerController();
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
			if(!success) {
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
				    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
				    alert.setHeaderText(null);
				    alert.setContentText(returnData.getMessage());
				    alert.showAndWait();
			}
			    
			//send bus driver id 
			else {

				int travelAgencyOwnerID=returnData.getObject();
				try {
		            // Dynamically create an instance of the next form's controller with the touristID
		            TravelAgencyOwnerMenuView controllerInstance = new TravelAgencyOwnerMenuView(travelAgencyOwnerID);

		            // Load the next form's scene
		            Parent root = controllerInstance.getRoot();
		            Scene newFormScene = new Scene(root);
		            Stage newFormStage = new Stage();
		            newFormStage.setScene(newFormScene);
		            newFormStage.setTitle("Travel Agency Owner Menu");

		            // Show the new form
		            newFormStage.show();

		            // Close the current form
		            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		            currentStage.close();

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				   	
			}//pass it on
		};
			
		loginButton.setOnAction(signupButtonHandler);
		signupButton.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerSignUpView.class, "Travel Agency Owner SignUp"));
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

	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !usernameInput.getText().trim().isEmpty() &&
	        !passwordInput.getText().trim().isEmpty() ;

		   loginButton.setDisable(!allFieldsFilled);
	}
}
