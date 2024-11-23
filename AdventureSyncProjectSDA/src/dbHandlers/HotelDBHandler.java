package dbHandlers;

import java.sql.Connection;
import java.sql.Date;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;

import accountAndPersonModels.Account;
import accountAndPersonModels.HotelOwner;
import accountAndPersonModels.Tourist;
import application.Feedback;
import hotelModels.FeedbackWithRoomID;
import hotelModels.FoodItem;
import hotelModels.Hotel;
import hotelModels.Kitchen;
import hotelModels.Room;
import hotelModels.RoomWithHotel;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
import travelAgencyModels.FeedbackWithBusID;
import travelAgencyModels.Seat;

public class HotelDBHandler {
	private static Connection conn;
	public HotelDBHandler() {}
	public HotelDBHandler(Connection c) {
		conn = c;
	}
	
	public static Connection getConnection() {
		return conn;
	}

	public static void setConnection(Connection conn) {
		HotelDBHandler.conn = conn;
	}
	//room related functions
	
	//hotel owner related functions
	public ReturnObjectUtility<Boolean> addRoom(Room room) {
		ReturnObjectUtility<Boolean> returnData=new ReturnObjectUtility<Boolean>();
		PreparedStatement pstmt;
		try {
			 String sql = "INSERT INTO Room (hotelID, rDescription, pricePerNight) VALUES (?, ?, ?);";
			 pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			    
			 // Set parameters
			 pstmt.setInt(1, room.getHotelID());
			 pstmt.setString(2, room.getDescription());
			 pstmt.setFloat(3, room.getPricePerNight());

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
		                if (generatedKeys.next()) {
		                    int newCarId = generatedKeys.getInt(1);
		                    returnData.setMessage("Room added successfully with ID: " + newCarId);
		                } else {
		                	returnData.setMessage("Room added, but ID retrieval failed.");
		                }
		            }
                 returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to add room.");
                 returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in adding room to db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

            returnData.setSuccess(false);
		}
		return returnData;
	}
	
	public static ReturnObjectUtility<HotelOwner> retrieveHotelOwnerData(int hotelOwnerID) {
		ReturnObjectUtility<HotelOwner> returnData = new ReturnObjectUtility();
		
		try {
			Statement stmt = conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from HotelOwner where hotelOwnerId="+hotelOwnerID);
	        
	        if (rSet.next()) { // Check if a result was found
	            // Create and populate a hotel owner object
	            int hotelOwnerIDRetrieved = rSet.getInt("hotelOwnerId");
	            String name = rSet.getString("aName");
	            Date date = rSet.getDate("dob");
	            LocalDate dob = date.toLocalDate();
	            String cnic = rSet.getString("cnic");

	            HotelOwner hotelOwner = new HotelOwner(hotelOwnerIDRetrieved, name, dob, cnic);
	            
	            // Set the hotel owner object and success message
	            returnData.setObject(hotelOwner);
	            returnData.setMessage("Hotel Owner data retrieved successfully.");
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Hotel Owner does not exist.");
	        }
            
		}
		catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("no such hotel owner") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		       returnData.setMessage("Error: Hotel Owner does not exist.");
		    } else {
		        // General case for other SQL exceptions
		    	returnData.setMessage("Issue in retrieving hotel owner from database: " + e.getMessage());
		    }
		}
        return returnData;
	}
	
	public ReturnObjectUtility<HotelOwner> addHotelOwner(HotelOwner hotelOwner) {
		ReturnObjectUtility<HotelOwner> returnData = new ReturnObjectUtility<>();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
			// First Insert into Account table
		    String sql = "INSERT INTO Account (username, email, accPassword, balance) VALUES (?, ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    // Set parameters
		    Account account = hotelOwner.getAccount();
		    pstmt.setString(1, account.getUsername());
		    pstmt.setString(2, account.getEmail());
		    pstmt.setString(3, account.getPassword());
		    pstmt.setFloat(4, account.getBalance());

		    // Execute the insert and fetch generated accountID
		    int retrievedAccountID=0;
		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	rs = pstmt.getGeneratedKeys();
		        if (rs.next()) {
		        	retrievedAccountID = rs.getInt(1);
		        	account.setAccountID(retrievedAccountID);
		        	hotelOwner.getAccount().setAccountID(retrievedAccountID);
		        } else {
		            returnData.setMessage("Failed to retrieve generated Account ID.");
		            returnData.setSuccess(false);
		            return returnData;
		        }
		    } else {
		        returnData.setMessage("Failed to insert into Account table.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		        // Step 2: Insert into TravelAgencyOwner table
		        sql = "INSERT INTO HotelOwner (accountID, aName, dob, cnic) VALUES ( ?, ?, ?, ?)";
		        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		        // Set parameters
		        pstmt.setInt(1, hotelOwner.getAccount().getAccountID());
		        pstmt.setString(2, hotelOwner.getName());
		        pstmt.setObject(3, hotelOwner.getDob());
		        pstmt.setString(4, hotelOwner.getCnic());

		        // Execute the insert
		        int retrievedOwnerID=0;
		        rowsAffected = pstmt.executeUpdate();
		        if (rowsAffected > 0) {
		            rs = pstmt.getGeneratedKeys();
		            if (rs.next()) {
		            	retrievedOwnerID = rs.getInt(1);
		            	hotelOwner.setHotelOwnerID(retrievedOwnerID);
		            	returnData.setMessage("Hotel Owner with id "+retrievedOwnerID+" added successfuly!");
		                returnData.setSuccess(true);
		                return returnData;
		            } else {
		                returnData.setMessage("Failed to retrieve generated Account ID.");
		                returnData.setSuccess(false);
		                return returnData;
		            }
		        } else {
		            returnData.setMessage("Failed to insert into Hotel Owner table.");
		            returnData.setSuccess(false);
		            return returnData;
		        }

		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		            } else if (errorMessage.contains("unique key constraint")) {
		                returnData.setMessage("Error: Duplicate username, email, or CNIC detected.");
		            } else {
		                returnData.setMessage("Issue in adding Hotel Owner in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
		}

	public ReturnObjectUtility<Hotel> retrieveHotelObject(int hotelID) {
	    ReturnObjectUtility<Hotel> returnData = new ReturnObjectUtility<>();
	    
	    try {
	        Statement stmt = conn.createStatement();
	        ResultSet hotelSet = stmt.executeQuery("SELECT * FROM Hotel WHERE hotelID = " + hotelID);

	        if (hotelSet.next()) { // Check if the hotel exists
	            // Retrieve hotel details
	            int retrievedHotelID = hotelSet.getInt("hotelID");
	            String hotelName = hotelSet.getString("hName");
	            String location = hotelSet.getString("hLocation");
	            int kitchenID = hotelSet.getInt("kitchenID");

	            // Create a Kitchen object
	            Kitchen kitchen = new Kitchen(kitchenID);

	            // Retrieve rooms associated with the hotel
	            ArrayList<Room> rooms = new ArrayList<>();
	            ResultSet roomSet = stmt.executeQuery("SELECT * FROM Room WHERE hotelID = " + hotelID);
	            while (roomSet.next()) {
	                int roomID = roomSet.getInt("roomID");
	                String description = roomSet.getString("rDescription");
	                float pricePerNight = roomSet.getFloat("pricePerNight");
	                boolean isBooked = roomSet.getBoolean("isBooked");

	                // Create a Room object
	                Room room = new Room(roomID, description, pricePerNight, isBooked, hotelID);
	                rooms.add(room);
	            }

	            // Create a Hotel object
	            Hotel hotel = new Hotel(retrievedHotelID, hotelName, location, kitchen, rooms);

	            // Set the Hotel object and success message
	            returnData.setObject(hotel);
	            returnData.setMessage("Hotel data retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no hotel is found, set an error message
	            returnData.setMessage("Error: Hotel does not exist.");
	            returnData.setSuccess(false);
	        }
	    } catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("no such hotel") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		       returnData.setMessage("Error: Hotel does not exist.");
		    } else {
		        // General case for other SQL exceptions
		    	returnData.setMessage("Issue in retrieving hotel owner from database: " + e.getMessage());
		    }
		}
        return returnData;
	}
	
	public ReturnObjectUtility<Room> updateRoomBookingStatus(int roomID, boolean bookingStatus) {
		ReturnObjectUtility<Room> returnData=new ReturnObjectUtility();
		PreparedStatement pstmt;
		try {
			 String sql = "UPDATE room SET isBooked = ? WHERE roomid = ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setBoolean(1,bookingStatus);
			 pstmt.setInt(2, roomID);

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 if(bookingStatus) {
					 returnData.setMessage("Room is now booked.");
				 }
				 else {
					 returnData.setMessage("Room is no longer booked.");
				 }

		         returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to update room booking status.");
		         returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("no such room") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		        	returnData.setMessage("Error: Room does not exist.");
		        }else {
		        	returnData.setMessage("Issue in updating room in db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

	        returnData.setSuccess(false);
		}
		return returnData;
	}
	
	public ReturnObjectUtility<FoodItem> updateFoodQuantity(int foodID, int quantity, boolean add) {
	    ReturnObjectUtility<FoodItem> returnData = new ReturnObjectUtility<>();
	    PreparedStatement pstmt;
	    try {
	        // Adjust the SQL statement based on the `add` flag
	        String sql = "UPDATE FoodItem SET quantity = quantity " + (add ? "+ ?" : "- ?") + " WHERE foodID = ?";
	        pstmt = conn.prepareStatement(sql);

	        // Set parameters
	        pstmt.setInt(1, quantity);
	        pstmt.setInt(2, foodID);

	        // Execute the update
	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            returnData.setMessage("Food quantity successfully updated.");
	            returnData.setSuccess(true);
	        } else {
	            returnData.setMessage("Failed to update food quantity. Food item may not exist.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage != null) {
	            if (errorMessage.contains("check constraint") || errorMessage.contains("negative")) {
	                returnData.setMessage("Error: Quantity cannot be negative.");
	            } else if (errorMessage.contains("no such fooditem") || errorMessage.contains("does not exist")) {
	                returnData.setMessage("Error: Food item does not exist.");
	            } else {
	                returnData.setMessage("Issue updating food quantity in database: " + errorMessage);
	            }
	        } else {
	            returnData.setMessage("An unknown error occurred.");
	        }

	        returnData.setSuccess(false);
	    }
	    return returnData;
	}
	public ReturnObjectUtility<Float> getBill(int roomID, int touristID){
	    ReturnObjectUtility<Float> returnData = new ReturnObjectUtility<>();
		 try {
			 String sql = " SELECT thb.bookingDate, r.pricePerNight FROM TouristHasBookedRooms thb JOIN room r ON thb.roomID = r.roomID WHERE thb.roomID = ? AND thb.touristID = ?";
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 pstmt.setInt(1, roomID);
			 pstmt.setInt(2, touristID);

			 ResultSet rSet = pstmt.executeQuery();

			 if (rSet.next()) {
			       Date bookingDate = rSet.getDate("bookingDate");
			       float pricePerNight = rSet.getFloat("pricePerNight");
			       Date today = new java.sql.Date(System.currentTimeMillis());
		            long diffInMillis = today.getTime() - bookingDate.getTime();
		            int nightsStayed = (int) (diffInMillis / (1000 * 60 * 60 * 24)); // Convert ms to days

		            if (nightsStayed <= 0) {
		                nightsStayed = 1; // Handle edge case where today is bookingDate
		            }

		            // Calculate the total bill
		            float totalBill = pricePerNight * nightsStayed;

		            // Set return values
		            returnData.setObject(totalBill);
		            returnData.setMessage("Total bill calculated successfully.");
		            returnData.setSuccess(true);
			 } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: Room does not exist.");
		            returnData.setSuccess(false);
		        }
		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();
		        
		        if (errorMessage.contains("no such Room") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
		            returnData.setMessage("Error: Room does not exist.");
		        } else {
		            // General case for other SQL exceptions
		            returnData.setMessage("Issue in retrieving Room from database: " + e.getMessage());
		        }

		        returnData.setSuccess(false);
		    }
		    
		    return returnData;
	}
	
	public ReturnObjectUtility<Float> getFoodBill(int foodID, int quantity){
	    ReturnObjectUtility<Float> returnData = new ReturnObjectUtility<>();
		 try {
			 String sql = " SELECT price from foodItem where foodID= ?";
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 pstmt.setInt(1, foodID);

			 ResultSet rSet = pstmt.executeQuery();

			 if (rSet.next()) {
			       float price = rSet.getFloat("price")*quantity;
		            // Set return values
		            returnData.setObject(price);
		            returnData.setMessage("Total bill calculated successfully.");
		            returnData.setSuccess(true);
			 } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: Room does not exist.");
		            returnData.setSuccess(false);
		        }
		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();
		        
		        if (errorMessage.contains("no such Room") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
		            returnData.setMessage("Error: Room does not exist.");
		        } else {
		            // General case for other SQL exceptions
		            returnData.setMessage("Issue in retrieving Room from database: " + e.getMessage());
		        }

		        returnData.setSuccess(false);
		    }
		    
		    return returnData;
	}
	
	
	public ReturnObjectUtility<Float> addMoney(int roomID, float bill){
		 ReturnObjectUtility<Float> returnData = new ReturnObjectUtility<>();
		 PreparedStatement pstmt;
		 try {
			 	String sql = "UPDATE account SET balance = balance + ? WHERE accountID = (select hotelOwnerID from room inner join HotelOwnerOwnsHotel on HotelOwnerOwnsHotel.hotelID=room.hotelID where roomID=?)";
		        pstmt = conn.prepareStatement(sql);

		        // Set parameters
		        pstmt.setFloat(1, bill); // Deduction amount
		        pstmt.setInt(2, roomID); // room ID

		        // Execute the update
		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            returnData.setMessage("Balance updated successfully for hotel owner");
		            returnData.setSuccess(true);
		        } else {
		            returnData.setMessage("Failed to update balance for hotel owner");
		            returnData.setSuccess(false);
		        }
		        
		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		            } else {
		                returnData.setMessage("Issue in deducting money in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
	}
	
	public ReturnObjectUtility<Integer> getHotelID(int hotelOwnerID){
	    ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility<>();
		 try {
		        Statement stmt = conn.createStatement();
		        ResultSet rSet = stmt.executeQuery("select hotelId from HotelOwnerOwnsHotel where hotelOwnerID=" + hotelOwnerID);
		        
		        if (rSet.next()) { // Check if a result was found
		            int hotelID= rSet.getInt("hotelID");

		            returnData.setObject(hotelID);
		            returnData.setMessage("HotelID retrieved successfully.");
		            returnData.setSuccess(true);
		        } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: Hotel does not exist.");
		            returnData.setSuccess(false);
		        }
		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();
		        
		        if (errorMessage.contains("no such Room") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
		            returnData.setMessage("Error: Hotel does not exist.");
		        } else {
		            // General case for other SQL exceptions
		            returnData.setMessage("Issue in retrieving Hotel from database: " + e.getMessage());
		        }

		        returnData.setSuccess(false);
		    }
		    
		    return returnData;
	}
	
	public ReturnObjectUtility<Boolean> addFoodItem(FoodItem foodItem, int hotelID) {
	    ReturnObjectUtility<Boolean> returnData = new ReturnObjectUtility<Boolean>();
	    PreparedStatement pstmt;
	    try {
	        String sql = "INSERT INTO FoodItem (fname, quantity, price) VALUES (?, ?, ?);";
	        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

	        // Set parameters
	        pstmt.setString(1, foodItem.getName());
	        pstmt.setInt(2, foodItem.getQuantity());
	        pstmt.setFloat(3, foodItem.getPrice());

	        // Execute the insert into the Food table
	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    int newFoodID = generatedKeys.getInt(1);
	                    returnData.setMessage("Food item added successfully with ID: " + newFoodID);

	                    String kitchenCheckSql = "SELECT kitchenID FROM HotelHasKitchen WHERE hotelID = ?;";
	                    pstmt = conn.prepareStatement(kitchenCheckSql);
	                    pstmt.setInt(1, hotelID);
	                    ResultSet kitchenResult = pstmt.executeQuery();

	                    if (kitchenResult.next()) {
	                        String insertKitchenHasFoodSql = "INSERT INTO KitchenHasFood (kitchenID, foodID) VALUES (?, ?);";
	                        pstmt = conn.prepareStatement(insertKitchenHasFoodSql);
	                        pstmt.setInt(1, kitchenResult.getInt("kitchenID"));
	                        pstmt.setInt(2, newFoodID);
	                        pstmt.executeUpdate();
	                        returnData.setSuccess(true);
	                    } else {
	                        returnData.setMessage("Error: Hotel does not have a kitchen.");
	                        returnData.setSuccess(false);
	                    }
	                } else {
	                    returnData.setMessage("Food item added, but ID retrieval failed.");
	                    returnData.setSuccess(false);
	                }
	            }
	        } else {
	            returnData.setMessage("Failed to add food item.");
	            returnData.setSuccess(false);
	        }

	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage != null) {
	            if (errorMessage.contains("foreign key constraint")) {
	                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
	            } else {
	                returnData.setMessage("Issue in adding food item to db: " + errorMessage);
	            }
	        } else {
	            returnData.setMessage("An unknown error occurred.");
	       }
	    }
	    return returnData;
	}
	        
	public ReturnListUtility<Hotel> retrieveHotelList() {
	    ReturnListUtility<Hotel> returnData = new ReturnListUtility<>();

	    try {
	        Statement stmt = conn.createStatement();
	        ResultSet rSet = stmt.executeQuery("SELECT hotelID, hLocation, hName FROM Hotel");

	        HashMap<Integer, Hotel> hotelList = new HashMap<>();

	        if (!rSet.next()) {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: No hotels found in the database.");
	            returnData.setSuccess(false);
	        } else {
	            do {
	                // Retrieve hotel details from the result set
	                int hotelID = rSet.getInt("hotelID");
	                String location = rSet.getString("hLocation");
	                String hotelName = rSet.getString("hName");

	                // Create a Hotel object
	                Hotel hotel = new Hotel(hotelID, location, hotelName);

	                // Add the Hotel object to the HashMap
	                hotelList.put(hotelID, hotel);
	            } while (rSet.next());

	            returnData.setList(hotelList);
	            returnData.setMessage("Hotels retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage.contains("no such hotel") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Hotel table does not exist or is empty.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving hotels from the database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }

	    return returnData;
	}
	public ReturnListUtility<Room> retrieveRoomList(int hotelID) {
	    ReturnListUtility<Room> returnData = new ReturnListUtility<>();

	    try {
	        Statement stmt = conn.createStatement();
	        String query = "SELECT r.roomID, r.rDescription, r.pricePerNight, " +
	                "(SELECT COALESCE(AVG(f.rating), 0) " +
	                " FROM Feedback f JOIN RoomHasFeedback rhf ON f.feedbackID = rhf.feedbackID " +
	                " WHERE rhf.roomID = r.roomID) AS overallRating " +
	                "FROM Room r " +
	                "WHERE r.hotelID = " + hotelID + "and r.isBooked = 0";

	        ResultSet rSet = stmt.executeQuery(query);

	        HashMap<Integer, Room> roomList = new HashMap<>();

	        if (!rSet.next()) {
	            // No rooms found for the given hotelID
	            returnData.setMessage("Error: No rooms found for the given hotel ID: " + hotelID);
	            returnData.setSuccess(false);
	        } else {
	            do {
	                // Retrieve room details from the result set
	                int roomID = rSet.getInt("roomID");
	                String description = rSet.getString("rDescription");
	                float pricePerNight = rSet.getFloat("pricePerNight");
	                int overallRating = rSet.getInt("overallRating");

	                // Create a Room object
	                Room room = new Room(roomID, overallRating, pricePerNight, description);

	                // Add the Room object to the HashMap
	                roomList.put(roomID, room);
	            } while (rSet.next());

	            returnData.setList(roomList);
	            returnData.setMessage("Rooms retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage.contains("no such room") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Room table does not exist or is empty.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving rooms from the database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }

	    return returnData;
	}
	public ReturnListUtility<FoodItem> retrieveFoodList(int hotelId) {
	    ReturnListUtility<FoodItem> returnData = new ReturnListUtility<>();

	    try {
	        Statement stmt = conn.createStatement();
	        String query = "SELECT fi.foodID, fi.fName, fi.quantity, fi.price " +
                    "FROM FoodItem fi " +
                    "JOIN KitchenHasFood khf ON fi.foodID = khf.foodID " +
                    "JOIN Kitchen k ON k.kitchenID = khf.kitchenID WHERE fi.quantity > 0 and k.hotelID = " + hotelId;
	        ResultSet rSet = stmt.executeQuery(query);

	        HashMap<Integer, FoodItem> foodList = new HashMap<>();

	        if (!rSet.next()) {
	            // No food items found for the given hotelID
	            returnData.setMessage("Error: No food items found for the given hotel ID: " + hotelId);
	            returnData.setSuccess(false);
	        } else {
	            do {
	                // Retrieve food item details from the result set
	                int foodID = rSet.getInt("foodID");
	                String name = rSet.getString("fName");
	                int quantity = rSet.getInt("quantity");
	                float price = rSet.getFloat("price");

	                // Create a FoodItem object
	                FoodItem foodItem = new FoodItem(foodID, name, quantity, price);

	                // Add the FoodItem object to the HashMap
	                foodList.put(foodID, foodItem);
	            } while (rSet.next());

	            returnData.setList(foodList);
	            returnData.setMessage("Food items retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage.contains("no such table") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Required table does not exist or is empty.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving food items from the database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }

	    return returnData;
	}

	public ReturnListUtility<RoomWithHotel> getBookedRoomDetails(int touristID) {
	    ReturnListUtility<RoomWithHotel> returnData = new ReturnListUtility<>();

	    try {
	        Statement stmt = conn.createStatement();
	        
	        // Updated query to get booked rooms for the tourist along with hotel ID
	        String query = "SELECT r.roomID, r.rDescription, r.pricePerNight, r.hotelID, h.hName, " +
	                "(SELECT COALESCE(AVG(f.rating), 0) " +
	                " FROM Feedback f JOIN RoomHasFeedback rhf ON f.feedbackID = rhf.feedbackID " +
	                " WHERE rhf.roomID = r.roomID) AS overallRating " +
	                "FROM Room r " +
	                "JOIN TouristHasBookedRooms tbr ON r.roomID = tbr.roomID " +
	                "JOIN Hotel h ON r.hotelID = h.hotelID " +
	                "WHERE tbr.touristID = " + touristID;

	        ResultSet rSet = stmt.executeQuery(query);

	        HashMap<Integer, RoomWithHotel> roomList = new HashMap<>();

	        if (!rSet.next()) {
	            // No rooms found for the given touristID
	            returnData.setMessage("Error: No rooms found for the given tourist ID: " + touristID);
	            returnData.setSuccess(false);
	        } else {
	            do {
	                // Retrieve room details from the result set
	                int roomID = rSet.getInt("roomID");
	                String description = rSet.getString("rDescription");
	                float pricePerNight = rSet.getFloat("pricePerNight");
	                int hotelID = rSet.getInt("hotelID");
	                String hotelName = rSet.getString("hName");
	                int overallRating = rSet.getInt("overallRating");

	                // Create RoomWithHotel object (with both room and hotel information)
	                RoomWithHotel roomWithHotel = new RoomWithHotel(roomID, pricePerNight, description, hotelID, hotelName);

	                // Add the RoomWithHotel object to the HashMap
	                roomList.put(roomID, roomWithHotel);
	            } while (rSet.next());

	            returnData.setList(roomList);
	            returnData.setMessage("Booked rooms retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage.contains("no such room") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Room table does not exist or is empty.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving rooms from the database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }
	    return returnData;
	}

	public ReturnObjectUtility<Integer> getHotelOwnerID(int foodID) {
	    ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility<>();
	    try {
	        String sql = "SELECT HOH.hotelOwnerID FROM KitchenHasFood KHF JOIN HotelHasKitchen HHK ON KHF.kitchenID = HHK.kitchenID JOIN HotelOwnerOwnsHotel HOH ON HHK.hotelID = HOH.hotelID WHERE KHF.foodID = ?";

	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, foodID); // Set the foodID parameter
	        
	        ResultSet rSet = pstmt.executeQuery();
	        
	        if (rSet.next()) { // Check if a result was found
	            int hotelOwnerID = rSet.getInt("hotelOwnerID");

	            returnData.setObject(hotelOwnerID);
	            returnData.setMessage("HotelOwnerID retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: No hotel owner associated with the given foodID.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("foreign key constraint")) {
	            returnData.setMessage("Error: Invalid foodID reference.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving HotelOwnerID from database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }
	    
	    return returnData;
	}
	public ReturnObjectUtility<Float> getOverallRating(int hotelID) {
	    ReturnObjectUtility<Float> returnData = new ReturnObjectUtility<>();

	    try {
	        // Create the SQL query to retrieve the average rating for the bus
	        String query = "SELECT ROUND(AVG(CAST(f.rating AS FLOAT)), 2) AS overallAverageRating " +
	                       "FROM RoomHasFeedback rhf " +
	                       "JOIN Feedback f ON rhf.feedbackID = f.feedbackID " +
	                       "JOIN Room r ON r.roomID = rhf.roomID " +
	                       "WHERE f.typeOfFeedback = 'room' and r.hotelID = " + hotelID;
	        
	        // Execute the query
	        Statement stmt = conn.createStatement();
	        ResultSet rSet = stmt.executeQuery(query);

	        // Check if a result is found
	        if (rSet.next()) {
	            float overallAverageRating = rSet.getFloat("overallAverageRating");

	            // Set the retrieved rating in the return object
	            returnData.setObject(overallAverageRating);
	            returnData.setMessage("Overall rating retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: No feedback found for this bus.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();
	        
	        if (errorMessage.contains("no such bus") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Bus does not exist.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving bus rating from database: " + e.getMessage());
	        }

	        returnData.setSuccess(false);
	    }
	    
	    return returnData;
	}
	public static ReturnListUtility<FeedbackWithRoomID> retrieveFeedbackList(int hotelID) {
	    ReturnListUtility<FeedbackWithRoomID> returnData = new ReturnListUtility<>();
	    
	    try {
	        // Create statement and execute the feedback query
	        Statement stmt = conn.createStatement();
	        ResultSet rSet = stmt.executeQuery("SELECT f.feedbackID, f.comment, rhf.roomID FROM RoomHasFeedback rhf JOIN Feedback f ON rhf.feedbackID = f.feedbackID " +
	                                           "JOIN Room r ON r.roomID = rhf.roomID " +
	                                           "WHERE f.typeOfFeedback = 'room' and r.hotelID = " + hotelID);
	      //SELECT f.feedbackID, f.comment, rhf.roomID FROM RoomHasFeedback rhf JOIN Feedback f ON rhf.feedbackID = f.feedbackID JOIN Room r ON r.roomID = rhf.roomID 
	        //WHERE f.typeOfFeedback = 'room' and r.hotelID = 1
	        // List to store feedback details
	        HashMap<Integer, FeedbackWithRoomID> feedbackList=new HashMap<Integer, FeedbackWithRoomID>();
	        // Process the result set
	        while (rSet.next()) {
	        	int feedbackID = rSet.getInt("feedbackID");
	            int roomId = rSet.getInt("roomID");
	            String comment = rSet.getString("comment");

	            // Create FeedbackWithBusID object and add to the list
	            FeedbackWithRoomID feedback = new FeedbackWithRoomID(feedbackID, roomId, comment);
	            feedbackList.put(feedback.getFeedbackID(), feedback);
	        }

	        // Set the result in the returnData object
	        if (feedbackList.isEmpty()) {
	            returnData.setMessage("No feedback found for buses.");
	            returnData.setSuccess(false);
	        } else {
	            returnData.setList(feedbackList);
	            returnData.setMessage("Feedback details retrieved successfully.");
	            returnData.setSuccess(true);
	        }
	        
	    } catch (SQLException e) {
	        // Handle SQL exceptions
	        returnData.setMessage("Error retrieving feedback details: " + e.getMessage());
	        returnData.setSuccess(false);
	    }

	    return returnData;
	}
	
	
}