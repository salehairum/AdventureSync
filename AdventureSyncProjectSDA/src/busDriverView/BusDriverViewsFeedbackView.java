package busDriverView;

import java.io.IOException;

import application.Feedback;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
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
import dataUtilityClasses.FeedbackWithBusID;
import controllers.busDriverController;

public class BusDriverViewsFeedbackView {
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
	private Button menuButton;
	@FXML
	private TableView<Feedback> commentTable;
	@FXML
	private TableColumn<Feedback, String> colComment;
	@FXML
	private Text ratingLabel;
	@FXML
	private Text msgText;
	
	Parent root;
	busDriverController bdController;
	
	private int busDriverID;
	private int busID;
	
	public BusDriverViewsFeedbackView(Integer bID) {
		busDriverID = bID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriverView/busDriverViewsFeedback.fxml"));
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
		bdController = new busDriverController();
		displayOwnerDetails();
		assignBusID();
		eventHandlersAssignment();
		loadCommentTable();
		loadRatingLabel();
	}
	 public void assignBusID(){
		 busID=bdController.retrieveBusByDriverID(busDriverID).getObject();
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = bdController.getBusDriverProfileDetail(busDriverID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        menuButton.setOnMouseClicked(createButtonHandler(BusDriverMenuView.class, "Bus Driver Menu", busDriverID));
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
    public void loadCommentTable() {
        // Initialize table columns
        colComment.setCellValueFactory(new PropertyValueFactory<>("Comment"));

        // Get car details from the controller
        ReturnListUtility<Feedback> returnData = bdController.retrieveFeedbackList(busID);

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<Feedback> feedback = FXCollections.observableArrayList(returnData.getList().values());
            commentTable.setItems(feedback); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
        	msgText.setVisible(true);
        	msgText.setText(returnData.getMessage());
            commentTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
    public void loadRatingLabel() {
        // Initialize table columns
    	ReturnObjectUtility<Float> overallRating = bdController.getOverallRating(busID);
        ratingLabel.setText(Float.toString(overallRating.getObject()));
    }
}
