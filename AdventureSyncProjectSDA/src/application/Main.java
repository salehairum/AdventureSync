package application;

import java.util.HashMap;
import hotelOwner.HotelOwnerMenuView;
import dbHandlers.DatabaseManager;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
import javafx.application.Application;
import tourist.TouristRatesBusTourView;
import travelAgencyModels.Car;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tourist.TouristMenuView;
import travelAgencyOwner.TravelAgencyViewBusesView;
import signupForms.TouristSignUpView;
import dbHandlers.TravelAgencyDBHandler;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		String connectionString="jdbc:sqlserver://DESKTOP-E85OBQM\\SQLEXPRESS;databaseName=sdaDB;integratedSecurity=true;encrypt=false";
		DatabaseManager dbManager=new DatabaseManager(connectionString);
		
    //this is db handler stuff that i wass doing
    
		//TravelAgencyDBHandler db=new TravelAgencyDBHandler(dbManager.getConnection());
		//Car car=new Car(12, "Toyota", "Corolla", 2022, "XYZ-1323", false, 5000.0f, 15.0f);
		
		//car.setBrand("bla bla");
		
		//String returnData=db.updateCar(car);
		//System.out.println(returnData);


		try {
			
			HotelOwnerMenuView hotelOwnerMenu=new HotelOwnerMenuView();
			Parent root = hotelOwnerMenu.getRoot();
			
			// Create the scene and set it
			Scene scene = new Scene(root, 750, 500);
	       
		        // Set the scene to the primary stage
		        primaryStage.setScene(scene);
		        primaryStage.setTitle("Hotel Owner Menu");
		        primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
