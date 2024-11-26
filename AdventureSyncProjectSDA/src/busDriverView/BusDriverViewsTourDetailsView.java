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
import dataUtilityClasses.ReturnObjectUtility;

public class BusDriverViewsTourDetailsView {
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
	private Text tourIDLabel;
	@FXML
	private Text dateLabel;
	@FXML
	private Text originLabel;
	@FXML
	private Text destinationLabel;
	@FXML
	private Text msgText;
	
	Parent root;
	busDriverController bdController;

	private int busDriverID;
	private int busID;
	
	public BusDriverViewsTourDetailsView(Integer bID) {
		busDriverID = bID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriverView/busDriverViewTour.fxml"));
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
		displayTourDetail();
	}
	
	public void assignBusID(){
		 busID=bdController.retrieveBusByDriverID(busDriverID).getObject();
	}
	
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = bdController.getBusDriverProfileDetail(busDriverID);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    public void displayTourDetail() {
        ReturnObjectUtility<String[]> returnData=bdController.getBusTourDetail(busID);
        String tourDetail[] = returnData.getObject();
        if(returnData.isSuccess())
        {
        	tourIDLabel.setText(tourDetail[0]);
        	originLabel.setText(tourDetail[1]);
        	destinationLabel.setText(tourDetail[2]);
        	dateLabel.setText(tourDetail[3]);
        }
        else
        {
            // Handle the error (e.g., log or show a message)
        	msgText.setVisible(true);
        	msgText.setText(returnData.getMessage());
        	tourIDLabel.setText("Tour ID");
        	originLabel.setText("Origin");
        	destinationLabel.setText("Destination");
        	dateLabel.setText("Date");
        }
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
        // Assign handlers with parameters for specific FXMLs and classes
        backButton.setOnMouseClicked(createButtonHandler(BusDriverMenuView.class, "Bus Driver Menu", busDriverID));
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
