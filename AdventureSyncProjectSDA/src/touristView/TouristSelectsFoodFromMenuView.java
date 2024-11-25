package touristView;

import java.io.IOException;

import controllers.TouristController;
import controllers.hotelOwnerController;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import hotelModels.FoodItem;
import hotelModels.Hotel;
import hotelModels.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TouristSelectsFoodFromMenuView {
	@FXML
	private TextField foodIDInput;
	@FXML
	private TextField foodQuanInput;
	@FXML
	private Button orderButton;
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
	private TableView<FoodItem> foodTable;
	@FXML
	private TableColumn<FoodItem, String> colFoodId, colName, colQuan, colPrice;
	
	private int touristID;
	
	Parent root;
	TouristController tController;
	hotelOwnerController hController;
	Hotel hotel;
	
	public TouristSelectsFoodFromMenuView(Integer id, Hotel newHotel) {
		touristID=id;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/touristView/touristSelectsFoodFromMenu.fxml"));
		loader.setController(this);
		hotel=newHotel;
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
		hController = new hotelOwnerController();
		displayOwnerDetails();
		loadFoodTable();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		foodIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		foodQuanInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> orderButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(foodIDInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for food ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			if(!isNumeric(foodQuanInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for food quantity"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int foodID=Integer.parseInt(foodIDInput.getText());					
			int foodQuantity=Integer.parseInt(foodQuanInput.getText());
			
			if(foodQuantity<=0) {
				alertInvalidInput.setContentText("Food quantity should be greater than zero."); 
				alertInvalidInput.showAndWait(); 
				return;
			}

			ReturnObjectUtility<FoodItem> returnData= tController.updateFoodQuantity(foodID, foodQuantity, false);
			boolean success=returnData.isSuccess();
			if(!success) {
				Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			}
			else {
				//add to transaction history
				ReturnObjectUtility<Integer> returnData2=tController.orderFood(touristID, foodID);
				success=returnData2.isSuccess();
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
				    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
				    alert.setHeaderText(null);
				    alert.setContentText(returnData2.getMessage());
				    alert.showAndWait();
				    
				if(success) {
					Integer transactionID=returnData2.getObject();
					try 
				    {
				    	String transactionType = "Order";
			            // Dynamically create an instance of the next form's controller with the touristID
			            TouristPaymentView controllerInstance = new TouristPaymentView(touristID, foodID, transactionType, transactionID, foodQuantity);

			            // Load the next form's scene
			            Parent root = controllerInstance.getRoot();
			            Scene newFormScene = new Scene(root);
			            Stage newFormStage = new Stage();
			            newFormStage.setScene(newFormScene);
			            newFormStage.setTitle("Payment Gateway");

			            // Show the new form
			            newFormStage.show();

			            // Close the current form
			            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			            currentStage.close();

			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				}
			}
		};
			
		orderButton.setOnAction(orderButtonHandler);
		backButton.setOnMouseClicked(createButtonHandler(TouristHotelServicesMenuView.class,"Hotel Services", touristID));
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
	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !foodIDInput.getText().trim().isEmpty() &&
	        !foodQuanInput.getText().trim().isEmpty();

	   orderButton.setDisable(!allFieldsFilled);
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(touristID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    public void loadFoodTable() {
        // Initialize table columns
    	colFoodId.setCellValueFactory(new PropertyValueFactory<>("FoodID"));
    	colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    	colQuan.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
    	colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Get car details from the controller
        ReturnListUtility<FoodItem> returnData = tController.getFoodDetails(hotel.getHotelID());

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<FoodItem> foodList = FXCollections.observableArrayList(returnData.getList().values());
            foodTable.setItems(foodList); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
            System.out.println("Error loading food items: " + returnData.getMessage());
            foodTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
	
}
