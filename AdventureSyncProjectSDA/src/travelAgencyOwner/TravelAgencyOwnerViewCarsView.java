package travelAgencyOwner;

import java.io.IOException;

import dbHandlers.ReturnListUtility;
import hotelModels.hotelOwnerController;
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
import travelAgencyModels.Car;
import travelAgencyModels.travelAgencyOwnerController;

public class TravelAgencyOwnerViewCarsView {
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
	private TableView<Car> carTable;
	@FXML
	private TableColumn<Car, String> colCarId, colModel, colBrand, colYear, colPlateNo, colRentalFee, colCost;
	
	Parent root;
	travelAgencyOwnerController taoController;
	public TravelAgencyOwnerViewCarsView() {
    
		FXMLLoader loader = new FXMLLoader(getClass().getResource("travelAgencyOwnerViewsCars.fxml"));
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
		loadCarTable();
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
    	updateButton.setOnMouseClicked(createButtonHandler(TravelAgencyUpdatesCarView.class, "Update Car"));
    	deleteButton.setOnMouseClicked(createButtonHandler(TravelAgencyDeleteCarsView.class, "Delete Car"));
        backButton.setOnMouseClicked(createButtonHandler(TravelAgencyManageCarsView.class, "Manage Car"));
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
    public void loadCarTable() {
        // Initialize table columns
        colCarId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("Model"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("Year"));
        colPlateNo.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));
        colRentalFee.setCellValueFactory(new PropertyValueFactory<>("RentalFee"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("CostPerKm"));

        // Get car details from the controller
        ReturnListUtility<Car> returnData = taoController.getCarDetails();

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<Car> carList = FXCollections.observableArrayList(returnData.getList().values());
            carTable.setItems(carList); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
            System.out.println("Error loading cars: " + returnData.getMessage());
            carTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
	
}
