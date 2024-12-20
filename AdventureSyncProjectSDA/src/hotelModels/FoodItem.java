package hotelModels;

import dataUtilityClasses.ReturnListUtility;
import dataUtilityClasses.ReturnObjectUtility;
import dbHandlers.HotelDBHandler;

public class FoodItem {
	//variables
	private int foodID;
	private String name;
	private int quantity;
	private float price;
	private HotelDBHandler hotelDbHandler;
	//constructor
	public FoodItem() {
		foodID = 0;
		name = "";
		quantity = 0;
		price = 0;
        hotelDbHandler=new HotelDBHandler();
	}
	public FoodItem(int foodID, String name, int quantity, float price) {
        this.foodID = foodID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.hotelDbHandler = new HotelDBHandler(); // Initialize database handler
    }
	//getter and setter
	public int getFoodID() {
		return foodID;
	}
	public void setFoodID(int foodID) {
		this.foodID = foodID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public ReturnObjectUtility<FoodItem> updateFoodQuantity(int foodID, int quantity, boolean add) {
		return hotelDbHandler.updateFoodQuantity(foodID, quantity, add);
	}
	public ReturnObjectUtility<Boolean> addFoodItem(FoodItem foodItem, int hotelID) {
		return hotelDbHandler.addFoodItem(foodItem, hotelID);
	}
	public ReturnObjectUtility<Float> getFoodBill(int foodID, int quantity){
		return hotelDbHandler.getFoodBill(foodID, quantity);
	}
	public ReturnListUtility<FoodItem> getFoodDetails(int hotelID) {
		return hotelDbHandler.retrieveFoodList(hotelID);
  }
	public ReturnObjectUtility<Integer> getHotelOwnerID(int foodID) {
		return hotelDbHandler.getHotelOwnerID(foodID);
	}

	public ReturnObjectUtility<Boolean> deleteFoodItem(int foodID) {
		return hotelDbHandler.deleteFoodItem(foodID);
	}
	public ReturnObjectUtility<FoodItem> retrieveFoodItemObject(int foodID) {
		return hotelDbHandler.retrieveFoodItemObject(foodID);
	}

	public ReturnObjectUtility<Boolean> updateFoodItem(FoodItem foodItem) {
		return hotelDbHandler.updateFoodItem(foodItem);
	}
}
