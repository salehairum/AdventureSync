package application;

import javafx.application.Application;
import tourist.TouristRatesBusTourView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tourist.TouristMenuView;
import travelAgencyOwner.TravelAgencyViewBusesView;
import signupForms.TouristSignUpView;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//String connectionString="jdbc:sqlserver://DESKTOP-E85OBQM\\SQLEXPRESS;databaseName=db_crms;integratedSecurity=true;encrypt=false";
		//DatabaseManager dbManager=new DatabaseManager(connectionString);
		
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
