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

public class HOMManageAccount {
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
	private ImageView updAccountLogo;
	@FXML
	private Text updAccountLabel;
	@FXML
	private ImageView delAccountLogo;
	@FXML
	private Text delAccountLabel;
	@FXML
	private Button menuButton;
	
	Parent root;
	hotelOwnerController hoContoller;
	
	private int hotelID;
	private int hotelOwnerID;
	
	public HOMManageAccount(Integer hID) {
		hotelOwnerID = hID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelOwnerView/HOMManageAccount.fxml"));
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
		assignHotelID();
	}
	public void assignHotelID(){
		hotelID=hoContoller.getHotelID(hotelOwnerID).getObject();
	}
	public Parent getRoot() {
		return root;
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = hoContoller.getHotelOwnerProfileDetail(1);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        menuButton.setOnMouseClicked(createButtonHandler(HotelOwnerMenuView.class, "Menu", hotelOwnerID));
        updAccountLogo.setOnMouseClicked(createButtonHandler(hotelOwnerUpdateAccount.class, "Update Account", hotelOwnerID));
        updAccountLabel.setOnMouseClicked(createButtonHandler(hotelOwnerUpdateAccount.class, "Update Account", hotelOwnerID));
        delAccountLogo.setOnMouseClicked(createButtonHandler(hotelOwnerDeleteAccount.class, "Delete Account", hotelOwnerID));
        delAccountLabel.setOnMouseClicked(createButtonHandler(hotelOwnerDeleteAccount.class, "Delete Account", hotelOwnerID));
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
