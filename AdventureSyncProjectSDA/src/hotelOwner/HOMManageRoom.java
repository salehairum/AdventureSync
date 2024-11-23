package hotelOwner;

import java.io.IOException;

import hotelModels.hotelOwnerController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HOMManageRoom {
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
	private ImageView addRoomLogo;
	@FXML
	private Text addRoomLabel;
	@FXML
	private ImageView delRoomLogo;
	@FXML
	private Text delRoomLabel;
	@FXML
	private ImageView updRoomLogo;
	@FXML
	private Text updRoomLabel;
	@FXML
	private ImageView viewRoomLogo;
	@FXML
	private Text viewRoomLabel;
	@FXML
	private Button menuButton;
	
	Parent root;
	hotelOwnerController hoContoller;
	private int hOwnerID;
	public HOMManageRoom(Integer hID) {
		hOwnerID = hID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwner/HOMManageRoom.fxml"));
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
        // Assign handlers with parameters for specific FXMLs and classes
        menuButton.setOnMouseClicked(createButtonHandler(HotelOwnerMenuView.class, "Menu", hOwnerID));
        addRoomLogo.setOnMouseClicked(createButtonHandler(HOMAddRoom.class, "Add Room", hOwnerID));
        addRoomLabel.setOnMouseClicked(createButtonHandler(HOMAddRoom.class, "Add Room", hOwnerID));
        delRoomLogo.setOnMouseClicked(createButtonHandler(HOMDeleteRoom.class, "Delete Room", hOwnerID));
        delRoomLabel.setOnMouseClicked(createButtonHandler(HOMDeleteRoom.class, "Delete Room", hOwnerID));
        viewRoomLogo.setOnMouseClicked(createButtonHandler(HOMViewRoom.class, "View Room", hOwnerID));
        viewRoomLabel.setOnMouseClicked(createButtonHandler(HOMViewRoom.class, "View Room", hOwnerID));
        updRoomLogo.setOnMouseClicked(createButtonHandler(HOMUpdateRoom.class, "Update Room", hOwnerID));
        updRoomLabel.setOnMouseClicked(createButtonHandler(HOMUpdateRoom.class, "Update Room", hOwnerID));
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
