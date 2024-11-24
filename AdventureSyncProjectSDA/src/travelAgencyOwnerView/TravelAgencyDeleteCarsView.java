package travelAgencyOwnerView;

import java.io.IOException;

import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelOwnerView.HOMManageKitchen;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Car;

public class TravelAgencyDeleteCarsView {
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
	private Button viewButton;
	@FXML
	private Button backButton;
	@FXML
	private Button deleteButton;
	@FXML
	private TextField carIdInput;
	
	
	Parent root;
	travelAgencyOwnerController taoController;
	
	public TravelAgencyDeleteCarsView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerDeleteCar.fxml"));
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
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(1);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
  //assigning buttons and listeners
  	public void listenersAssignment() {
  		carIdInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
  	}
    
    // Method for button handling
    public void eventHandlersAssignment() {
		EventHandler<ActionEvent> deleteButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid Input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			//check if inputs are numeric
			if(!isNumeric(carIdInput.getText())) {
				errorMessage.append("Please enter numeric value for car ID.\n");
			}
			
	        if (errorMessage.length() > 0) {
	            alertInvalidInput.setContentText(errorMessage.toString());
	            alertInvalidInput.showAndWait();
	            return;
	        }
	        
	        int carID=Integer.parseInt(carIdInput.getText());

			ReturnObjectUtility<Boolean> returnData=taoController.deleteCar(carID);
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
		};
		deleteButton.setOnAction(deleteButtonHandler);
        
    	
        viewButton.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerViewCarsView.class, "View Cars"));
        backButton.setOnMouseClicked(createButtonHandler(TravelAgencyManageCarsView.class, "Manage Cars"));
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
    

	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}
	
	//check if all inputs have been given
		private void validateInputs() {
		    boolean allFieldsFilled = 
		        !carIdInput.getText().trim().isEmpty();

		    deleteButton.setDisable(!allFieldsFilled);
		}

}
