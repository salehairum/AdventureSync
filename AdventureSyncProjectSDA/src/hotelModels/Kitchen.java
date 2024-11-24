package hotelModels;

import java.util.ArrayList;

import dataUtilityClasses.ReturnObjectUtility;

public class Kitchen {
	//variables
	private int kitchenID;
	private ArrayList<FoodItem> menu;
	private FoodItem food;
	//constructor
	public Kitchen() {
		menu = null;
		food=new FoodItem();
	}
	public Kitchen(int kitchenID) {
		this.kitchenID=kitchenID;
		menu = null;
		food=new FoodItem();
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
		return food.updateFoodQuantity(foodID, quantity, add);
	}
	public ReturnObjectUtility<Boolean> addFoodItem(FoodItem foodItem, int hotelID) {
		return food.addFoodItem(foodItem, hotelID);
	}
	public ReturnObjectUtility<Float> getFoodBill(int foodID, int quantity){
		return food.getFoodBill(foodID, quantity);
	}
	public ReturnObjectUtility<Integer> getHotelOwnerID(int foodID) {
		return food.getHotelOwnerID(foodID);
	}
	public ReturnObjectUtility<Boolean> deleteFoodItem(int foodID) {
		return food.deleteFoodItem(foodID);
	}
	public ReturnObjectUtility<FoodItem> retrieveFoodItemObject(int foodID) {
		return food.retrieveFoodItemObject(foodID);
	}

	public ReturnObjectUtility<Boolean> updateFoodItem(FoodItem foodItem) {
		return food.updateFoodItem(foodItem);
	}
}

