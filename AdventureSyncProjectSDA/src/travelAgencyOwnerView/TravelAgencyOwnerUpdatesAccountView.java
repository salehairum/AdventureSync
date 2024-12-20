package travelAgencyOwnerView;

import java.io.IOException;

import accountAndPersonModels.Tourist;
import accountAndPersonModels.TravelAgencyOwner;
import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelOwnerView.HotelOwnerUpdatesPasswordView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TravelAgencyOwnerUpdatesAccountView {
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
	private Button updateButton;
	@FXML
	private TextField nameInput;
	@FXML
	private TextField emailInput;
	@FXML
	private TextField usernameInput;
	@FXML
	private TextField cnicInput;
	@FXML
	private TextField balanceInput;
	@FXML
	private DatePicker dobInput;
	@FXML
	private Button updPasswordButton;
	
	Parent root;
	travelAgencyOwnerController taoController;
	
	private int agencyOwnerID;
	public TravelAgencyOwnerUpdatesAccountView(Integer id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwnerView/travelAgencyOwnerUpdateAccount.fxml"));
		loader.setController(this);
		agencyOwnerID=id;
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
		listenersAssignment();
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(agencyOwnerID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
 // Method for button handling
    public void eventHandlersAssignment() {
    	EventHandler<ActionEvent> updateButtonHandler = (event) -> {
	        // Create a single alert instance to avoid repeated showAndWait() calls
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			if(!balanceInput.getText().trim().isEmpty() && !isNumeric(balanceInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for balance"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			if(!emailInput.getText().trim().isEmpty() && !isValidEmail(emailInput.getText())) {
				alertInvalidInput.setContentText("Please enter valid email"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			if(!cnicInput.getText().trim().isEmpty() && !isValidCNIC(cnicInput.getText())) {
				alertInvalidInput.setContentText("Please enter valid cnic of format XXXXX-XXXXXXX-X"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
	        
	        TravelAgencyOwner TravelAgencyOwnerData=TravelAgencyOwner.getInstance();
	        
	        TravelAgencyOwner agencyOwner=updateTravelAgencyOwnerObject(TravelAgencyOwnerData);
	        
	        ReturnObjectUtility<TravelAgencyOwner> returnData=taoController.updateAgencyOwner(agencyOwner);
	        
	        // Show success or failure message
	        boolean success = returnData.isSuccess();
	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
	        alert.setHeaderText(null);
	        alert.setContentText(returnData.getMessage());
	        alert.showAndWait();
	    };
	    updateButton.setOnAction(updateButtonHandler);
        // Assign handlers with parameters for specific FXMLs and classes
        backButton.setOnMouseClicked(createButtonHandler(TravelAgencyManageAccountView.class, "Manage Account", agencyOwnerID));
        updPasswordButton.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerUpdatePasswordView.class, "Change Password", agencyOwnerID));
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
    public TravelAgencyOwner updateTravelAgencyOwnerObject(TravelAgencyOwner agencyOwner) {
	    if (!nameInput.getText().trim().isEmpty()) {
	    	agencyOwner.setName(nameInput.getText().trim());
	    }
	    if (!usernameInput.getText().trim().isEmpty()) {
	    	agencyOwner.getAccount().setUsername(usernameInput.getText().trim());
	    }
	    if (!emailInput.getText().trim().isEmpty()) {
	    	agencyOwner.getAccount().setEmail(emailInput.getText());
	    }
	    if (!balanceInput.getText().trim().isEmpty()) {
	    	agencyOwner.getAccount().setBalance((Integer.parseInt(balanceInput.getText().trim())));
	    }
	    if (!cnicInput.getText().trim().isEmpty()) {
	    	agencyOwner.setCnic(cnicInput.getText().trim());
	    }
	    if (dobInput.getValue()!=null) {
	    	agencyOwner.setDob(dobInput.getValue());
	    }
	    return agencyOwner;
	}
    
	//assigning buttons and listeners
	public void listenersAssignment() {
		nameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		emailInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		usernameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		balanceInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		cnicInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		dobInput.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs()); 
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
	public boolean isValidCNIC(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    
	    // Regular expression for CNIC format: XXXXX-XXXXXXX-X
	    return str.matches("\\d{5}-\\d{7}-\\d{1}");
	}
	private void validateInputs() {
	    // Check if at least one of the other fields is filled
	    boolean atLeastOneOtherFieldFilled = 
	        !nameInput.getText().trim().isEmpty() ||
	        dobInput.getValue()!=null ||
	        !usernameInput.getText().trim().isEmpty() ||
	        !cnicInput.getText().trim().isEmpty() ||
	        !balanceInput.getText().trim().isEmpty() ||
	        !emailInput.getText().trim().isEmpty();

	    // Enable the button if Bus ID is filled and at least one other field is filled
	    updateButton.setDisable(!atLeastOneOtherFieldFilled);
	}

}
