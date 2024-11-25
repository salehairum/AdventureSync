package dataUtilityClasses;

import dbHandlers.HotelDBHandler;

public class RoomWithHotel {
    private int roomId;
    private float price;
    private String description;
    private int hotelId;
    private String hotelName;
    private HotelDBHandler hotelDbHandler;
    
    public RoomWithHotel() {
        this.roomId = 0;
        this.price = 0;
        this.description = "";
        this.hotelId = 0;
        this.hotelName = "";
        hotelDbHandler=new HotelDBHandler();
    }
    public RoomWithHotel(int rID, float price, String desc, int hID, String hName) {
        this.roomId = rID;
        this.price = price;
        this.description = desc;
        this.hotelId = hID;
        this.hotelName = hName;
        hotelDbHandler=new HotelDBHandler();
    }
    public int getRoomId() { return roomId; }
    public float getPrice() { return price; }
    public String getDescription() { return description; }
    public int getHotelId() { return hotelId; }
    public String getHotelName() { return hotelName; }
    
    public ReturnListUtility<RoomWithHotel> getBookedRoomDetails(int touristlID) {
		return hotelDbHandler.getBookedRoomDetails(touristlID);
	}
}
