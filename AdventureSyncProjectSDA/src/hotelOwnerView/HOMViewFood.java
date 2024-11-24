package hotelOwnerView;

import java.io.IOException;

import controllers.hotelOwnerController;
import dataUtilityClasses.ReturnListUtility;
import hotelModels.FoodItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HOMViewFood {
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
	private Button updFoodButton;
	@FXML
	private Button delFoodButton;
	@FXML
	private TableView<FoodItem> foodTable;
	@FXML
	private TableColumn<FoodItem, String> colFoodId, colName, colQuan, colPrice;
	
	Parent root;
	hotelOwnerController hoContoller;
	private int hotelOwnerID;
	private int hotelID;
	
	public HOMViewFood(Integer hID) {
		hotelOwnerID = hID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/HOMViewFood.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void assignHotelID(){
		hotelID=hoContoller.getHotelID(hotelOwnerID).getObject();
	}
	@FXML
	private void initialize() {
		hoContoller = new hotelOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
		loadFoodTable();
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
        // Assign handlers with parameters for specific FXMLs and classes
        backButton.setOnMouseClicked(createButtonHandler(HOMManageKitchen.class, "Manage Kitchen", hotelOwnerID));
        delFoodButton.setOnMouseClicked(createButtonHandler(HOMDeleteFood.class, "Delete Food", hotelOwnerID));
        updFoodButton.setOnMouseClicked(createButtonHandler(HOMUpdateFood.class, "Update Food", hotelOwnerID));
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
    public void loadFoodTable() {
        // Initialize table columns
    	colFoodId.setCellValueFactory(new PropertyValueFactory<>("FoodID"));
    	colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    	colQuan.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
    	colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Get car details from the controller
        ReturnListUtility<FoodItem> returnData = hoContoller.getFoodDetails(1);

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
