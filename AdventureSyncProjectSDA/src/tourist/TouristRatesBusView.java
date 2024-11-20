package tourist;

import java.io.IOException;

import application.Feedback;
import dbHandlers.ReturnObjectUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Bus;
import travelAgencyModels.TouristController;

public class TouristRatesBusView {
	@FXML
	private TextField busIdInput;
	@FXML
	private TextField commentsInput;
	@FXML
	private ComboBox<String> ratingInput;
	@FXML
	private Button menuButton;
	@FXML
	private Button submitButton;
	private int touristID;
	
	Parent root;
	TouristController tController;
	
	public TouristRatesBusView(int id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristRatesBus.fxml"));
		loader.setController(this);
		touristID=id;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	@FXML
	private void initialize() {
		listenersAssignment();
		eventHandlersAssignment();
		ratingInput.getItems().addAll("1", "2", "3","4","5");
		tController = new TouristController();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//assigning buttons and listeners
	public void listenersAssignment() {
		busIdInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		commentsInput.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
		ratingInput.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
	}
		
	//afsah tumne yahan doosre form par lekar jana at the end
	public void eventHandlersAssignment() {
		EventHandler<ActionEvent> submitButtonHandler=(event)->{
			Alert alertInvalidInput = new Alert(AlertType.ERROR); 
			alertInvalidInput.setTitle("Invalid input"); 
			
			StringBuilder errorMessage = new StringBuilder();
			
			if(!isNumeric(busIdInput.getText())) {
				alertInvalidInput.setContentText("Please enter numeric value for bus ID"); 
				alertInvalidInput.showAndWait(); 
				return;
			}

			Feedback feedback=createFeedbackObject();
						
			ReturnObjectUtility<Feedback> returnData=tController.giveFeedbackToBus(feedback);
			boolean success=returnData.isSuccess();
			Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		    alert.setTitle(success ? "Operation Successful" : "Operation Failed");
		    alert.setHeaderText(null);
		    alert.setContentText(returnData.getMessage());
		    alert.showAndWait();
		};
			
		submitButton.setOnAction(submitButtonHandler);
		menuButton.setOnMouseClicked(createButtonHandler(TouristMenuView.class,"Menu"));
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
	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    return str.matches("\\d+(\\.\\d+)?"); // Matches integers or decimals
	}
	
	// Feedback related methods
	public Feedback createFeedbackObject() {
	    // Extract input values
	    int serviceID = Integer.parseInt(busIdInput.getText()); // Assuming service ID corresponds to bus ID
	    String comment = commentsInput.getText();
	    int rating = Integer.parseInt(ratingInput.getValue()); // Get selected rating value from ComboBox
	    String typeOfFeedback = "Bus";
	    
	    // Create and return the Feedback object
	    return new Feedback(0, serviceID, rating, comment, typeOfFeedback, touristID);
	}

	
	//check if all inputs have been given
	private void validateInputs() {
	    boolean allFieldsFilled = 
	        !busIdInput.getText().trim().isEmpty()&&
	    !commentsInput.getText().trim().isEmpty()&&
	    ratingInput.getValue()!=null;
	    

	    submitButton.setDisable(!allFieldsFilled);
	}
}
