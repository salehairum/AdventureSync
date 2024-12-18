package busDriverView;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import controllers.busDriverController;

public class BusDriverViewBusDetailsView {
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
	private Button backButton;
	@FXML
	private Button updateButton;
	@FXML
	private Text busIDLabel;
	@FXML
	private Text brandLabel;
	@FXML
	private Text modelLabel;
	@FXML
	private Text yearLabel;
	@FXML
	private Text nSeatsLabel;
	@FXML
	private Text seatFeeLabel;
	@FXML
	private Text plateNoLabel;
	@FXML
	private Text hasTourLabel;
	
	Parent root;
	busDriverController bdController;

	private int busDriverID;
	private int busID;
	
	public BusDriverViewBusDetailsView(Integer bID) {
		busDriverID = bID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriverView/busDriverViewBus.fxml"));
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
		displayBusDetail();
	}
	public void assignBusID(){
		 busID=bdController.retrieveBusByDriverID(busDriverID).getObject();
		 //System.out.println("busID " + busID);
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = bdController.getBusDriverProfileDetail(busDriverID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    public void displayBusDetail() {
        String profileDetail[] = bdController.getBusDetail(busID);
        busIDLabel.setText(profileDetail[0]);
        brandLabel.setText(profileDetail[1]);
        modelLabel.setText(profileDetail[2]);
        yearLabel.setText(profileDetail[3]);
        nSeatsLabel.setText(profileDetail[4]);
        seatFeeLabel.setText(profileDetail[5]);
        plateNoLabel.setText(profileDetail[6]);
        hasTourLabel.setText(profileDetail[7]);
    }
    // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        backButton.setOnMouseClicked(createButtonHandler(BusDriverManageBusView.class, "Manage Bus", busDriverID));
        updateButton.setOnMouseClicked(createButtonHandler(BusDriverUpdateBusView.class, "Update Bus", busDriverID));
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
