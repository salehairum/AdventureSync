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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TravelAgencyManageBusView {
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
	private ImageView viewBusLogo;
	@FXML 
	private Text viewBusLabel;
	@FXML 
	private ImageView viewDriversLogo;
	@FXML 
	private Text viewDriversLabel;
	@FXML 
	private ImageView assignTourLogo;
	@FXML 
	private Text assignTourLabel;
	
	Parent root;
	travelAgencyOwnerController taoController;
	public TravelAgencyManageBusView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwnerView/travelAgencyOwnerMgrBuses.fxml"));
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
    	viewBusLogo.setOnMouseClicked(createButtonHandler(TravelAgencyViewBusesView.class, "View Bus"));
    	viewBusLabel.setOnMouseClicked(createButtonHandler(TravelAgencyViewBusesView.class, "View Bus"));
    	//viewDriversLogo.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerDeleteAccountView.class, "View Bus Driver"));
    	//viewDriversLabel.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerDeleteAccountView.class, "View Bus Driver"));
    	assignTourLogo.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerAssignsTourToBusView.class, "Assign Tour"));
    	assignTourLabel.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerAssignsTourToBusView.class, "Assign Tour"));
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
