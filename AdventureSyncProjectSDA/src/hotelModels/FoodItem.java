package hotelModels;

public class FoodItem {
	//variables
	private int foodID;
	private String name;
	private int quantity;
	private float price;
	//constructor
	public FoodItem() {
		foodID = 0;
		name = "";
		quantity = 0;
		price = 0;
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
	
}
