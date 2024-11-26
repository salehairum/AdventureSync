package touristView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import accountAndPersonModels.Tourist;
import controllers.TouristController;
import controllers.busDriverController;
import controllers.travelAgencyOwnerController;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
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
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.Seat;

public class TouristSelectSeatFromBusView {
	@FXML
	private TextField seatIdInput;
	@FXML
	private Button bookSeatButton;
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
	private TableView<Seat> seatTable;
	@FXML
	private TableColumn<Seat, String> colSeatId, colRowNo;
	@FXML
	private Text msgText;	
	
	
	private int touristID;
	private int busID;
	Parent root;
	TouristController tController;
	travelAgencyOwnerController toaController;
	busDriverController bController;
	
	public TouristSelectSeatFromBusView(Integer id, Integer bID) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/touristView/touristSelectSeatFromBus.fxml"));
		loader.setController(this);
		touristID=id;
		busID=bID;
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
		toaController = new travelAgencyOwnerController();
		bController = new busDriverController();
		displayOwnerDetails();
		loadSeatTable();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		seatIdInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> bookSeatButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(seatIdInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for seat ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int seatID=Integer.parseInt(seatIdInput.getText());
			//if seat exists, then it must be booked
			ReturnObjectUtility<Integer> returnData=tController.addSeatToBookedSeats(touristID, seatID);
			boolean success=returnData.isSuccess();
			if(!success) {
				Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			}
			else {
				//add seat to booked seats
				ReturnObjectUtility<Seat> returnData2= bController.updateSeatBookingStatus(seatID, true);
				success=returnData2.isSuccess();
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
				    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
				    alert.setHeaderText(null);
				    alert.setContentText(returnData2.getMessage());
				    alert.showAndWait();
				    
				if(!success)
					bController.updateSeatBookingStatus(seatID, false);
				else {
					//go to payment
					//pass busId, nottt seatID!!
					//pass transactionID
					int transactionID=returnData.getObject();
					try 
				    {
				    	String transactionType = "Bus";
			            // Dynamically create an instance of the next form's controller with the touristID
			            TouristPaymentView controllerInstance = new TouristPaymentView(touristID, busID, transactionType, transactionID, 0);

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
			
		bookSeatButton.setOnAction(bookSeatButtonHandler);
		backButton.setOnMouseClicked(createButtonHandler(TouristTravelServicesMenuView.class, "Travel Services", touristID));
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
	        !seatIdInput.getText().trim().isEmpty();

	    bookSeatButton.setDisable(!allFieldsFilled);
	}
	public void loadSeatTable() {
        // Initialize table columns
        colSeatId.setCellValueFactory(new PropertyValueFactory<>("SeatID"));
        colRowNo.setCellValueFactory(new PropertyValueFactory<>("RowNo"));

        // Get car details from the controller
        ReturnListUtility<Seat> returnData = tController.getSeatDetails(busID);

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<Seat> seatList = FXCollections.observableArrayList(returnData.getList().values());
            seatTable.setItems(seatList); // Set data to the table
        } else {
        	msgText.setVisible(true);
        	msgText.setText(returnData.getMessage());
            seatTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
}
