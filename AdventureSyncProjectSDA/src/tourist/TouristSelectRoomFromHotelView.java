package tourist;

import java.io.IOException;

import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
import hotelModels.Hotel;
import hotelModels.Room;
import hotelModels.hotelOwnerController;
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
import travelAgencyModels.Seat;
import travelAgencyModels.TouristController;

public class TouristSelectRoomFromHotelView {
	@FXML
	private TextField roomIDInput;
	@FXML
	private Button bookButton;
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
	private TableView<Room> roomTable;
	@FXML
	private TableColumn<Room, String> colRoomId, colRating, colPrice, colDesc;
	
	private int touristID;
	
	Parent root;
	TouristController tController;
	hotelOwnerController hController;
	Hotel hotel;
	
	public TouristSelectRoomFromHotelView(Integer touristID, Hotel newHotel) {
		this.touristID=touristID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristSelectRoomFromHotel.fxml"));
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
		loadRoomTable();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		roomIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	//afsah tumne yahan doosre form par lekar jana at the end-->this is just like book seat
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> bookButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(roomIDInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for room ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int roomID=Integer.parseInt(roomIDInput.getText());		
			int hotelID=hotel.getHotelID();
			//if seat exists, then it must be booked
			ReturnObjectUtility<Room> returnData= tController.updateRoomBookingStatus(roomID, true);
			boolean success=returnData.isSuccess();
			if(!success) {
				Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			}
			else {
				//mark car as rented
				ReturnObjectUtility<Integer> returnData2=tController.addRoomToBookedRooms(touristID, roomID);
				success=returnData2.isSuccess();
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
				    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
				    alert.setHeaderText(null);
				    alert.setContentText(returnData2.getMessage());
				    alert.showAndWait();
				    loadRoomTable();
				if(!success)
					tController.updateRoomBookingStatus(roomID, false);
				else {
					int transactionID=returnData2.getObject();
				}
			}
		};
			
		bookButton.setOnAction(bookButtonHandler);
		backButton.setOnMouseClicked(createButtonHandler(TouristHotelServicesMenuView.class, "Hotel Services", touristID));
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
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(touristID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
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
	        !roomIDInput.getText().trim().isEmpty();

	   bookButton.setDisable(!allFieldsFilled);
	}
	public void loadRoomTable() {
        // Initialize table columns
    	colRoomId.setCellValueFactory(new PropertyValueFactory<>("RoomID"));
    	colRating.setCellValueFactory(new PropertyValueFactory<>("OverallRating"));
    	colPrice.setCellValueFactory(new PropertyValueFactory<>("PricePerNight"));
    	colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        // Get car details from the controller
        ReturnListUtility<Room> returnData = tController.getNonBookedRoomDetails(hotel.getHotelID());

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
