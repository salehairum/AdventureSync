package signupForms;

import java.io.IOException;

import controllers.hotelOwnerController;
import hotelOwnerView.HOMManageAccount;
import hotelOwnerView.HOMManageHotel;
import hotelOwnerView.HOMManageKitchen;
import hotelOwnerView.HOMManageRoom;
import hotelOwnerView.HOMViewFeedback;
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

public class AccountMenuView {
	@FXML
	private Pane sidePanel;
	@FXML
	private Pane mainPanel;
	@FXML
	private ImageView touristLogo;
	@FXML
	private Text touristLabel;
	@FXML
	private ImageView hotelLogo;
	@FXML
	private Text hotelLabel;
	@FXML
	private ImageView driverLogo;
	@FXML
	private Text driverLabel;
	@FXML
	private ImageView travelLogo;
	@FXML
	private Text travelLabel;
	Parent root;
	
	public AccountMenuView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupForms/accountMenu.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	@FXML
	private void initialize() {
		eventHandlersAssignment();
	}
	public Parent getRoot() {
		return root;
	}
	// Method for button handling
    public void eventHandlersAssignment() {
	    // Using a custom handler factory method
	    touristLogo.setOnMouseClicked(createButtonHandler(TouristSignUpView.class, "Tourist Sign Up"));
	    touristLabel.setOnMouseClicked(createButtonHandler(TouristSignUpView.class, "Tourist Sign Up"));
	    driverLogo.setOnMouseClicked(createButtonHandler(BusDriverSignUpView.class, "Driver Sign Up"));
	    driverLabel.setOnMouseClicked(createButtonHandler(BusDriverSignUpView.class, "Driver Sign Up"));
	    travelLogo.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerSignUpView.class, "Travel Agency Owner Sign Up"));
	    travelLabel.setOnMouseClicked(createButtonHandler(TravelAgencyOwnerSignUpView.class, "Travel Agency Owner Sign Up"));
	    hotelLogo.setOnMouseClicked(createButtonHandler(HotelOwnerSignUpView.class, "Hotel Owner Sign Up"));
	    hotelLabel.setOnMouseClicked(createButtonHandler(HotelOwnerSignUpView.class, "Hotel Owner Sign Up"));
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
