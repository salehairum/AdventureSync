package busDriver;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class BusDriverManageBusView {
	@FXML
	private Pane sidePanel;
	@FXML
	private Pane mainPanel;
	Parent root;
	
	public BusDriverManageBusView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/busDriver/busDriverMgrBus.fxml"));
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
