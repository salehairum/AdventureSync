package travelAgencyOwnerView;

import java.io.IOException;

import accountAndPersonModels.TravelAgencyOwner;
import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import signupForms.AccountMenuView;

public class TravelAgencyOwnerDeleteAccountView {
	@FXML
	private Pane sidePanel;
	@FXML
	private Pane mainPanel;
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
	@FXML
	private Button yesButton;
	
	Parent root;
	travelAgencyOwnerController taoController;
	private int tOwnerID;
	
	public TravelAgencyOwnerDeleteAccountView(Integer id) {
		tOwnerID = id;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwnerView/travelAgencyOwnerDeleteAccount.fxml"));
		loader.setController(this);
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
		taoController = new travelAgencyOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(tOwnerID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    // Method for button handling
    public void eventHandlersAssignment() {
		EventHandler<ActionEvent> yesButtonHandler=(event)->{
			
			ReturnObjectUtility<TravelAgencyOwner> returnData=taoController.deleteAgencyOwner(tOwnerID);
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			    if(success)
			    {
			    	try {
			            // Dynamically create an instance of the next form's controller with the touristID
						AccountMenuView controllerInstance = new AccountMenuView();

			            // Load the next form's scene
			            Parent root = controllerInstance.getRoot();
			            Scene newFormScene = new Scene(root);
			            Stage newFormStage = new Stage();
			            newFormStage.setScene(newFormScene);
			            newFormStage.setTitle("Menu");

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
		yesButton.setOnAction(yesButtonHandler);
        // Assign handlers with parameters for specific FXMLs and classes
        backButton.setOnMouseClicked(createButtonHandler(TravelAgencyManageAccountView.class, "Manage Account", tOwnerID));
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
}
