module AdventureSyncProjectSDA {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens travelAgencyOwner to javafx.fxml;
	opens busDriver to javafx.fxml;
	opens signupForms to javafx.fxml;
}
