package travelAgencyOwnerView;

import java.io.IOException;

import controllers.travelAgencyOwnerController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TravelAgencyOwnerMenuView extends AnchorPane{
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
	private ImageView carLogo;
	@FXML 
	private Text carLabel;
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
	travelAgencyOwnerController taoController;
	private int tOwnerID;
	
	public TravelAgencyOwnerMenuView(Integer id) {
		tOwnerID = id;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwnerView/travelAgencyOwnerMenu.fxml"));
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
    	carLogo.setOnMouseClicked(createButtonHandler(TravelAgencyManageCarsView.class, "Manage Car", tOwnerID));
        carLabel.setOnMouseClicked(createButtonHandler(TravelAgencyManageCarsView.class, "Manage Car", tOwnerID));
        busLogo.setOnMouseClicked(createButtonHandler(TravelAgencyManageBusView.class, "Manage Bus", tOwnerID));
        busLabel.setOnMouseClicked(createButtonHandler(TravelAgencyManageBusView.class, "Manage Bus", tOwnerID));
        ratingLogo.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerViewsFeedbackView.class, "View Feedback", tOwnerID));
        ratingLabel.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerViewsFeedbackView.class, "View Feedback", tOwnerID));
        accountLogo.setOnMouseClicked(createButtonHandler(TravelAgencyManageAccountView.class, "Manage Account", tOwnerID));
        accountLabel.setOnMouseClicked(createButtonHandler(TravelAgencyManageAccountView.class, "Manage Account", tOwnerID));
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
