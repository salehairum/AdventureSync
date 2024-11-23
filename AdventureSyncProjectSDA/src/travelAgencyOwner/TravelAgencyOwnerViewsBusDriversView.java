package travelAgencyOwner;

import java.io.IOException;

import accountAndPersonModels.BusDriver;
import dbHandlers.ReturnListUtility;
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
import travelAgencyModels.travelAgencyOwnerController;

public class TravelAgencyOwnerViewsBusDriversView {
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
	private TableView<BusDriver> busDriverTable;
	@FXML
	private TableColumn<BusDriver, String> colBDriverID, colName, colDOB, colCNIC;
	Parent root;
	travelAgencyOwnerController taoController;
	public TravelAgencyOwnerViewsBusDriversView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("travelAgencyOwnerViewsBusDrivers.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		taoController = new travelAgencyOwnerController();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	@FXML
	private void initialize() {
		taoController = new travelAgencyOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
		loadBusDriverTable();
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(1);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
 // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        backButton.setOnMouseClicked(createButtonHandler(TravelAgencyManageBusView.class, "Manage Buses"));
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
    
    public void loadBusDriverTable() {
        // Initialize table columns
        colBDriverID.setCellValueFactory(new PropertyValueFactory<>("BusDriverID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("Dob"));
        colCNIC.setCellValueFactory(new PropertyValueFactory<>("Cnic"));

        // Get bus driver details from the controller
        ReturnListUtility<BusDriver> returnData = taoController.getBusDriverDetails();

        if (returnData.isSuccess()) {
            // Convert data to ObservableList
            ObservableList<BusDriver> busDriverList = FXCollections.observableArrayList(returnData.getList().values());

            // Set the data into the TableView
            busDriverTable.setItems(busDriverList);
        } else {
            // Handle the error (log or show a message)
            System.out.println("Error loading bus drivers: " + returnData.getMessage());
            busDriverTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }

}
