package touristView;

import java.io.IOException;

import application.Feedback;
import controllers.TouristController;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dataUtilityClasses.RoomWithHotel;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class touristRoomFeedbackView {
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
	private TextField roomIdInput;
	@FXML
	private TextField commentsInput;
	@FXML
	private ComboBox<String> ratingInput;
	@FXML
	private Button backButton;
	@FXML
	private Button submitButton;
	@FXML
	private TableView<RoomWithHotel> roomTable;
	@FXML
	private TableColumn<RoomWithHotel, String> colRoomId, colPrice, colDesc, colHotelId, colHotelName;
	@FXML
	private Text msgText;
	
	Parent root;
	TouristController tController;
	private int touristID;
	
	public touristRoomFeedbackView(Integer id) {
		touristID=id;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/touristView/touristRoomFeedback.fxml"));
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
		listenersAssignment();
		tController = new TouristController();
		ratingInput.getItems().addAll("1", "2", "3","4","5");
		displayOwnerDetails();
		eventHandlersAssignment();
		getRoomDetails();
	}
	
	public void listenersAssignment() {
		roomIdInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		commentsInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		ratingInput.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
	
	
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> submitButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(roomIdInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for bus ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}

			Feedback feedback=createFeedbackObject();
						
			ReturnObjectUtility<Feedback> returnData=tController.giveFeedbackToRoom(feedback);
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
		    alert.setHeaderText(null);
		    alert.setContentText(returnData.getMessage());
		    alert.showAndWait();
		};
			
		submitButton.setOnAction(submitButtonHandler);
        // Assign handlers with parameters for specific FXMLs and classes
		backButton.setOnMouseClicked(createButtonHandler(TouristHotelServicesMenuView.class, "Hotel Services", touristID));
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
	
	// Feedback related methods
	public Feedback createFeedbackObject() {
	    // Extract input values
	    int serviceID = Integer.parseInt(roomIdInput.getText()); // Assuming service ID corresponds to bus ID
	    String comment = commentsInput.getText();
	    int rating = Integer.parseInt(ratingInput.getValue()); // Get selected rating value from ComboBox
	    String typeOfFeedback = "Room";
	    
	    // Create and return the Feedback object
	    return new Feedback(0, serviceID, rating, comment, typeOfFeedback, touristID);
	}

	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !roomIdInput.getText().trim().isEmpty()&&
	    !commentsInput.getText().trim().isEmpty()&&
	    ratingInput.getValue()!=null;
	    

	    submitButton.setDisable(!allFieldsFilled);
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(touristID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    public void getRoomDetails() {
        // Initialize table columns
    	colRoomId.setCellValueFactory(new PropertyValueFactory<>("RoomId"));
    	colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
    	colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
    	colHotelId.setCellValueFactory(new PropertyValueFactory<>("HotelId"));
    	colHotelName.setCellValueFactory(new PropertyValueFactory<>("HotelName"));
        // Get car details from the controller
        ReturnListUtility<RoomWithHotel> returnData = tController.getBookedRoomDetails(touristID);

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<RoomWithHotel> bookedRoomList = FXCollections.observableArrayList(returnData.getList().values());
            roomTable.setItems(bookedRoomList); // Set data to the table
        } else {
        	msgText.setVisible(true);
        	msgText.setText(returnData.getMessage());
            roomTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
}
