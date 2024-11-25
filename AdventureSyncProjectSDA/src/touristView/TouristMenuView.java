package touristView;

import java.io.IOException;

import controllers.TouristController;
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

public class TouristMenuView {
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
	private ImageView accountLogo;
	@FXML
	private Text accountLabel;
	@FXML
	private ImageView travelLogo;
	@FXML
	private Text travelLabel;
	@FXML
	private ImageView hotelLogo;
	@FXML
	private Text hotelLabel;
	
	Parent root;
	TouristController tController;
	private int touristID;
	public TouristMenuView(Integer touristID) {
		this.touristID = touristID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristMenu.fxml"));
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
		eventHandlersAssignment();
		displayOwnerDetails();
	}
	
	public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
		accountLogo.setOnMouseClicked(createButtonHandler(TouristManagesAccountView.class, "Manage Account", touristID));
		accountLabel.setOnMouseClicked(createButtonHandler(TouristManagesAccountView.class, "Manage Account", touristID));
		hotelLogo.setOnMouseClicked(createButtonHandler(TouristHotelServicesMenuView.class, "Hotel Services", touristID));
		hotelLabel.setOnMouseClicked(createButtonHandler(TouristHotelServicesMenuView.class, "Hotel Services", touristID));
		travelLogo.setOnMouseClicked(createButtonHandler(TouristTravelServicesMenuView.class, "Travel Services", touristID));
		travelLabel.setOnMouseClicked(createButtonHandler(TouristTravelServicesMenuView.class, "Travel Services", touristID));
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

	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = tController.getTouristProfileDetail(touristID);

        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
}
