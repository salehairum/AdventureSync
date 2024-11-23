package tourist;

import java.io.IOException;

import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
import hotelModels.Hotel;
import hotelModels.hotelOwnerController;
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
import travelAgencyModels.TouristController;

public class TouristOrdersFoodView {
	@FXML
	private TextField hotelIDInput;
	@FXML
	private Button selectHotelButton;
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
	private TableView<Hotel> hotelTable;
	@FXML
	private TableColumn<Hotel, String> colHotelId, colHotelName, colHotelLoc;
	private int touristID;
	
	Parent root;
	TouristController tController;
	hotelOwnerController hController;
	
	public TouristOrdersFoodView(Integer id) {
		touristID=id;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristOrdersFood.fxml"));
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
		loadHotelTable();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		hotelIDInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	//afsah tumne yahan doosre form par lekar jana at the end-->this is just like book seat
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> selectBusButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(hotelIDInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for hotel ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}
			
			int hotelID=Integer.parseInt(hotelIDInput.getText());
						
			ReturnObjectUtility<Hotel> returnData=tController.retrieveHotelObject(hotelID);
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
		    alert.setHeaderText(null);
		    alert.setContentText(returnData.getMessage());
		    alert.showAndWait();
		    
		    if(success) {
		    	//go to next form
		    	//just send poora hotel since ab woh rooms aa chuke hain uskay 
		    	//pass this room
		    	Hotel hotel=returnData.getObject();
		    	try {
		            // Create an instance of the controller using its constructor
		            TouristSelectsFoodFromMenuView controllerInstance = new TouristSelectsFoodFromMenuView(touristID, hotel);

		            // Get the root node from the controller
		            Parent root = controllerInstance.getRoot();

		            // Create a new scene and stage for the new form
		            Scene newFormScene = new Scene(root);
		            Stage newFormStage = new Stage();
		            newFormStage.setScene(newFormScene);
		            newFormStage.setTitle("Select Food");

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
			
		selectHotelButton.setOnAction(selectBusButtonHandler);
		backButton.setOnMouseClicked(createButtonHandler(TouristHotelServicesMenuView.class,"Hotel Services", touristID));
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
	        !hotelIDInput.getText().trim().isEmpty();

	    selectHotelButton.setDisable(!allFieldsFilled);
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(touristID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    public void loadHotelTable() {
        // Initialize table columns
    	colHotelId.setCellValueFactory(new PropertyValueFactory<>("HotelID"));
        colHotelName.setCellValueFactory(new PropertyValueFactory<>("HotelName"));
        colHotelLoc.setCellValueFactory(new PropertyValueFactory<>("Location"));

        // Get car details from the controller
        ReturnListUtility<Hotel> returnData = tController.getHotelDetails();

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<Hotel> hotelList = FXCollections.observableArrayList(returnData.getList().values());
            hotelTable.setItems(hotelList); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
            System.out.println("Error loading hotel: " + returnData.getMessage());
            hotelTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
}
