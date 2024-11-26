module AdventureSyncProjectSDA {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
	opens travelAgencyOwnerView to javafx.fxml;
	opens hotelOwnerView to javafx.fxml;
	opens busDriverView to javafx.fxml;
	opens signupForms to javafx.fxml;
	opens travelAgencyModels to javafx.base;
	opens touristView to javafx.fxml;
	opens hotelModels to javafx.base;
	opens accountAndPersonModels to javafx.base;
	opens dataUtilityClasses to javafx.base;
}
