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
	public TravelAgencyOwnerMenuView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerMenu.fxml"));
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
        String profileDetail[] = taoController.getTravelAgencyOwnerProfileDetail(1);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
 // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
    	carLogo.setOnMouseClicked(createButtonHandler(TravelAgencyManageCarsView.class, "Manage Car"));
        carLabel.setOnMouseClicked(createButtonHandler(TravelAgencyManageCarsView.class, "Manage Car"));
        busLogo.setOnMouseClicked(createButtonHandler(TravelAgencyManageBusView.class, "Manage Bus"));
        busLabel.setOnMouseClicked(createButtonHandler(TravelAgencyManageBusView.class, "Manage Bus"));
        ratingLogo.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerViewsFeedbackView.class, "View Feedback"));
        ratingLabel.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerViewsFeedbackView.class, "View Feedback"));
        accountLogo.setOnMouseClicked(createButtonHandler(TravelAgencyManageAccountView.class, "Manage Account"));
        accountLabel.setOnMouseClicked(createButtonHandler(TravelAgencyManageAccountView.class, "Manage Account"));
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
