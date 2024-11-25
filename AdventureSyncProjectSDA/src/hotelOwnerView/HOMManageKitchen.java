package hotelOwnerView;

import java.io.IOException;

import controllers.hotelOwnerController;
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

public class HOMManageKitchen {
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
	private ImageView addFoodLogo;
	@FXML
	private Text addFoodLabel;
	@FXML
	private ImageView delFoodLogo;
	@FXML
	private Text delFoodLabel;
	@FXML
	private ImageView updFoodLogo;
	@FXML
	private Text updFoodLabel;
	@FXML
	private ImageView viewFoodLogo;
	@FXML
	private Text viewFoodLabel;
	@FXML
	private Button menuButton;
	
	Parent root;
	hotelOwnerController hoContoller;
	private int hotelOwnerID;
	private int hotelID;
	public HOMManageKitchen(Integer hID) {
		hotelOwnerID = hID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwnerView/HOMManageKitchen.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void assignHotelID(){
		hotelID=hoContoller.getHotelID(hotelOwnerID).getObject();
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
        String profileDetail[] = hoContoller.getHotelOwnerProfileDetail(hotelOwnerID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        menuButton.setOnMouseClicked(createButtonHandler(HotelOwnerMenuView.class, "Hotel Owner Menu", hotelOwnerID));
        addFoodLogo.setOnMouseClicked(createButtonHandler(HOMAddFood.class, "Add Food", hotelOwnerID));
        addFoodLabel.setOnMouseClicked(createButtonHandler(HOMAddFood.class, "Add Food", hotelOwnerID));
        delFoodLogo.setOnMouseClicked(createButtonHandler(HOMDeleteFood.class, "Delete Food", hotelOwnerID));
        delFoodLabel.setOnMouseClicked(createButtonHandler(HOMDeleteFood.class, "Delete Food", hotelOwnerID));
        viewFoodLogo.setOnMouseClicked(createButtonHandler(HOMViewFood.class, "View Food", hotelOwnerID));
        viewFoodLabel.setOnMouseClicked(createButtonHandler(HOMViewFood.class, "View Food", hotelOwnerID));
        updFoodLogo.setOnMouseClicked(createButtonHandler(HOMUpdateFood.class, "Update Food", hotelOwnerID));
        updFoodLabel.setOnMouseClicked(createButtonHandler(HOMUpdateFood.class, "Update Food", hotelOwnerID));
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
