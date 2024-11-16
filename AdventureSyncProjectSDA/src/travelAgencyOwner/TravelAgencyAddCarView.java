package travelAgencyOwner;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class TravelAgencyAddCarView {
	
	@FXML
	private Button addButton;
	@FXML
	private TextField brandInput;
	@FXML
	private TextField modelInput;
	@FXML
	private TextField yearInput;
	@FXML
	private TextField rentalFeeInput;
	@FXML
	private TextField plateNoInput;
	@FXML
	private TextField costPerKmInput;
	Parent root;
	
	public TravelAgencyAddCarView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerAddCar.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@FXML
	private void initialize() {
		buttonAssignment();
	}
	
	public Parent getRoot() {
		return root;
	}
	
	public void buttonAssignment() {
		EventHandler<ActionEvent> addButtonHandler=(event)->{
			System.out.println(brandInput.getText());
		};
		addButton.setOnAction(addButtonHandler);
	}
}
