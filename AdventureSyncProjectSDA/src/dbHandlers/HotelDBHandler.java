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
import hotelModels.FoodItem;
import hotelModels.Hotel;
import hotelModels.Kitchen;
import hotelModels.Room;
import travelAgencyModels.Bus;
import travelAgencyModels.Car;
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
	public String addRoom(Room room) {
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
		                    int newRoomId = generatedKeys.getInt(1);
		                    return "Car added successfully with ID: " + newRoomId;
		                } else {
		                    return "Car added, but ID retrieval failed.";
		                }
		            }
			 } else {
				 return "Failed to add car.";
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		            return "Error: Invalid reference. Check if the related data exists.";
		        } else {
		            return "Issue in adding room to db: " + errorMessage;
		        }
		    } else {
		        return "An unknown error occurred.";
		    }
		}
	}
	
	//hotel owner related functions
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

}