package touristView;

import java.io.IOException;

import accountAndPersonModels.Tourist;
import controllers.TouristController;
import controllers.hotelOwnerController;
import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dataUtilityClasses.RoomWithHotel;
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
import travelAgencyModels.Car;

public class TouristChecksOutRoomView {
	@FXML
	private TextField roomIDInput;
	@FXML
	private Button checkoutButton;
	@FXML
	private Button backButton;
	@FXML
	private Text name;
	@FXML
	private Text id;
	@FXML
	private Text cnic;
	@FXML
	private Text dob;
	@FXML
	private TableView<RoomWithHotel> roomTable;
	@FXML
	private TableColumn<RoomWithHotel, String> colRoomId, colPrice, colDesc, colHotelId, colHotelName;
	
	private int touristID;
	
	Parent root;
	TouristController tController;
	hotelOwnerController hController;
	Hotel hotel;
	
	public TouristChecksOutRoomView(Integer touristID) {
		this.touristID=touristID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/touristView/touristCheckoutRoom.fxml"));
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
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> rentButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(roomIDInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for room ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int roomID=Integer.parseInt(roomIDInput.getText());
						
			ReturnObjectUtility<Room> returnData= tController.updateRoomBookingStatus(roomID, false);
			boolean success=returnData.isSuccess();
			if(!success) {
				Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			}
			else {
				//first get number of days
				ReturnObjectUtility<Integer> numberOfNights=hController.getNumberOfNights(roomID, touristID);
				
				//mark room as not booked
				ReturnObjectUtility<Integer> returnData2=tController.removeRoomFromBookedRooms(touristID, roomID);	
				success=returnData2.isSuccess();
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
				    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
				    alert.setHeaderText(null);
				    alert.setContentText(returnData2.getMessage());
				    alert.showAndWait();
				   
				if(!success)
					tController.updateRoomBookingStatus(roomID, true); 
				else {
					 try 
					    {
					    	Integer transactionID = returnData2.getObject();
					    	String transactionType = "Room";
				            // Dynamically create an instance of the next form's controller with the touristID
				            TouristPaymentView controllerInstance = new TouristPaymentView(touristID, roomID, transactionType, transactionID, numberOfNights.getObject());

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
				//if transaction could not be made, set isBooked as true i.e it is still booked.
			}
		};
		checkoutButton.setOnAction(rentButtonHandler);
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

	    checkoutButton.setDisable(!allFieldsFilled);
	}
	// Method to display profile
	public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(touristID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    public void loadRoomTable() {
        // Initialize table columns
    	colRoomId.setCellValueFactory(new PropertyValueFactory<>("RoomId"));
    	colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
    	colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
    	colHotelId.setCellValueFactory(new PropertyValueFactory<>("HotelId"));
    	colHotelName.setCellValueFactory(new PropertyValueFactory<>("HotelName"));
        // Get car details from the controller
        ReturnListUtility<RoomWithHotel> returnData = tController.getBookedRoomDetails(touristID);

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<RoomWithHotel> bookedRoomList = FXCollections.observableArrayList(returnData.getList().values());
            roomTable.setItems(bookedRoomList); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
            System.out.println("Error loading bus: " + returnData.getMessage());
            roomTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
}
