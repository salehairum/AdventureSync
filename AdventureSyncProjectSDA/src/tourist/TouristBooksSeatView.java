package tourist;

import java.io.IOException;

import accountAndPersonModels.Tourist;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.TouristController;
import travelAgencyModels.busDriverController;
import travelAgencyModels.travelAgencyOwnerController;

public class TouristBooksSeatView {
	@FXML
	private TextField busIdInput;
	@FXML
	private Button selectBusButton;
	
	private int touristID;
	
	Parent root;
	TouristController tController;
	travelAgencyOwnerController toaController;
	busDriverController bController;
	
	public TouristBooksSeatView(int id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristBookSeat.fxml"));
		loader.setController(this);
		touristID=id;
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
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		busIdInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	//afsah tumne yahan doosre form par lekar jana at the end
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> selectBusButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(busIdInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for bus ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int busID=Integer.parseInt(busIdInput.getText());
						
			ReturnObjectUtility<Bus> returnData=bController.retrieveBusObject(busID);
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
		    alert.setHeaderText(null);
		    alert.setContentText(returnData.getMessage());
		    alert.showAndWait();
		    
		    if(success) {
		    	//go to next form
		    	//just send poori bus since ab woh seats aa chuki hain us 
		    	//pass this bus
		    	Bus bus=returnData.getObject();
		    }
		};
			
		selectBusButton.setOnAction(selectBusButtonHandler);
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
	        !busIdInput.getText().trim().isEmpty();

	    selectBusButton.setDisable(!allFieldsFilled);
	}
}
