package hotelModels;

import java.util.ArrayList;

import dbHandlers.ReturnObjectUtility;

public class Kitchen {
	//variables
	private int kitchenID;
	private ArrayList<FoodItem> menu;
	//constructor
	public Kitchen() {
		menu = null;
	}
	public Kitchen(int kitchenID) {
		this.kitchenID=kitchenID;
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
	public ReturnObjectUtility<FoodItem> updateFoodQuantity(int foodID, int quantity, boolean add) {
		FoodItem food=new FoodItem();
		return food.updateFoodQuantity(foodID, quantity, add);
	}
}

