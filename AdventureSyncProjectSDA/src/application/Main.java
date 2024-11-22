package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import tourist.TouristRatesBusView;
import tourist.TouristRentCarView;
import tourist.TouristReturnCarView;
import tourist.TouristSelectRoomFromHotelView;
import tourist.TouristSelectSeatFromBusView;
import tourist.TouristSelectsFoodFromMenuView;
import tourist.touristRoomFeedbackView;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.Seat;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tourist.TouristBooksRoomView;
import tourist.TouristBooksSeatView;
import tourist.TouristDeletesAccountView;
import tourist.TouristChecksOutRoomView;
import tourist.TouristMenuView;
import tourist.TouristOrdersFoodView;
import tourist.TouristPaymentView;
import travelAgencyOwner.TravelAgencyAddCarView;
import travelAgencyOwner.TravelAgencyDeleteCarsView;
import travelAgencyOwner.TravelAgencyManageAccountView;
import travelAgencyOwner.TravelAgencyManageBusView;
import travelAgencyOwner.TravelAgencyManageCarsView;
import travelAgencyOwner.TravelAgencyOwnerAssignsTourToBusView;
import travelAgencyOwner.TravelAgencyOwnerUpdatesAccountView;
import travelAgencyOwner.TravelAgencyOwnerViewCarsView;
import travelAgencyOwner.TravelAgencyOwnerViewsBusDriversView;
import travelAgencyOwner.TravelAgencyOwnerViewsFeedbackView;
import travelAgencyOwner.TravelAgencyUpdatesCarView;
import travelAgencyOwner.TravelAgencyViewBusesView;
import signupForms.BusDriverAddsBus;
import signupForms.BusDriverSignUpView;
import signupForms.TouristSignUpView;
import signupForms.TravelAgencyOwnerSignUpView;
import dbHandlers.TravelAgencyDBHandler;
import hotelModels.Hotel;
import hotelModels.Room;
import hotelModels.hotelOwnerController;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//saleha: E85OBQM
		//afsah: MHOGR9K
		//apni db ka naam bhi change karlena yaad se!
		String connectionString="jdbc:sqlserver://DESKTOP-MHOGR9K\\SQLEXPRESS;databaseName=sdaProjectDB;integratedSecurity=true;encrypt=false";
		DatabaseManager dbManager=new DatabaseManager(connectionString);
		
		HotelDBHandler hdb = new HotelDBHandler(dbManager.getConnection());
		TravelAgencyDBHandler tadb = new TravelAgencyDBHandler(dbManager.getConnection());
		BusDBHandler bdb = new BusDBHandler(dbManager.getConnection());
		TouristDBHandler tdb = new TouristDBHandler(dbManager.getConnection());

		try {
			
 			//Hotel hotel=hdb.retrieveHotelObject(1).getObject();
// 			ArrayList<Room> rooms=hotel.getRooms();
// 			for (Room room : rooms) {
// 			    // Access properties or methods of each room
// 			    System.out.println("Room ID: " + room.getRoomID());}
			
// 			Bus bus=bdb.retrieveBusObject(15).getObject();
				
	
 		//	TouristSelectRoomFromHotelView
 			//TouristChecksOutRoomView
 			Hotel hotel = new Hotel();
 			hotel.setHotelID(1);
 			TravelAgencyOwnerViewsFeedbackView hotelOwnerMenu = new TravelAgencyOwnerViewsFeedbackView();

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
