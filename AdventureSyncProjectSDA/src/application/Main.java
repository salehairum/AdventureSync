package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accountAndPersonModels.Account;
import accountAndPersonModels.TravelAgencyOwner;
import busDriverView.BusDriverCompletesTourView;
import busDriverView.BusDriverDeletesAccountView;
import busDriverView.BusDriverManageBusView;
import busDriverView.BusDriverMenuView;
import busDriverView.BusDriverUpdateBusView;
import busDriverView.BusDriverUpdatesAccountView;
import busDriverView.BusDriverViewBusDetailsView;
import busDriverView.BusDriverViewsFeedbackView;
import busDriverView.BusDriverViewsTourDetailsView;
import busDriverView.busDriverLogin;
import controllers.hotelOwnerController;
import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dbHandlers.BusDBHandler;
import dbHandlers.DatabaseManager;
import dbHandlers.HotelDBHandler;
import dbHandlers.TouristDBHandler;
import javafx.application.Application;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.Seat;
import travelAgencyOwnerView.TravelAgencyAddCarView;
import travelAgencyOwnerView.TravelAgencyDeleteCarsView;
import travelAgencyOwnerView.TravelAgencyManageAccountView;
import travelAgencyOwnerView.TravelAgencyManageBusView;
import travelAgencyOwnerView.TravelAgencyManageCarsView;
import travelAgencyOwnerView.TravelAgencyOwnerAssignsTourToBusView;
import travelAgencyOwnerView.TravelAgencyOwnerDeleteAccountView;
import travelAgencyOwnerView.TravelAgencyOwnerUpdatePasswordView;
import travelAgencyOwnerView.TravelAgencyOwnerUpdatesAccountView;
import travelAgencyOwnerView.TravelAgencyOwnerViewCarsView;
import travelAgencyOwnerView.TravelAgencyOwnerViewsBusDriversView;
import travelAgencyOwnerView.TravelAgencyOwnerViewsFeedbackView;
import travelAgencyOwnerView.TravelAgencyUpdatesCarView;
import travelAgencyOwnerView.TravelAgencyViewBusesView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import signupForms.AccountMenuView;
import signupForms.BusDriverAddsBus;
import signupForms.BusDriverSignUpView;
import signupForms.HotelOwnerAddsHotel;
import signupForms.TouristSignUpView;
import signupForms.TravelAgencyOwnerSignUpView;
import touristView.TouristBooksRoomView;
import touristView.TouristBooksSeatView;
import touristView.TouristChecksOutRoomView;
import touristView.TouristDeletesAccountView;
import touristView.TouristMenuView;
import touristView.TouristOrdersFoodView;
import touristView.TouristPaymentView;
import touristView.TouristRatesBusView;
import touristView.TouristRentCarView;
import touristView.TouristReturnCarView;
import touristView.TouristSelectRoomFromHotelView;
import touristView.TouristSelectSeatFromBusView;
import touristView.TouristSelectsFoodFromMenuView;
import touristView.TouristUpdatePasswordView;
import touristView.TouristUpdatesAccountView;
import touristView.touristLogin;
import touristView.touristRoomFeedbackView;
import dbHandlers.TravelAgencyDBHandler;
import hotelModels.Hotel;
import hotelModels.Room;
import hotelOwnerView.HOMAddFood;
import hotelOwnerView.HOMAddRoom;
import hotelOwnerView.HOMDeleteFood;
import hotelOwnerView.HOMDeleteRoom;
import hotelOwnerView.HOMManageAccount;
import hotelOwnerView.HOMManageHotel;
import hotelOwnerView.HOMManageKitchen;
import hotelOwnerView.HOMManageRoom;
import hotelOwnerView.HOMUpdateFood;
import hotelOwnerView.HOMUpdateHotel;
import hotelOwnerView.HOMUpdateRoom;
import hotelOwnerView.HOMViewFeedback;
import hotelOwnerView.HOMViewFood;
import hotelOwnerView.HOMViewHotel;
import hotelOwnerView.HOMViewRoom;
import hotelOwnerView.HotelOwnerMenuView;
import hotelOwnerView.hotelOwnerLogin;
import hotelOwnerView.hotelOwnerUpdateAccount;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		FactoryClass factory=new FactoryClass(primaryStage);

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
