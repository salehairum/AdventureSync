package busDriverView;

import java.io.IOException;

import dataUtilityClasses.ReturnObjectUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Bus;
import controllers.busDriverController;

public class BusDriverCompletesTourView {
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
	private Button viewDetailsButton;
	@FXML
	private Button yesButton;
	@FXML
	private Text tourIDText;
	@FXML
	private Text msgText;
	
	Parent root;
	busDriverController bdController;

	private int busDriverID;
	private int busID;
	private int tourID;

	public BusDriverCompletesTourView(Integer id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriverView/busDriverCompletesTour.fxml"));
		loader.setController(this);
		busDriverID=id;
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
		setTourID();
		assignBusID();
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = bdController.getBusDriverProfileDetail(busDriverID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    public void assignBusID(){
		 busID=bdController.retrieveBusByDriverID(busDriverID).getObject();
	}
    // Method for button handling
    public void eventHandlersAssignment() {
    	 EventHandler<ActionEvent> yesButtonHandler = (event) -> {
 	        // Create a single alert instance to avoid repeated showAndWait() calls

    		ReturnObjectUtility<Boolean> returnData=bdController.completeTour(tourID);
 	        boolean success = returnData.isSuccess();
 	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
 	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
 	        alert.setHeaderText(null);
 	        alert.setContentText(returnData.getMessage());
 	        alert.showAndWait();
 	    };
 	    yesButton.setOnAction(yesButtonHandler);
        menuButton.setOnMouseClicked(createButtonHandler(BusDriverMenuView.class, "Bus Driver Menu", busDriverID));
        viewDetailsButton.setOnMouseClicked(createButtonHandler(BusDriverViewsTourDetailsView.class, "View Tour Detail", busDriverID));
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
    
    public void setTourID() {
    	ReturnObjectUtility<Integer> returnData=bdController.retrieveTourID(busDriverID);
    	boolean success=returnData.isSuccess();
    	if(!success) {
    		msgText.setText("Currently no tour has been assigned to your bus");
    		tourIDText.setVisible(false);
    		tourID=0;
    		viewDetailsButton.setDisable(true);
    		yesButton.setDisable(true);
    	}
    	else
    	{
    		tourID=returnData.getObject();
    		tourIDText.setText(Integer.toString(returnData.getObject()));
    	}
    }
}