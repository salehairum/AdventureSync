package tourist;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import accountAndPersonModels.Tourist;
import dbHandlers.ReturnObjectUtility;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.Seat;
import travelAgencyModels.TouristController;
import travelAgencyModels.busDriverController;
import travelAgencyModels.travelAgencyOwnerController;

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
	private int touristID;
	
	Parent root;
	TouristController tController;
	travelAgencyOwnerController toaController;
	busDriverController bController;
	Bus bus;
	
	public TouristSelectSeatFromBusView(int id, Bus newBus) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristSelectSeatFromBus.fxml"));
		loader.setController(this);
		touristID=id;
		bus=newBus;
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
			int busID=bus.getID();
			//if seat exists, then it must be booked
			ReturnObjectUtility<Seat> returnData= bController.updateSeatBookingStatus(seatID, true);
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
				ReturnObjectUtility<Integer> returnData2=tController.addSeatToBookedSeats(touristID, seatID);
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
					int transactionID=returnData2.getObject();
				}
			}
		};
			
		bookSeatButton.setOnAction(bookSeatButtonHandler);
		backButton.setOnMouseClicked(createButtonHandler(TouristTravelServicesMenuView.class, "Travel Services"));
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
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(1);

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
}
