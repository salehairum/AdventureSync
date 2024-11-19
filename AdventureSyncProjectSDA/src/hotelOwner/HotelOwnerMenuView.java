package hotelOwner;

import java.io.IOException;

import hotelModels.hotelOwnerController;
import javafx.event.ActionEvent;
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

public class HotelOwnerMenuView {
	@FXML
	private Text name;
	@FXML
	private Text id;
	@FXML
	private Text cnic;
	@FXML
	private Text dob;
	@FXML
	private ImageView roomLogo;
	@FXML
	private Text roomLabel;
	@FXML
	private ImageView kitchenLogo;
	@FXML
	private Text kitchenLabel;
	@FXML
	private ImageView ratingLogo;
	@FXML
	private Text ratingLabel;
	@FXML
	private ImageView accountLogo;
	@FXML
	private Text accountLabel;
	@FXML
	private ImageView hotelLogo;
	@FXML
	private Text hotelLabel;
	
	Parent root;
	hotelOwnerController hoContoller;
	public HotelOwnerMenuView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/HotelOwnerMenu.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void initialize() {
		hoContoller = new hotelOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
	}
	
	public Parent getRoot() {
		return root;
	}

	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoContoller.getHotelOwnerProfileDetail(1);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
	    // Using a custom handler factory method
	    roomLogo.setOnMouseClicked(createButtonHandler(HOMManageRoom.class, "Manage Room"));
	    roomLabel.setOnMouseClicked(createButtonHandler(HOMManageRoom.class, "Manage Room"));
	    kitchenLogo.setOnMouseClicked(createButtonHandler(HOMManageKitchen.class, "Manage Kitchen"));
	    kitchenLabel.setOnMouseClicked(createButtonHandler(HOMManageKitchen.class, "Manage Kitchen"));
	    ratingLogo.setOnMouseClicked(createButtonHandler(HOMViewFeedback.class, "View Feedback"));
	    ratingLabel.setOnMouseClicked(createButtonHandler(HOMViewFeedback.class, "View Feedback"));
	    accountLogo.setOnMouseClicked(createButtonHandler(HOMManageAccount.class, "Manage Account"));
	    accountLabel.setOnMouseClicked(createButtonHandler(HOMManageAccount.class, "Manage Account"));
	    hotelLogo.setOnMouseClicked(createButtonHandler(HOMManageHotel.class, "Manage Hotel"));
	    hotelLabel.setOnMouseClicked(createButtonHandler(HOMManageHotel.class, "Manage Hotel"));
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
