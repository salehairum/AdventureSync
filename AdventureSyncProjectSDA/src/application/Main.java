package application;

import java.time.LocalDate;
import java.util.HashMap;

import busDriver.BusDriverCompletesTourView;
import busDriver.BusDriverDeletesAccountView;
import busDriver.BusDriverManageBusView;
import busDriver.BusDriverMenuView;
import accountAndPersonModels.Account;
import accountAndPersonModels.TravelAgencyOwner;
import busDriver.BusDriverUpdateBusView;
import busDriver.BusDriverViewBusDetailsView;
import hotelOwner.HOMAddFood;
import hotelOwner.HOMAddRoom;
import hotelOwner.HOMDeleteFood;
import hotelOwner.HOMDeleteRoom;
import hotelOwner.HOMManageAccount;
import hotelOwner.HOMManageHotel;
import hotelOwner.HOMManageKitchen;
import hotelOwner.HOMManageRoom;
import hotelOwner.HOMUpdateFood;
import hotelOwner.HOMUpdateHotel;
import hotelOwner.HOMUpdateRoom;
import hotelOwner.HotelOwnerMenuView;
import dbHandlers.BusDBHandler;
import dbHandlers.DatabaseManager;
import dbHandlers.HotelDBHandler;
import dbHandlers.TouristDBHandler;
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
import travelAgencyOwner.TravelAgencyManageAccountView;
import travelAgencyOwner.TravelAgencyManageBusView;
import travelAgencyOwner.TravelAgencyManageCarsView;
import travelAgencyOwner.TravelAgencyOwnerAssignsTourToBusView;
import travelAgencyOwner.TravelAgencyOwnerUpdatesAccountView;
import travelAgencyOwner.TravelAgencyOwnerViewCarsView;
import travelAgencyOwner.TravelAgencyUpdatesCarView;
import travelAgencyOwner.TravelAgencyViewBusesView;
import signupForms.BusDriverAddsBus;
import signupForms.BusDriverSignUpView;
import signupForms.TouristSignUpView;
import signupForms.TravelAgencyOwnerSignUpView;
import dbHandlers.TravelAgencyDBHandler;
import hotelModels.hotelOwnerController;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//saleha: E85OBQM
		//afsah: MHOGR9K
		String connectionString="jdbc:sqlserver://DESKTOP-MHOGR9K\\SQLEXPRESS;databaseName=sdaDB;integratedSecurity=true;encrypt=false";
		DatabaseManager dbManager=new DatabaseManager(connectionString);
		
		HotelDBHandler hdb = new HotelDBHandler(dbManager.getConnection());
		TravelAgencyDBHandler tadb = new TravelAgencyDBHandler(dbManager.getConnection());
		BusDBHandler bdb = new BusDBHandler(dbManager.getConnection());
		TouristDBHandler tdb = new TouristDBHandler(dbManager.getConnection());

		try {
		//	TravelAgencyAddCarView view= new TravelAgencyAddCarView();


//			HotelOwnerMenuView hotelOwnerMenu=new HotelOwnerMenuView();
//			Parent root = hotelOwnerMenu.getRoot();
//			

			

			TravelAgencyManageCarsView hotelOwnerMenu = new TravelAgencyManageCarsView();
			Parent root = hotelOwnerMenu.getRoot();
//			// Create the scene and set it
			Scene scene = new Scene(root, 750, 500);
//	       
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
