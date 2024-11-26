package hotelOwnerView;

import java.io.IOException;

import controllers.hotelOwnerController;
import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnObjectUtility;
import hotelModels.FoodItem;
import hotelModels.Room;
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

public class HOMAddFood {
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
	private Button menuButton;
	@FXML
	private Button backButton;
	@FXML
	private Button addFoodButton;
	@FXML
	private TextField foodNameInput;
	@FXML
	private TextField foodQuanInput;
	@FXML
	private TextField foodPriceInput;
	
	private int hotelID;
	private int hotelOwnerID;
	
	Parent root;
	hotelOwnerController hoContoller;
	public HOMAddFood(Integer id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwnerView/HOMAddFood.fxml"));
		loader.setController(this);
		hotelOwnerID=id;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void initialize() {
		listenersAssignment();
		hoContoller = new hotelOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
		assignHotelID();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoContoller.getHotelOwnerProfileDetail(hotelOwnerID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {	
    	EventHandler<ActionEvent> addButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid Input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			//check if inputs are numeric
			if(!isNumeric(foodQuanInput.getText())) {
				errorMessage.append("Please enter numeric value for food quantity.\n");
			}
			if(!isNumeric(foodPriceInput.getText())) {
				errorMessage.append("Please enter numeric value for food price.\n");
			}
	        if (errorMessage.length() > 0) {
	            alertInvalidInput.setContentText(errorMessage.toString());
	            alertInvalidInput.showAndWait();
	            return;
	        }
			FoodItem food=createFoodItemObject();
			ReturnObjectUtility<Boolean> returnData=hoContoller.addFoodItem(food, hotelID);
			
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
		};
		addFoodButton.setOnAction(addButtonHandler);
        // Assign handlers with parameters for specific FXMLs and classes
        backButton.setOnMouseClicked(createButtonHandler(HOMManageKitchen.class, "Manage Kitchen", hotelOwnerID));
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
    public void assignHotelID(){
		hotelID=hoContoller.getHotelID(hotelOwnerID).getObject();
	}
	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}

	public FoodItem createFoodItemObject() {
	    // Retrieve inputs from the corresponding TextFields
	    String name = foodNameInput.getText();
	    int quantity = Integer.parseInt(foodQuanInput.getText());
	    float price = Float.parseFloat(foodPriceInput.getText());

	    // Create and return the FoodItem object
	    return new FoodItem(0, name, quantity, price);
	}



	//assigning buttons and listeners
	public void listenersAssignment() {
		foodQuanInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		foodPriceInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		foodNameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !foodQuanInput.getText().trim().isEmpty() &&
	        !foodPriceInput.getText().trim().isEmpty() &&
	    !foodNameInput.getText().trim().isEmpty();

	    addFoodButton.setDisable(!allFieldsFilled);
	}
}
