package travelAgencyOwnerView;

import java.io.IOException;

import application.Feedback;
import controllers.travelAgencyOwnerController;
import dataUtilityClasses.FeedbackWithBusID;
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
import travelAgencyModels.Bus;

public class TravelAgencyOwnerViewsFeedbackView {
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
	private TableView<FeedbackWithBusID> commentTable;
	@FXML
	private TableColumn<FeedbackWithBusID, String> colBusId, colComment;
	@FXML
	private Text ratingLabel;
	
	Parent root;
	travelAgencyOwnerController taoController;
<<<<<<< HEAD
	public TravelAgencyOwnerViewsFeedbackView() {
=======
	
	private int tOwnerID;
	
	public TravelAgencyOwnerViewsFeedbackView(Integer id) {
		tOwnerID = id;
>>>>>>> 69e3e1a5ece4688ce91e725d7ef42086b7887192
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwnerView/travelAgencyOwnerViewFeedback.fxml"));
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
		loadCommentTable();
		loadRatingLabel();
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(tOwnerID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
 // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        menuButton.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerMenuView.class, "Travel Agency Owner Menu", tOwnerID));
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
        colBusId.setCellValueFactory(new PropertyValueFactory<>("BusID"));
        colComment.setCellValueFactory(new PropertyValueFactory<>("Comments"));

        // Get car details from the controller
        ReturnListUtility<FeedbackWithBusID> returnData = taoController.getFeedbackDetailsWithBusID();

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<FeedbackWithBusID> feedbackWithBusIDList = FXCollections.observableArrayList(returnData.getList().values());
            commentTable.setItems(feedbackWithBusIDList); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
            System.out.println("Error loading bus: " + returnData.getMessage());
            commentTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
    public void loadRatingLabel() {
        // Initialize table columns
    	ReturnObjectUtility<Float> overallRating = taoController.getOverallRating();
        ratingLabel.setText(Float.toString(overallRating.getObject()));
    }
}
