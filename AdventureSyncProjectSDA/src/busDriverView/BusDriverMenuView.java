package busDriverView;

import java.io.IOException;

import controllers.busDriverController;
import controllers.hotelOwnerController;
import dbHandlers.BusDBHandler;
import hotelOwnerView.HOMManageKitchen;
import hotelOwnerView.HotelOwnerMenuView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BusDriverMenuView {
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
	private ImageView tourLogo;
	@FXML
	private Text tourLabel;
	@FXML
	private ImageView completeLogo;
	@FXML
	private Text completeLabel;
	@FXML
	private ImageView busLogo;
	@FXML
	private Text busLabel;
	@FXML
	private ImageView ratingLogo;
	@FXML
	private Text ratingLabel;
	@FXML
	private ImageView accountLogo;
	@FXML
	private Text accountLabel;
	
	Parent root;
	busDriverController bdController;
	public BusDriverMenuView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriver/busDriverMenu.fxml"));
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
        tourLogo.setOnMouseClicked(createButtonHandler(BusDriverViewsTourDetailsView.class, "View Tour Detail"));
        tourLabel.setOnMouseClicked(createButtonHandler(BusDriverViewsTourDetailsView.class, "View Tour Detail"));
        completeLogo.setOnMouseClicked(createButtonHandler(BusDriverCompletesTourView.class, "Complete Tour"));
        completeLabel.setOnMouseClicked(createButtonHandler(BusDriverCompletesTourView.class, "Complete Tour"));
        busLogo.setOnMouseClicked(createButtonHandler(BusDriverManageBusView.class, "Manage Bus"));
        busLabel.setOnMouseClicked(createButtonHandler(BusDriverManageBusView.class, "Manage Bus"));
        ratingLogo.setOnMouseClicked(createButtonHandler(BusDriverViewsFeedbackView.class, "View Feedback"));
        ratingLabel.setOnMouseClicked(createButtonHandler(BusDriverViewsFeedbackView.class, "View Feedback"));
        accountLogo.setOnMouseClicked(createButtonHandler(BusDriverMgrAccountView.class, "Manage Account"));
        accountLabel.setOnMouseClicked(createButtonHandler(BusDriverMgrAccountView.class, "Manage Account"));
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
}
