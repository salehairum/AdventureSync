package hotelModels;

import java.util.ArrayList;

public class Kitchen {
	//variables
	private int kitchenID;
	private ArrayList<FoodItem> menu;
	//constructor
	public Kitchen() {
		menu = null;
	}
	//getter and setter
	public ArrayList<FoodItem> getMenu() {
		return menu;
	}

	public void setMenu(ArrayList<FoodItem> menu) {
		this.menu = menu;
	}
	public int getKitchenID() {
		return kitchenID;
	}
	public void setKitchenID(int kitchenID) {
		this.kitchenID = kitchenID;
	}
}
