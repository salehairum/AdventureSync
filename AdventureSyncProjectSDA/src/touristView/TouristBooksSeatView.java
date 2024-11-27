package touristView;

import java.io.IOException;

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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyOwnerView.TravelAgencyManageCarsView;

public class TouristBooksSeatView {
	@FXML
	private TextField busIdInput;
	@FXML
	private Button selectBusButton;
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
	private TableView<Bus> busTable;
	@FXML
	private TableColumn<Bus, String> colBusId, colModel, colBrand, colYear, colPlateNo, colDriverID;
	@FXML
	private Text msgText;	
	
	private int touristID;
	Parent root;
	TouristController tController;
	
	public TouristBooksSeatView(Integer id) {
		touristID=id;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/touristView/touristBookSeat.fxml"));
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
		displayOwnerDetails();
		getBusDetails();
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
						
			ReturnObjectUtility<Bus> returnData=tController.retrieveBusObjectWithTours(busID);
			boolean success=returnData.isSuccess();
			if(!success) {
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
			    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
			    alert.setHeaderText(null);
			    alert.setContentText(returnData.getMessage());
			    alert.showAndWait();
			}
		    
			else {
		    	//go to next form
		    	//just send poori bus since ab woh seats aa chuki hain us 
		    	//pass this bus
		    	Bus bus=returnData.getObject();
		    	Integer busid = bus.getID();
		    	try {
		            // Create an instance of the controller using its constructor
		    		TouristSelectSeatFromBusView controllerInstance = new TouristSelectSeatFromBusView(touristID, busid);

		            // Get the root node from the controller
		            Parent root = controllerInstance.getRoot();

		            // Create a new scene and stage for the new form
		            Scene newFormScene = new Scene(root);
		            Stage newFormStage = new Stage();
		            newFormStage.setScene(newFormScene);
		            newFormStage.setTitle("Select Seat");

		            // Show the new form
		            newFormStage.show();

		            // Close the current form
		            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		            currentStage.close();

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		};
			
		selectBusButton.setOnAction(selectBusButtonHandler);
		menuButton.setOnMouseClicked(createButtonHandler(TouristTravelServicesMenuView.class,"Travel Services", touristID));
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
	        !busIdInput.getText().trim().isEmpty();

	    selectBusButton.setDisable(!allFieldsFilled);
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(touristID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    public void getBusDetails() {
        // Initialize table columns
        colBusId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("Year"));
        colPlateNo.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));
        colDriverID.setCellValueFactory(new PropertyValueFactory<>("BusDriverID"));

        // Get car details from the controller
        ReturnListUtility<Bus> returnData = tController.getBusDetailsWithBusDriverID();

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<Bus> busList = FXCollections.observableArrayList(returnData.getList().values());
            busTable.setItems(busList); // Set data to the table
        } else {
        	msgText.setVisible(true);
        	msgText.setText(returnData.getMessage());
            busTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
}
