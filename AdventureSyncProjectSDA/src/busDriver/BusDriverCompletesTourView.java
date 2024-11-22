package busDriver;

import java.io.IOException;

import dbHandlers.ReturnObjectUtility;
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
import travelAgencyModels.busDriverController;

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
	
	Parent root;
	busDriverController bdController;
	int busDriverID;
	
	public BusDriverCompletesTourView(int id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriver/busDriverCompletesTour.fxml"));
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
		tourIDText.setText(Integer.toString(getTourID()));
	}
	// Method to display profile
    public void displayOwnerDetails() {
        String profileDetail[] = bdController.getBusDriverProfileDetail(1);
        name.setText(profileDetail[0]);
        id.setText(profileDetail[1]);
        cnic.setText(profileDetail[2]);
        dob.setText(profileDetail[3]);
    }
    
    // Method for button handling
    public void eventHandlersAssignment() {
    	 EventHandler<ActionEvent> yesButtonHandler = (event) -> {
 	        // Create a single alert instance to avoid repeated showAndWait() calls

    		ReturnObjectUtility<Boolean> returnData=bdController.completeTour(getTourID());
 	        boolean success = returnData.isSuccess();
 	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
 	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
 	        alert.setHeaderText(null);
 	        alert.setContentText(returnData.getMessage());
 	        alert.showAndWait();
 	    };
 	   yesButton.setOnAction(yesButtonHandler);
    	
        menuButton.setOnMouseClicked(createButtonHandler(BusDriverMenuView.class, "Bus Driver Menu"));
        viewDetailsButton.setOnMouseClicked(createButtonHandler(BusDriverViewsTourDetailsView.class, "View Tour Detail"));
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
    
    public int getTourID() {
    	ReturnObjectUtility<Integer> returnData=bdController.retrieveTourID(busDriverID);
    	boolean success=returnData.isSuccess();
    	if(!success) {
    		Alert alert = new Alert(AlertType.ERROR);
		    alert.setTitle("Operation Failed");
		    alert.setHeaderText(null);
		    alert.setContentText(returnData.getMessage());
		    alert.showAndWait();
		    return 0;
    	}
    	return returnData.getObject();
    }
}
