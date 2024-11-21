package tourist;

import java.io.IOException;

import accountAndPersonModels.Tourist;
import dbHandlers.ReturnObjectUtility;
import hotelModels.hotelOwnerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Bus;
import travelAgencyModels.TouristController;
import travelAgencyModels.busDriverController;
import travelAgencyModels.travelAgencyOwnerController;

public class TouristPaymentView {
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
	private Button payCreditButton;
	@FXML
	private Button payCashButton;
	@FXML
	private Text amountText;
	
	float bill;
	private int touristID;
	private int serviceID;
	private String typeOfTransaction;
	private int transactionID;
	Parent root;
	
	TouristController tController;
	busDriverController bController;
	hotelOwnerController hController;
	travelAgencyOwnerController taController;
	
	public TouristPaymentView(int tID, int sID, String type, int trID) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristPayment.fxml"));
		loader.setController(this);
		touristID=tID;
		serviceID=sID;
		typeOfTransaction=type;
		transactionID=trID;
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
		bController = new busDriverController();
		hController=new hotelOwnerController();
		taController=new travelAgencyOwnerController();
		displayOwnerDetails();
		eventHandlersAssignment();
		bill=getBill();
		amountText.setText(Float.toString(bill));
	}
	
	public void eventHandlersAssignment() {
		payCreditButton.setOnAction(payButtonHandler(true));
		payCashButton.setOnAction(payButtonHandler(false));

		backButton.setOnMouseClicked(createButtonHandler(TouristMenuView.class, "Menu"));
    }
	private EventHandler<ActionEvent> payButtonHandler(boolean isCreditPayment) {
	    return (event) -> {
	        payCreditButton.setDisable(true);
	        payCashButton.setDisable(true); // Disable further clicking

	        if(isCreditPayment) {
	        	ReturnObjectUtility<Boolean> isPaymentPossible=tController.checkBalance(touristID,bill);
	        	if(!isPaymentPossible.isSuccess()) {
	        		Alert alert = new Alert(AlertType.ERROR);
	    	        alert.setTitle("Operation Failed");
	    	        alert.setHeaderText(null);
	    	        alert.setContentText(isPaymentPossible.getMessage());
	    	        alert.showAndWait();
	    	        
	    	        payCashButton.setDisable(false); // Disable further clicking
	    	        return;
	        	}
	        }
	        
	        // Deduct money
	        ReturnObjectUtility<Tourist> returnData = tController.deductMoney(touristID, bill, transactionID, isCreditPayment);
	        boolean success = returnData.isSuccess();
	        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
	        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
	        alert.setHeaderText(null);
	        alert.setContentText(returnData.getMessage());
	        alert.showAndWait();

	        if (success && isCreditPayment) {
	            // Add money
	        	ReturnObjectUtility<Float> returnData2=new ReturnObjectUtility<Float>();
	        	if(typeOfTransaction=="Bus") {
	        		bController.addMoney(serviceID, bill);
	        	}
	        	else if(typeOfTransaction=="Rent") {
	        		taController.addMoney(bill);
	        	}
  	            success = returnData2.isSuccess();
  	            alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
  	            alert.setTitle(success ? "Operation Successful" : "Operation Failed");
  	            alert.setHeaderText(null);
  	            alert.setContentText(returnData2.getMessage());
  	            alert.showAndWait();	
	        }
	    };
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
    
    public float getBill() {
    	ReturnObjectUtility<Float> returnData=new ReturnObjectUtility<Float>();
    	if(typeOfTransaction=="Bus")  //bus seat was booked
    	{
    		returnData=bController.getBill(serviceID);
    	}
    	else if(typeOfTransaction=="Room")  //hotel room was booked
    	{
    	}
    	else if(typeOfTransaction=="Rent")  //car was rented
    	{
    		returnData=taController.getBill(serviceID);
    	}
    	else if(typeOfTransaction=="Return")  //car was returned
    	{

    	}
    	else //food was ordered
    	{
    		
    	}
    	
    	boolean success=returnData.isSuccess();
    	if(!success) {
    		Alert alert = new Alert(AlertType.ERROR);
		    alert.setTitle("Operation Failed");
		    alert.setHeaderText(null);
		    alert.setContentText(returnData.getMessage());
		    alert.showAndWait();
		    return 0f;
    	}
    	else return returnData.getObject();
    }
}
