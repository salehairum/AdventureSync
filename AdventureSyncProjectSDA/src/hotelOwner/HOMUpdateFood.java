package hotelOwner;

import java.io.IOException;

import dbHandlers.ReturnObjectUtility;
import hotelModels.FoodItem;
import hotelModels.Room;
import hotelModels.hotelOwnerController;
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

public class HOMUpdateFood {
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
	private Button viewFoodButton;
	@FXML
	private Button backButton;
	@FXML
	private TextField foodIDInput;
	@FXML
	private TextField foodNameInput;
	@FXML
	private TextField foodQuanInput;
	@FXML
	private TextField foodPriceInput;
	@FXML
	private Button updFoodButton;	
	
	
	Parent root;
	hotelOwnerController hoController;
	private int hotelOwnerID;
	private int hotelID;
	public HOMUpdateFood(Integer hID) {
		hotelOwnerID = hID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/HOMUpdateFood.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void assignHotelID(){
		hotelID=hoController.getHotelID(hotelOwnerID).getObject();
	}
	@FXML
	private void initialize() {
		listenersAssignment();
		hoController = new hotelOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoController.getHotelOwnerProfileDetail(hotelOwnerID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }

   	public void listenersAssignment() {
   		foodNameInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   		foodQuanInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   		foodIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   		foodPriceInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
   	}
   	
   	public void eventHandlersAssignment() {
   	    EventHandler<ActionEvent> updateButtonHandler = (event) -> {
   	    	Alert alertInvalidInput = new Alert(AlertType.ERROR);
	        alertInvalidInput.setTitle("Invalid Input");

	        if(!foodIDInput.getText().trim().isEmpty()) {
				if(!isNumeric(foodIDInput.getText())) {
					alertInvalidInput.setContentText("Please enter numeric value for food id"); 
					alertInvalidInput.showAndWait(); 
					return;
				}
	        }
	        if(!foodPriceInput.getText().trim().isEmpty()) {
				if(!isNumeric(foodPriceInput.getText())) {
					alertInvalidInput.setContentText("Please enter numeric value for price"); 
					alertInvalidInput.showAndWait(); 
					return;
				}
	        }
	        if(!foodQuanInput.getText().trim().isEmpty()) {
				if(!isNumeric(foodQuanInput.getText())) {
					alertInvalidInput.setContentText("Please enter numeric value for food quantity"); 
					alertInvalidInput.showAndWait(); 
					return;
				}
	        }
   	        int foodID = Integer.parseInt(foodIDInput.getText());
   	        ReturnObjectUtility<FoodItem> foodData=hoController.retrieveFoodItemObject(foodID);
   	        
   	        if(!foodData.isSuccess()) {
   				Alert alert = new Alert(AlertType.ERROR);
   			    alert.setTitle("Operation Failed");
   			    alert.setHeaderText(null);
   			    alert.setContentText(foodData.getMessage());
   			    alert.showAndWait();
   			    return;
   			}
   	        
   	        FoodItem food=updateFoodItemObject(foodData.getObject());
   	        
   	        ReturnObjectUtility<Boolean> returnData=hoController.updateFoodItem(food);
   	        
   	        // Show success or failure message
   	        boolean success = returnData.isSuccess();
   	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
   	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
   	        alert.setHeaderText(null);
   	        alert.setContentText(returnData.getMessage());
   	        alert.showAndWait();
   	    };
   	    updFoodButton.setOnAction(updateButtonHandler);
        viewFoodButton.setOnMouseClicked(createButtonHandler(HOMViewFood.class, "View Food", hotelOwnerID));
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
    public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}

	//bus related methods
	public FoodItem updateFoodItemObject(FoodItem food) {
	    if (!foodNameInput.getText().trim().isEmpty()) {
	    	food.setName(foodNameInput.getText().trim());
	    }
	    if (!foodQuanInput.getText().trim().isEmpty()) {
	    	food.setQuantity(Integer.parseInt(foodQuanInput.getText().trim()));
	    }
	    if (!foodPriceInput.getText().trim().isEmpty()) {
	    	food.setPrice(Float.parseFloat(foodPriceInput.getText().trim()));
	    }
	    return food;
	}
	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean foodIDFilled = !foodIDInput.getText().trim().isEmpty();

	    // Check if at least one of the other fields is filled
	    boolean atLeastOneOtherFieldFilled = 
	        !foodNameInput.getText().trim().isEmpty() ||
	        !foodPriceInput.getText().trim().isEmpty() ||
	        !foodQuanInput.getText().trim().isEmpty();

	    // Enable the button if Bus ID is filled and at least one other field is filled
	    updFoodButton.setDisable(!(foodIDFilled && atLeastOneOtherFieldFilled));
	}

}
