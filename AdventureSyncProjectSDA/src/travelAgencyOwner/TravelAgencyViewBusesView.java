package travelAgencyOwner;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class TravelAgencyViewBusesView {
	@FXML
	private Pane sidePanel;
	@FXML
	private Pane mainPanel;
	
	Parent root;
	
	public TravelAgencyViewBusesView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/travelAgencyOwner/travelAgencyOwnerViewsBuses.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public Parent getRoot() {
		return root;
	}
}
