package busDriverView;

import java.io.IOException;

import dbHandlers.BusDBHandler;
import controllers.hotelOwnerController;
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
import controllers.busDriverController;

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
	
	private int busDriverID;
	private int busID;
	
	public BusDriverMenuView(Integer bID) {
		busDriverID = bID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriverView/busDriverMenu.fxml"));
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
		assignBusID();
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
        tourLogo.setOnMouseClicked(createButtonHandler(BusDriverViewsTourDetailsView.class, "View Tour Detail", busDriverID));
        tourLabel.setOnMouseClicked(createButtonHandler(BusDriverViewsTourDetailsView.class, "View Tour Detail", busDriverID));
        completeLogo.setOnMouseClicked(createButtonHandler(BusDriverCompletesTourView.class, "Complete Tour", busDriverID));
        completeLabel.setOnMouseClicked(createButtonHandler(BusDriverCompletesTourView.class, "Complete Tour", busDriverID));
        busLogo.setOnMouseClicked(createButtonHandler(BusDriverManageBusView.class, "Manage Bus", busDriverID));
        busLabel.setOnMouseClicked(createButtonHandler(BusDriverManageBusView.class, "Manage Bus", busDriverID));
        ratingLogo.setOnMouseClicked(createButtonHandler(BusDriverViewsFeedbackView.class, "View Feedback", busDriverID));
        ratingLabel.setOnMouseClicked(createButtonHandler(BusDriverViewsFeedbackView.class, "View Feedback", busDriverID));
        accountLogo.setOnMouseClicked(createButtonHandler(BusDriverMgrAccountView.class, "Manage Account", busDriverID));
        accountLabel.setOnMouseClicked(createButtonHandler(BusDriverMgrAccountView.class, "Manage Account", busDriverID));
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
