package hotelOwnerView;

import java.io.IOException;

import controllers.hotelOwnerController;
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
	private int hOwnerID;
	public HotelOwnerMenuView(Integer hID) {
		hOwnerID = hID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwnerView/HotelOwnerMenu.fxml"));
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
        String profileDetail[] = hoContoller.getHotelOwnerProfileDetail(hOwnerID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
	    // Using a custom handler factory method
	    roomLogo.setOnMouseClicked(createButtonHandler(HOMManageRoom.class, "Manage Room", hOwnerID));
	    roomLabel.setOnMouseClicked(createButtonHandler(HOMManageRoom.class, "Manage Room", hOwnerID));
	    kitchenLogo.setOnMouseClicked(createButtonHandler(HOMManageKitchen.class, "Manage Kitchen", hOwnerID));
	    kitchenLabel.setOnMouseClicked(createButtonHandler(HOMManageKitchen.class, "Manage Kitchen", hOwnerID));
	    ratingLogo.setOnMouseClicked(createButtonHandler(HOMViewFeedback.class, "View Feedback", hOwnerID));
	    ratingLabel.setOnMouseClicked(createButtonHandler(HOMViewFeedback.class, "View Feedback", hOwnerID));
	    accountLogo.setOnMouseClicked(createButtonHandler(HOMManageAccount.class, "Manage Account", hOwnerID));
	    accountLabel.setOnMouseClicked(createButtonHandler(HOMManageAccount.class, "Manage Account", hOwnerID));
	    hotelLogo.setOnMouseClicked(createButtonHandler(HOMManageHotel.class, "Manage Hotel", hOwnerID));
	    hotelLabel.setOnMouseClicked(createButtonHandler(HOMManageHotel.class, "Manage Hotel", hOwnerID));
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

}
