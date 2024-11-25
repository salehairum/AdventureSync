package touristView;

import java.io.IOException;

import controllers.TouristController;
import dataUtilityClasses.ReturnListUtility;
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
import travelAgencyModels.Bus;

public class TouristRatesBusTourView {
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
	private TableView<Bus> busTable;
	@FXML
	private TableColumn<Bus, String> colBusId, colModel, colBrand, colYear, colPlateNo;
	Parent root;
	TouristController tController;
	int touristID;
	public TouristRatesBusTourView(Integer tID) {
		touristID = tID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristRatesBusTour.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public Parent getRoot() {
		return root;
	}
	@FXML
	private void initialize() {
		tController = new TouristController();
		displayOwnerDetails();
		eventHandlersAssignment();
		loadBusTable();
	}
	public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
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
    public void loadBusTable() {
        // Initialize table columns
        colBusId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("Year"));
        colPlateNo.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));

        // Get car details from the controller
        ReturnListUtility<Bus> returnData = tController.getBusDetailsWithTouristID(touristID);

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<Bus> busList = FXCollections.observableArrayList(returnData.getList().values());
            busTable.setItems(busList); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
            System.out.println("Error loading bus: " + returnData.getMessage());
            busTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
}
