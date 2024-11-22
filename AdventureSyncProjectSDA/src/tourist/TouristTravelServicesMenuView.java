package tourist;

import java.io.IOException;

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
import travelAgencyModels.TouristController;

public class TouristTravelServicesMenuView {
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
	private ImageView rentLogo;
	@FXML
	private Text rentLabel;
	@FXML
	private ImageView returnLogo;
	@FXML
	private Text returnLabel;
	@FXML
	private ImageView busLogo;
	@FXML
	private Text busLabel;
	@FXML
	private ImageView ratingLogo;
	@FXML
	private Text ratingLabel;
	@FXML
	private Button menuButton;
	Parent root;
	TouristController tController;
	
	public TouristTravelServicesMenuView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristTravelServicesMenu.fxml"));
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
		tController = new TouristController();
		displayOwnerDetails();
		eventHandlersAssignment();
	}
	public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
		menuButton.setOnMouseClicked(createButtonHandler(TouristMenuView.class, "Menu"));
		rentLogo.setOnMouseClicked(createButtonHandler(TouristRentCarView.class, "Rent Car")); //pass tourist ID
		rentLabel.setOnMouseClicked(createButtonHandler(TouristRentCarView.class, "Rent Car"));
		returnLogo.setOnMouseClicked(createButtonHandler(TouristReturnCarView.class, "Return Car"));
		returnLabel.setOnMouseClicked(createButtonHandler(TouristReturnCarView.class, "Return Car"));
		busLogo.setOnMouseClicked(createButtonHandler(TouristBooksSeatView.class, "Book Seat"));
		busLabel.setOnMouseClicked(createButtonHandler(TouristBooksSeatView.class, "Book Seat"));
		ratingLogo.setOnMouseClicked(createButtonHandler(TouristRatesBusTourView.class, "Bus Feedback"));
		ratingLabel.setOnMouseClicked(createButtonHandler(TouristRatesBusTourView.class, "Bus Feedback"));
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
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(1);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
}
