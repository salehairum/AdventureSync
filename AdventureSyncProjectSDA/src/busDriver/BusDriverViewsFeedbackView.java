package busDriver;

import java.io.IOException;

import application.Feedback;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
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
import travelAgencyModels.FeedbackWithBusID;
import travelAgencyModels.busDriverController;

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
	
	Parent root;
	busDriverController bdController;
	
	public BusDriverViewsFeedbackView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriver/busDriverViewsFeedback.fxml"));
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
		eventHandlersAssignment();
		loadCommentTable();
		loadRatingLabel();
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = bdController.getBusDriverProfileDetail(1);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        menuButton.setOnMouseClicked(createButtonHandler(BusDriverMenuView.class, "Menu"));
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
    public void loadCommentTable() {
        // Initialize table columns
        colComment.setCellValueFactory(new PropertyValueFactory<>("Comment"));

        // Get car details from the controller
        ReturnListUtility<Feedback> returnData = bdController.retrieveFeedbackList(1);

        if (returnData.isSuccess()) {
            // Convert HashMap to ObservableList
            ObservableList<Feedback> feedback = FXCollections.observableArrayList(returnData.getList().values());
            commentTable.setItems(feedback); // Set data to the table
        } else {
            // Handle the error (e.g., log or show a message)
            System.out.println("Error loading bus: " + returnData.getMessage());
            commentTable.setItems(FXCollections.observableArrayList()); // Set an empty list in case of failure
        }
    }
    public void loadRatingLabel() {
        // Initialize table columns
    	ReturnObjectUtility<Float> overallRating = bdController.getOverallRating(1);
        ratingLabel.setText(Float.toString(overallRating.getObject()));
    }
}
