package dbHandlers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import accountAndPersonModels.Account;
import accountAndPersonModels.BusDriver;
import accountAndPersonModels.Tourist;
import application.Feedback;
import hotelModels.Room;
import travelAgencyModels.Bus;
import travelAgencyModels.Seat;

public class TouristDBHandler {
	static private Connection conn;
	
	//constructors
	public TouristDBHandler() {}
	
	public TouristDBHandler(Connection c) {
		conn = c;
	}
	
	public static Connection getConnection() {
		return conn;
	}

	public static void setConnection(Connection conn) {
		TouristDBHandler.conn = conn;
	}
	
	//tourist account related
	public ReturnObjectUtility<Tourist> addTourist(Tourist tourist) {
		ReturnObjectUtility<Tourist> returnData = new ReturnObjectUtility<>();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
			// First Insert into Account table
		    String sql = "INSERT INTO Account (username, email, accPassword, balance) VALUES (?, ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    // Set parameters
		    Account account = tourist.getAccount();
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
		        	tourist.getAccount().setAccountID(retrievedAccountID);
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
		        sql = "INSERT INTO tourist (accountID, tName, dob, cnic) VALUES ( ?, ?, ?, ?)";
		        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		        // Set parameters
		        pstmt.setInt(1, tourist.getAccount().getAccountID());
		        pstmt.setString(2, tourist.getName());
		        pstmt.setObject(3, tourist.getDob());
		        pstmt.setString(4, tourist.getCnic());

		        // Execute the insert
		        int retrievedOwnerID=0;
		        rowsAffected = pstmt.executeUpdate();
		        if (rowsAffected > 0) {
		            rs = pstmt.getGeneratedKeys();
		            if (rs.next()) {
		            	retrievedOwnerID = rs.getInt(1);
		            	tourist.setTouristID(retrievedOwnerID);
		            	returnData.setObject(tourist);
		            	returnData.setMessage("Tourist with id "+retrievedOwnerID+" added successfuly!");
		                returnData.setSuccess(true);
		                return returnData;
		            } else {
		                returnData.setMessage("Failed to retrieve generated Account ID.");
		                returnData.setSuccess(false);
		                return returnData;
		            }
		        } else {
		            returnData.setMessage("Failed to insert into Tourist table.");
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
		                returnData.setMessage("Issue in adding Tourist in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
		}

	//tourist account related
	public ReturnObjectUtility<Tourist> updateTourist(Tourist tourist) {
			ReturnObjectUtility<Tourist> returnData=new ReturnObjectUtility();
			PreparedStatement pstmt;
			try {
				 //first update travel agency owner
				 String sql = "UPDATE tourist SET tname = ?, dob = ?, cnic= ? where TravelAgencyOwnerId= ?";
				 pstmt = conn.prepareStatement(sql);
				    
				 // Set parameters
				 pstmt.setString(1, tourist.getName());
				 pstmt.setObject(2, tourist.getDob());
				 pstmt.setString(3, tourist.getCnic());
				 pstmt.setInt(4, tourist.getTouristID());

				 // Execute the insert
				 int rowsAffected = pstmt.executeUpdate();
				    
				 if (rowsAffected <=0 ) {
					 returnData.setMessage("Failed to update tourist.");
					 returnData.setSuccess(false);
					 return returnData;
				 }
				 
				 //now update account
				 sql = "UPDATE Account SET username = ?, email= ?, accPassword= ? where AccountID= ?";
				 pstmt = conn.prepareStatement(sql);
				    
				 Account account=tourist.getAccount();
				 
				 // Set parameters
				 pstmt.setString(1, account.getUsername());
				 pstmt.setObject(2, account.getEmail());
				 pstmt.setString(3, account.getPassword());
				 pstmt.setInt(4, account.getAccountID());
				 
				 // Execute the insert
				 rowsAffected = pstmt.executeUpdate();
				    
				 if (rowsAffected > 0) {
					 returnData.setMessage("Tourist with id "+tourist.getTouristID()+" updated successfully.");
					 returnData.setSuccess(true);
				 } else {
					 returnData.setMessage("Failed to update tourist.");
					 returnData.setSuccess(false);
				 }
				 
			} catch (SQLException e) {
				String errorMessage = e.getMessage().toLowerCase();
				
				if (errorMessage != null) {
			        if (errorMessage.contains("foreign key constraint")) {
			        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
			        } else {
			        	returnData.setMessage("Issue in updating tourist in db: " + errorMessage);
			        }
			    } else {
			    	returnData.setMessage("An unknown error occurred.");
			    }
				returnData.setSuccess(false);
			}
			return returnData;
		}

	public ReturnObjectUtility<Integer> addCarToRentedCars(int touristId,int carID){
		ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
			//first see if car exists or not
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from car where carID="+carID);
	        if(!rSet.next()) {
	        	 returnData.setMessage("No car found with the entered Car ID..");
		         returnData.setSuccess(false);
		         return returnData;
	        }
			//if car exists, proceed with renting 
	        
		    String sql = "INSERT INTO TouristHasRentedCars (touristId,carID) VALUES (?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    // Set parameters
		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, carID);

		    // Execute the insert and fetch generated accountID
		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected <= 0) {
		        returnData.setMessage("Failed to store rented car.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    sql = "INSERT INTO TransactionHistory (touristID, serviceID, typeOfTransaction) VALUES ( ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, carID);
		    pstmt.setString(3, "Rent Car");
		    
		        // Execute the insert
		    rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	rs = pstmt.getGeneratedKeys();
		        if (rs.next()) {
		        	int tID = rs.getInt(1);
		        	returnData.setObject(tID); 
		        } else {
		            returnData.setMessage("Failed to retrieve generated Transaction ID.");
		            returnData.setSuccess(false);
		            return returnData;
		        }
		    	
		    	returnData.setMessage("Car rented successfully.");
		        returnData.setSuccess(true);
		        return returnData;
		    } else {
		    	returnData.setMessage("Failed to rent car.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the car/tourist exists.");
		            } else {
		                returnData.setMessage("Issue in renting car in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
	}

	public ReturnObjectUtility<Tourist> removeCarFromRentedCars(int touristId,int carID){
		ReturnObjectUtility<Tourist> returnData = new ReturnObjectUtility();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
			//first see if car exists or not
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from TouristHasRentedCars where carID="+carID);
	        if(!rSet.next()) {
	        	 returnData.setMessage("No rented car found with the entered Car ID.");
		         returnData.setSuccess(false);
		         return returnData;
	        }
			//if car exists, proceed with renting 
	        
	        String sql = "DELETE FROM TouristHasRentedCars WHERE touristId = ? AND carID = ?";
	        pstmt = conn.prepareStatement(sql);

	        // Set parameters
	        pstmt.setInt(1, touristId);
	        pstmt.setInt(2, carID);

		    // Execute the insert and fetch generated accountID
		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected <= 0) {
		        returnData.setMessage("Failed to return car.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    sql = "INSERT INTO TransactionHistory (touristID, serviceID, typeOfTransaction) VALUES ( ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, carID);
		    pstmt.setString(3, "Return Car");
		    
		        // Execute the insert
		    rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	returnData.setMessage("Car returned successfully.");
		        returnData.setSuccess(true);
		        return returnData;
		    } else {
		    	returnData.setMessage("Failed to return car.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the car/tourist exists.");
		            } else {
		                returnData.setMessage("Issue in returning car in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
	}

	public ReturnObjectUtility<Integer> addSeatToBookedSeats(int touristId,int seatID){
		ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
			//first see if car exists or not
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from seat where seatID="+seatID);
	        if(!rSet.next()) {
	        	 returnData.setMessage("No seat found with the entered Seat ID..");
		         returnData.setSuccess(false);
		         return returnData;
	        }
			//if car exists, proceed with renting 
	        
		    String sql = "INSERT INTO TouristHasBookedSeats (touristId,seatID) VALUES (?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    // Set parameters
		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, seatID);

		    // Execute the insert and fetch generated accountID
		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected <= 0) {
		        returnData.setMessage("Failed to store booked Seat.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    sql = "INSERT INTO TransactionHistory (touristID, serviceID, typeOfTransaction) VALUES ( ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, seatID);
		    pstmt.setString(3, "Book Bus Seat");
		    
		        // Execute the insert
		    rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	rs = pstmt.getGeneratedKeys();
		        if (rs.next()) {
		        	int tID = rs.getInt(1);
		        	returnData.setObject(tID); 
		        } else {
		            returnData.setMessage("Failed to retrieve generated Transaction ID.");
		            returnData.setSuccess(false);
		            return returnData;
		        }
		    }
		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the seat/tourist exists.");
		            } else {
		                returnData.setMessage("Issue in renting seat in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
	}

	public ReturnObjectUtility<Room> addRoomToBookedRooms(int touristId,int roomID){
		ReturnObjectUtility<Room> returnData = new ReturnObjectUtility();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
			//first see if car exists or not
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from room where roomID="+roomID);
	        if(!rSet.next()) {
	        	 returnData.setMessage("No room found with the entered room ID.");
		         returnData.setSuccess(false);
		         return returnData;
	        }
			//if car exists, proceed with renting 
	        
		    String sql = "INSERT INTO TouristHasBookedRooms (touristId,roomID) VALUES (?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    // Set parameters
		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, roomID);

		    // Execute the insert and fetch generated accountID
		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected <= 0) {
		        returnData.setMessage("Failed to store booked room.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    sql = "INSERT INTO TransactionHistory (touristID, serviceID, typeOfTransaction) VALUES ( ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, roomID);
		    pstmt.setString(3, "Book Hotel Room");
		    
		        // Execute the insert
		    rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	returnData.setMessage("Room booked successfully.");
		        returnData.setSuccess(true);
		        return returnData;
		    } else {
		    	returnData.setMessage("Failed to book room.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the room/tourist exists.");
		            } else {
		                returnData.setMessage("Issue in booking room in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
	}
	
	//bus driver related functions
	public static ReturnObjectUtility<Tourist> retrieveTouristData(int touristID) {
				ReturnObjectUtility<Tourist> returnData = new ReturnObjectUtility();
				
				try {
					Statement stmt = conn.createStatement();
			        ResultSet rSet=stmt.executeQuery("select * from Tourist where touristId=" + touristID);
			        
			        if (rSet.next()) { // Check if a result was found
			            // Create and populate a hotel owner object
			            int touristIDRetrieved = rSet.getInt("touristID");
			            String name = rSet.getString("tName");
			            Date date = rSet.getDate("dob");
			            LocalDate dob = date.toLocalDate();
			            String cnic = rSet.getString("cnic");

			            Tourist tourist = new Tourist(touristIDRetrieved, name, dob, cnic);
			            
			            // Set the hotel owner object and success message
			            returnData.setObject(tourist);
			            returnData.setMessage("Tourist data retrieved successfully.");
			        } else {
			            // If no result is found, set an error message
			            returnData.setMessage("Error: Tourist does not exist.");
			        }
		            
				}
				catch(SQLException e){
					String errorMessage = e.getMessage().toLowerCase();
				    
				    if (errorMessage.contains("no such tourist") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
				       returnData.setMessage("Error: Tourist does not exist.");
				    } else {
				        // General case for other SQL exceptions
				    	returnData.setMessage("Issue in retrieving tourist from database: " + e.getMessage());
				    }
				}
		        return returnData;
			}
	
	public ReturnObjectUtility<Room> removeRoomFromBookedRooms(int touristId,int roomID){
		ReturnObjectUtility<Room> returnData = new ReturnObjectUtility();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
			//first see if car exists or not
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from TouristHasBookedRooms where roomID="+roomID);
	        if(!rSet.next()) {
	        	 returnData.setMessage("No booked Room found with the entered Room ID.");
		         returnData.setSuccess(false);
		         return returnData;
	        }
			//if car exists, proceed with renting 
	        
	        String sql = "DELETE FROM TouristHasBookedRooms WHERE touristId = ? AND roomID = ?";
	        pstmt = conn.prepareStatement(sql);

	        // Set parameters
	        pstmt.setInt(1, touristId);
	        pstmt.setInt(2, roomID);

		    // Execute the insert and fetch generated accountID
		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected <= 0) {
		        returnData.setMessage("Failed to checkout from hotel.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    sql = "INSERT INTO TransactionHistory (touristID, serviceID, typeOfTransaction) VALUES ( ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, roomID);
		    pstmt.setString(3, "Checkout Hotel Room");
		    
		        // Execute the insert
		    rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	returnData.setMessage("Room checked out successfully.");
		        returnData.setSuccess(true);
		        return returnData;
		    } else {
		    	returnData.setMessage("Failed to checkout from hotel.");
		        returnData.setSuccess(false);
		        return returnData;
		    }

		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the hotel room/tourist exists.");
		            } else {
		                returnData.setMessage("Issue in checkout from hotel in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
	}
	
	public ReturnObjectUtility<Feedback> giveFeedbackToBus(Feedback feedback) {
	    ReturnObjectUtility<Feedback> returnData = new ReturnObjectUtility<>();
	    PreparedStatement pstmt;
	    ResultSet rs;

	    try {
	        // First, check if the bus exists
	        String checkBusSql = "SELECT * FROM Bus WHERE busID = ?";
	        pstmt = conn.prepareStatement(checkBusSql);
	        int busID=feedback.getServiceID();
	        pstmt.setInt(1,busID );
	        rs = pstmt.executeQuery();

	        if (!rs.next()) {
	            returnData.setMessage("No bus found with the entered bus ID.");
	            returnData.setSuccess(false);
	            return returnData;
	        }

	        // Insert feedback into Feedback table
	        String insertFeedbackSql = "INSERT INTO Feedback (rating, comment, touristID, serviceID, typeOfFeedback) " +
	                                   "VALUES (?, ?, ?, ?, ?)";
	        pstmt = conn.prepareStatement(insertFeedbackSql, Statement.RETURN_GENERATED_KEYS);
	        pstmt.setInt(1, feedback.getRating());
	        pstmt.setString(2, feedback.getComment());
	        pstmt.setInt(3, feedback.getTouristID());
	        pstmt.setInt(4, busID);
	        pstmt.setString(5, feedback.getTypeOfFeedback());

	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected <= 0) {
	            returnData.setMessage("Failed to store feedback.");
	            returnData.setSuccess(false);
	            return returnData;
	        }

	        // Get the generated feedback ID
	        rs = pstmt.getGeneratedKeys();
	        int feedbackID = -1;
	        if (rs.next()) {
	            feedbackID = rs.getInt(1);
	        } else {
	            returnData.setMessage("Failed to retrieve feedback ID.");
	            returnData.setSuccess(false);
	            return returnData;
	        }

	        // Link feedback to bus in BusHasFeedback table
	        String linkFeedbackSql = "INSERT INTO BusHasFeedback (BusID, feedbackID) VALUES (?, ?)";
	        pstmt = conn.prepareStatement(linkFeedbackSql);
	        pstmt.setInt(1, busID);
	        pstmt.setInt(2, feedbackID);

	        rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            returnData.setMessage("Feedback submitted successfully.");
	            returnData.setSuccess(true);
	        } else {
	            returnData.setMessage("Failed to link feedback to bus.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage != null) {
	            if (errorMessage.contains("foreign key constraint")) {
	                returnData.setMessage("Error: Invalid reference. Check if the bus or tourist exists.");
	            } else {
	                returnData.setMessage("Issue in storing feedback in DB: " + errorMessage);
	            }
	        } else {
	            returnData.setMessage("An unknown error occurred.");
	        }
	        returnData.setSuccess(false);
	    }

	    return returnData;
	}

	public ReturnObjectUtility<Feedback> giveFeedbackToRoom(Feedback feedback) {
	    ReturnObjectUtility<Feedback> returnData = new ReturnObjectUtility<>();
	    PreparedStatement pstmt;
	    ResultSet rs;

	    try {
	        // First, check if the bus exists
	        String checkBusSql = "SELECT * FROM Room WHERE RoomID = ?";
	        pstmt = conn.prepareStatement(checkBusSql);
	        int RoomID=feedback.getServiceID();
	        pstmt.setInt(1,RoomID );
	        rs = pstmt.executeQuery();

	        if (!rs.next()) {
	            returnData.setMessage("No Room found with the entered Room ID.");
	            returnData.setSuccess(false);
	            return returnData;
	        }

	        // Insert feedback into Feedback table
	        String insertFeedbackSql = "INSERT INTO Feedback (rating, comment, touristID, serviceID, typeOfFeedback) " +
	                                   "VALUES (?, ?, ?, ?, ?)";
	        pstmt = conn.prepareStatement(insertFeedbackSql, Statement.RETURN_GENERATED_KEYS);
	        pstmt.setInt(1, feedback.getRating());
	        pstmt.setString(2, feedback.getComment());
	        pstmt.setInt(3, feedback.getTouristID());
	        pstmt.setInt(4, RoomID);
	        pstmt.setString(5, feedback.getTypeOfFeedback());

	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected <= 0) {
	            returnData.setMessage("Failed to store feedback.");
	            returnData.setSuccess(false);
	            return returnData;
	        }

	        // Get the generated feedback ID
	        rs = pstmt.getGeneratedKeys();
	        int feedbackID = -1;
	        if (rs.next()) {
	            feedbackID = rs.getInt(1);
	        } else {
	            returnData.setMessage("Failed to retrieve feedback ID.");
	            returnData.setSuccess(false);
	            return returnData;
	        }

	        // Link feedback to bus in BusHasFeedback table
	        String linkFeedbackSql = "INSERT INTO RoomHasFeedback (roomID, feedbackID) VALUES (?, ?)";
	        pstmt = conn.prepareStatement(linkFeedbackSql);
	        pstmt.setInt(1, RoomID);
	        pstmt.setInt(2, feedbackID);

	        rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            returnData.setMessage("Feedback submitted successfully.");
	            returnData.setSuccess(true);
	        } else {
	            returnData.setMessage("Failed to link feedback to bus.");
	            returnData.setSuccess(false);
	        }
	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage != null) {
	            if (errorMessage.contains("foreign key constraint")) {
	                returnData.setMessage("Error: Invalid reference. Check if the Room or tourist exists.");
	            } else {
	                returnData.setMessage("Issue in storing feedback in DB: " + errorMessage);
	            }
	        } else {
	            returnData.setMessage("An unknown error occurred.");
	        }
	        returnData.setSuccess(false);
	    }

	    return returnData;
	}
	
	public ReturnObjectUtility<Tourist> deductMoney(int touristID, float bill, int transactionID, boolean deduct){
		 ReturnObjectUtility<Tourist> returnData = new ReturnObjectUtility<>();
		 PreparedStatement pstmt;
		 String sql;
		 int rowsAffected;
		 try {
				 if(deduct) {				        
				        
					 	sql = "UPDATE account SET balance = balance - ? WHERE accountID = (SELECT accountID FROM tourist WHERE touristID = ?)";
				        pstmt = conn.prepareStatement(sql);
	
				        // Set parameters
				        pstmt.setFloat(1, bill); // Deduction amount
				        pstmt.setInt(2, touristID); // Tourist ID
	
				        // Execute the update
				        rowsAffected= pstmt.executeUpdate();
	
				        if (rowsAffected > 0) {
				            returnData.setMessage("Balance updated successfully for tourist ID: " + touristID);
				            returnData.setSuccess(true);
				        } else {
				            returnData.setMessage("Failed to update balance for tourist ID: " + touristID);
				            returnData.setSuccess(false);
				        }
				 }
			        
		        sql = "UPDATE transactionHistory SET paymentStatus = ? WHERE transactionID =  ?";
		        pstmt = conn.prepareStatement(sql);

		        // Set parameters
		        pstmt.setBoolean(1, true); 
		        pstmt.setInt(2, transactionID); 

		        // Execute the update
		        rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            returnData.setMessage("Transaction updated successfully");
		            returnData.setSuccess(true);
		        } else {
		            returnData.setMessage("Failed to update transaction");
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

	public ReturnObjectUtility<Boolean> checkBalance(int touristID, float bill){
		 ReturnObjectUtility<Boolean> returnData = new ReturnObjectUtility<>();
		 PreparedStatement pstmt;
		 String sql;
		 int rowsAffected;
		 try {
			 Statement stmt = conn.createStatement();
		     ResultSet rSet=stmt.executeQuery("select balance from tourist inner join account on account.accountID=tourist.accountID where touristID=" + touristID);
		     if(rSet.next()) {
		        float balance= rSet.getInt("balance");
		        if(balance<bill) {
		        	returnData.setMessage("You do not have enough balance");
		            returnData.setSuccess(false);
		        }
		     }
		    } catch (SQLException e) {
		        String errorMessage = e.getMessage().toLowerCase();

		        if (errorMessage != null) {
		            if (errorMessage.contains("foreign key constraint")) {
		                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		            } else {
		                returnData.setMessage("Issue in retrieving in DB: " + errorMessage);
		            }
		        } else {
		            returnData.setMessage("An unknown error occurred.");
		        }
		        returnData.setSuccess(false);
		    }
		    return returnData;
		}

}
