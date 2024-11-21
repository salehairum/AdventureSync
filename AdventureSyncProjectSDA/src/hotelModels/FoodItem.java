package hotelModels;

import dbHandlers.HotelDBHandler;
import dbHandlers.ReturnObjectUtility;

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
}
