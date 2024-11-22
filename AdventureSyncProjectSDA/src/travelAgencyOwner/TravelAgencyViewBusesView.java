package travelAgencyOwner;

import java.io.IOException;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.travelAgencyOwnerController;

public class TravelAgencyViewBusesView {
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
	private Button updateButton;
	@FXML 
	private Button deleteButton;
	@FXML 
	private Button backButton;
	@FXML
	private TableView<Bus> busTable;
	@FXML
	private TableColumn<Bus, String> colBusId, colModel, colBrand, colYear, colPlateNo, colDriverID, colHasTour;
	
	Parent root;
	travelAgencyOwnerController taoController;
	public TravelAgencyViewBusesView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("travelAgencyOwnerViewsBuses.fxml"));
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
		taoController = new travelAgencyOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
		loadBusTable();
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
    
    public void loadBusTable() {
        // Initialize table columns
        colBusId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("Year"));
        colPlateNo.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));
        colDriverID.setCellValueFactory(new PropertyValueFactory<>("BusDriverID"));
        colHasTour.setCellValueFactory(new PropertyValueFactory<>("HasTour"));

        // Get car details from the controller
        ReturnListUtility<Bus> returnData = taoController.getBusDetailsWithBusDriverID();

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
