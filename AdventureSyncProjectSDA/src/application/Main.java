package application;

import dbHandlers.DatabaseManager;
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
//		String connectionString="jdbc:sqlserver://DESKTOP-E85OBQM\\SQLEXPRESS;databaseName=sdaDB;integratedSecurity=true;encrypt=false";
//		DatabaseManager dbManager=new DatabaseManager(connectionString);
//		
//		TravelAgencyDBHandler db=new TravelAgencyDBHandler(dbManager.getConnection());
//		Car car=new Car(1234, "Toyota", "Corolla", 2022, "XYZ-1234", false, 5000.0f, 15.0f);
//		
//		System.out.println(db.deleteCar(1));
		
		try {		
			TouristSignUpView busDriverMenu=new TouristSignUpView();
			Parent root=busDriverMenu.getRoot();
			
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
