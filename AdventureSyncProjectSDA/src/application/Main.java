package application;

import java.util.HashMap;

import busDriver.BusDriverUpdateBusView;
import hotelOwner.HOMAddFood;
import hotelOwner.HOMAddRoom;
import hotelOwner.HotelOwnerMenuView;
import dbHandlers.BusDBHandler;
import dbHandlers.DatabaseManager;
import dbHandlers.HotelDBHandler;
import dbHandlers.ReturnListUtility;
import dbHandlers.ReturnObjectUtility;
import javafx.application.Application;
import tourist.TouristRatesBusTourView;
import travelAgencyModels.Car;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tourist.TouristMenuView;
import travelAgencyOwner.TravelAgencyAddCarView;
import travelAgencyOwner.TravelAgencyDeleteCarsView;
import travelAgencyOwner.TravelAgencyOwnerAssignsTourToBusView;
import travelAgencyOwner.TravelAgencyOwnerUpdatesAccountView;
import travelAgencyOwner.TravelAgencyUpdatesCarView;
import travelAgencyOwner.TravelAgencyViewBusesView;
import signupForms.BusDriverAddsBus;
import signupForms.TouristSignUpView;
import dbHandlers.TravelAgencyDBHandler;
import hotelModels.hotelOwnerController;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//saleha: E85OBQM
		//afsah: MHOGR9K
		String connectionString="jdbc:sqlserver://DESKTOP-E85OBQM\\SQLEXPRESS;databaseName=sdaDB;integratedSecurity=true;encrypt=false";
		DatabaseManager dbManager=new DatabaseManager(connectionString);
		
		HotelDBHandler hdb = new HotelDBHandler(dbManager.getConnection());
		TravelAgencyDBHandler tbd = new TravelAgencyDBHandler(dbManager.getConnection());
		BusDBHandler bdb = new BusDBHandler(dbManager.getConnection());

		try {
		//	TravelAgencyAddCarView view= new TravelAgencyAddCarView();


//			HotelOwnerMenuView hotelOwnerMenu=new HotelOwnerMenuView();
//			Parent root = hotelOwnerMenu.getRoot();
//			
			
			TravelAgencyAddCarView hotelOwnerMenu = new TravelAgencyAddCarView();
			//BusDriverUpdateBusView  hotelOwnerMenu = new BusDriverUpdateBusView();
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
