package tourist;

import java.io.IOException;

import accountAndPersonModels.Tourist;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
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
import travelAgencyModels.Car;
import travelAgencyModels.TouristController;
import travelAgencyModels.travelAgencyOwnerController;

public class TouristReturnCarView {
	@FXML
	private TextField carIdInput;
	@FXML
	private TextField kmsTravelledInput;
	@FXML
	private Button returnButton;
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
	private TableView<Car> carTable;
	@FXML
	private TableColumn<Car, String> colCarId, colModel, colBrand, colYear, colPlateNo, colRentalFee, colCost;
	private int touristID;
	
	Parent root;
	TouristController tController;
	travelAgencyOwnerController toaController;
	
	public TouristReturnCarView(int id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristReturnCars.fxml"));
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
		displayOwnerDetails();
		loadCarTable();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		carIdInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		kmsTravelledInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> rentButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(carIdInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for car ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			if(!isNumeric(kmsTravelledInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for number of kms travelled."); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int carID=Integer.parseInt(carIdInput.getText());
			int nKms=Integer.parseInt(kmsTravelledInput.getText());
						
			ReturnObjectUtility<Car> returnData= toaController.updateCarRentalStatus(carID, false);
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
				ReturnObjectUtility<Integer> returnData2=tController.removeCarFromRentedCars(touristID, carID);	
				success=returnData2.isSuccess();
				Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
				    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
				    alert.setHeaderText(null);
				    alert.setContentText(returnData2.getMessage());
				    alert.showAndWait();
				    
				if(!success)
					toaController.updateCarRentalStatus(carID, true);   
					//if transaction could not be made, set rental status as true i.e it is still rented.
				else  {
					int transactionID=returnData2.getObject();
	                try {
	                    // Dynamically load and show the TouristPaymentView
	                	Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	                    currentStage.close();
	                    createButtonHandler(TouristPaymentView.class, "Payment Gateway"/*, touristID,carID, "Return", transactionID,nKms*/).handle(null);;
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
			}
		};
			
		returnButton.setOnAction(rentButtonHandler);
		backButton.setOnMouseClicked(createButtonHandler(TouristTravelServicesMenuView.class, "Travel Services"));
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
	        !carIdInput.getText().trim().isEmpty()&&
	        !kmsTravelledInput.getText().trim().isEmpty();

		   returnButton.setDisable(!allFieldsFilled);
	}
	private <T> EventHandler<MouseEvent> createButtonHandler(Class<T> viewObject, String stageTitle/*, Object... params*/) {
        return event -> {
            try {
//            	Class<?>[] paramTypes = new Class<?>[params.length];
//                for (int i = 0; i < params.length; i++) {
//                    paramTypes[i] = params[i].getClass();
//                }
//                java.lang.reflect.Constructor<T> constructor = viewObject.getConstructor(paramTypes);

            	
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
        ReturnListUtility<Car> returnData = toaController.getTouristRentedCarDetails(touristID);

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
