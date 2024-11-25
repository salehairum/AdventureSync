package hotelOwnerView;

import java.io.IOException;

import dataUtilityClasses.ReturnListUtility;
import hotelModels.Room;
import controllers.hotelOwnerController;
import javafx.beans.property.SimpleStringProperty;
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


public class HOMViewRoom {
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
	private Button updRoomButton;
	@FXML
	private Button delRoomButton;
	@FXML
	private TableView<Room> roomTable;
	@FXML
	private TableColumn<Room, String> colRoomId, colRating, colPrice, colDesc, colIsBooked;
	
	Parent root;
	hotelOwnerController hoController;
	private int hOwnerID;
	private int hotelID;
	public HOMViewRoom(Integer hID) {
		hOwnerID = hID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/HOMViewRoom.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void assignHotelID(){
		hotelID=hoController.getHotelID(hOwnerID).getObject();
	}
	@FXML
	private void initialize() {
		hoController = new hotelOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
		loadRoomTable();
		assignHotelID();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoController.getHotelOwnerProfileDetail(hOwnerID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        backButton.setOnMouseClicked(createButtonHandler(HOMManageRoom.class, "Manage Room", hOwnerID));
        delRoomButton.setOnMouseClicked(createButtonHandler(HOMDeleteRoom.class, "Delete Room", hOwnerID));
        updRoomButton.setOnMouseClicked(createButtonHandler(HOMUpdateRoom.class, "Update Room", hOwnerID));
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
    public void loadRoomTable() {
        // Initialize table columns
    	colRoomId.setCellValueFactory(new PropertyValueFactory<>("RoomID"));
    	colRating.setCellValueFactory(new PropertyValueFactory<>("OverallRating"));
    	colPrice.setCellValueFactory(new PropertyValueFactory<>("PricePerNight"));
    	//colIsBooked.setCellValueFactory(new PropertyValueFactory<>("isBooked"));
    	colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        // Get car details from the controller
        ReturnListUtility<Room> returnData = hoController.getRoomDetails(hOwnerID);

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<Room> roomList = FXCollections.observableArrayList(returnData.getList().values());
            roomTable.setItems(roomList); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
            System.out.println("Error loading bus: " + returnData.getMessage());
            roomTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
}
