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

public class TravelAgencyManageCarsView extends AnchorPane {
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
	private ImageView addLogo;
	@FXML 
	private Text addLabel;
	@FXML 
	private ImageView deleteLogo;
	@FXML 
	private Text deleteLabel;
	@FXML 
	private ImageView updateLogo;
	@FXML 
	private Text updateLabel;
	@FXML 
	private ImageView viewLogo;
	@FXML 
	private Text viewLabel;
	
	Parent root;
	travelAgencyOwnerController taoController;
	public TravelAgencyManageCarsView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerMgrCars.fxml"));
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
    	addLogo.setOnMouseClicked(createButtonHandler(TravelAgencyAddCarView.class, "Add Car"));
    	addLabel.setOnMouseClicked(createButtonHandler(TravelAgencyAddCarView.class, "Add Car"));
    	deleteLogo.setOnMouseClicked(createButtonHandler(TravelAgencyDeleteCarsView.class, "Delete Car"));
    	deleteLabel.setOnMouseClicked(createButtonHandler(TravelAgencyDeleteCarsView.class, "Delete Car"));
    	updateLogo.setOnMouseClicked(createButtonHandler(TravelAgencyUpdatesCarView.class, "Update Car"));
    	updateLabel.setOnMouseClicked(createButtonHandler(TravelAgencyUpdatesCarView.class, "Update Car"));
    	viewLogo.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerViewCarsView.class, "View Car"));
    	viewLabel.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerViewCarsView.class, "View Car"));
        menuButton.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerMenuView.class, "Menu"));
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
