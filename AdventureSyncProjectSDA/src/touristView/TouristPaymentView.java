package touristView;

import java.io.IOException;

import accountAndPersonModels.Tourist;
import controllers.TouristController;
import controllers.busDriverController;
import controllers.hotelOwnerController;
import controllers.travelAgencyOwnerController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import travelAgencyModels.Bus;

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
	int numberOfUnits;
	Parent root;
	
	TouristController tController;
	busDriverController bController;
	hotelOwnerController hController;
	travelAgencyOwnerController taController;
	
	//n can be 
	//food quantity
	//number of kms travelled
	//number of nights tourist stayed
	public TouristPaymentView(Integer tID, Integer sID, String type, Integer trID, Integer n) {
		touristID=tID;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/touristView/touristPayment.fxml"));
		loader.setController(this);
		serviceID=sID;
		typeOfTransaction=type;
		transactionID=trID;
		numberOfUnits=n;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public TouristPaymentView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/tourist/touristPayment.fxml"));
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
		backButton.setOnMouseClicked(createButtonHandler(TouristMenuView.class, "Menu", touristID));
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
	    	        
	    	        payCashButton.setDisable(false); 
	    	        return;
	        	}
	        }
	        
	        // Deduct money
	        ReturnObjectUtility<Tourist> returnData = tController.deductMoney(touristID, bill, transactionID, isCreditPayment);
	        boolean success = returnData.isSuccess();
	        
	        if(!success) {
		        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
		        alert.setTitle(success ? "Operation Successful" : "Operation Failed");
		        alert.setHeaderText(null);
		        alert.setContentText(returnData.getMessage());
		        alert.showAndWait();
	        }
	        
	        if (success && isCreditPayment) {
	            // Add money
	        	ReturnObjectUtility<Float> returnData2=new ReturnObjectUtility<Float>();
	        	if(typeOfTransaction=="Bus") {
	        		returnData2=bController.addMoney(serviceID, bill);
	        	}
	        	else if(typeOfTransaction=="Rent" || typeOfTransaction=="Return") {
	        		returnData2=taController.addMoney(bill);
	        	}
	        	else if(typeOfTransaction=="Room") {
	        		returnData2=hController.addMoneyRoom(serviceID, bill);
	        	}
	        	else if(typeOfTransaction=="Order") {
	        		returnData2=hController.addMoneyFood(serviceID, bill);
	        	}
  	            success = returnData2.isSuccess();
  	            Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
  	            alert.setTitle(success ? "Operation Successful" : "Operation Failed");
  	            alert.setHeaderText(null);
  	            alert.setContentText(returnData2.getMessage());
  	            alert.showAndWait();	
	        }
	    };
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
    
    public float getBill() {
    	ReturnObjectUtility<Float> returnData=new ReturnObjectUtility<Float>();
    	if(typeOfTransaction=="Bus")  //bus seat was booked
    	{
    		returnData=bController.getBill(serviceID);
    	}
    	else if(typeOfTransaction=="Room")  //hotel room was booked
    	{
    		returnData=hController.getBill(serviceID, touristID);
    	}
    	else if(typeOfTransaction=="Rent")  //car was rented
    	{
    		returnData=taController.getBill(serviceID);
    	}
    	else if(typeOfTransaction=="Return")  //car was returned
    	{
    		returnData=taController.getKmsTravelledBill(serviceID, numberOfUnits);
    	}
    	else //food was ordered
    	{
    		 returnData=hController.getFoodBill(serviceID, numberOfUnits);
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
