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
import accountAndPersonModels.HotelOwner;
import accountAndPersonModels.Tourist;
import application.Feedback;
import dataUtilityClasses.ReturnObjectUtility;
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

	public ReturnObjectUtility<Integer> checkPassword(String enteredPassword, String username) {
		ReturnObjectUtility<Integer> returnData=new ReturnObjectUtility<Integer>();
		PreparedStatement pstmt;
		try {
			 Statement stmt = conn.createStatement();
		        ResultSet rSet = stmt.executeQuery("select accPassword, accountID from account where username='" + username+"'");
		        
		        if (rSet.next()) { // Check if a result was found
		            String accPassword= rSet.getString("accPassword");
			        int accountID=rSet.getInt("accountID");
		            Statement stmtForAccount = conn.createStatement();
			        ResultSet rSetForAccount = stmt.executeQuery("select touristID from account inner join tourist on tourist.accountID=account.accountID where account.accountID=" + accountID);
			        if(!rSetForAccount.next()) {
			        	returnData.setMessage("This username does not belong to a tourist account!");
			            returnData.setSuccess(false);
			            return returnData;
			        }
		            
			        int touristID=rSetForAccount.getInt("touristID");
			        if(accPassword.equals(enteredPassword)) {
			        	returnData.setObject(touristID);
			            returnData.setMessage("Logged in successfully");
			            returnData.setSuccess(true);
			            return returnData;
			        }
			        else {
			        	returnData.setObject(touristID);
			            returnData.setMessage("Incorrect Password");
			            returnData.setSuccess(false);
			            return returnData;
			        }

		        } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: User does not exist.");
		            returnData.setSuccess(false);
		        }
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in logging in: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

            returnData.setSuccess(false);
		}
		return returnData;
	}
	
	public ReturnObjectUtility<Integer> compareOldPassword(String enteredPassword, int touristID) {
		ReturnObjectUtility<Integer> returnData=new ReturnObjectUtility<Integer>();
		PreparedStatement pstmt;
		try {
			 Statement stmt = conn.createStatement();
		        ResultSet rSet = stmt.executeQuery("select account.accountID, account.accPassword from account inner join tourist on tourist.accountID=account.accountID where touristID=" + touristID+"");
		        
		        int accountID=0;
		        if (rSet.next()) { // Check if a result was found
		            String accPassword= rSet.getString("accPassword");
			        accountID=rSet.getInt("accountID");
		            
			        Statement stmtForAccount = conn.createStatement();
			        
			        if(accPassword.equals(enteredPassword)) {
			        	returnData.setObject(accountID);
			            returnData.setMessage("Correct Old Password");
			            returnData.setSuccess(true);
			            return returnData;
			        }
			        else {
			        	returnData.setObject(accountID);
			            returnData.setMessage("Please correctly enter your old password.");
			            returnData.setSuccess(false);
			            return returnData;
			        }
		        } else {
		            // If no result is found, set an error message
		            returnData.setMessage("Error: User does not exist.");
		            returnData.setSuccess(false);
		        }
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in changing password: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

            returnData.setSuccess(false);
		}
		return returnData;
	}
	
	public ReturnObjectUtility<Tourist> updatePassword(String password, int accountID) {
		ReturnObjectUtility<Tourist> returnData=new ReturnObjectUtility();
		PreparedStatement pstmt;
		try {
			 //first update travel agency owner
			 String sql = "UPDATE account SET accPassword= ? where accountID= ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setString(1, password);
			 pstmt.setObject(2, accountID);
			 
			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 returnData.setMessage("Password updated successfully.");
				 returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to update password.");
				 returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in updating password in db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }
			returnData.setSuccess(false);
		}
		return returnData;
	}
	
	//tourist account related
	public ReturnObjectUtility<Tourist> deleteTourist(int touristID) {
	    ReturnObjectUtility<Tourist> returnData = new ReturnObjectUtility<>();
	    PreparedStatement pstmt;
	    ResultSet rs;

	    try {
	        
	        Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("SELECT COUNT(*) AS bookedRooms FROM TouristHasBookedRooms WHERE touristID="+touristID);
	        
	        int busID=0;
	        if(rSet.next()) {
	        	int nRooms= rSet.getInt("bookedrooms");
	        	if(nRooms>0) {
	                returnData.setMessage("You have booked hotel rooms. Your account cannot be deleted");
	                returnData.setSuccess(false);
	                return returnData;
	        	}
	        }
	        
	        stmt=conn.createStatement();
	        rSet=stmt.executeQuery("SELECT COUNT(*) AS rentedCars FROM TouristHasRentedCars WHERE touristID="+touristID);
	        if(rSet.next()) {
	        	int nCars= rSet.getInt("rentedCars");
	        	if(nCars>0) {
	                returnData.setMessage("You have rented cars. Your account cannot be deleted");
	                returnData.setSuccess(false);
	                return returnData;
	        	}
	        }
	        
	        stmt=conn.createStatement();
	        rSet=stmt.executeQuery("SELECT COUNT(*) AS bookedSeats FROM TouristHasBookedSeats WHERE touristID="+touristID);
	        if(rSet.next()) {
	        	int nSeats= rSet.getInt("bookedSeats");
	        	if(nSeats>0) {
	                returnData.setMessage("You have booked seats in buses. Your account cannot be deleted");
	                returnData.setSuccess(false);
	                return returnData;
	        	}
	        }
	        
	        stmt=conn.createStatement();
	        rSet=stmt.executeQuery("SELECT accountid from tourist where touristID="+touristID);
	        int accID=0;
	        if(rSet.next()) {
	        	accID= rSet.getInt("accountid");
	        }
	        
	        String sql = "Delete from tourist where touristID="+touristID;
	        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        
	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            rs = pstmt.getGeneratedKeys();
	            if (rs.next()) {
	            	int retrievedAccountID = rs.getInt(1);
	                returnData.setMessage("Your account has been deleted successfully!");
	                returnData.setSuccess(true);
	            }
	        }  else {
                returnData.setMessage("Your account could not be deleted. Try again later");
                returnData.setSuccess(false);
                return returnData;
            }
	        
	        

	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage != null) {
	            if (errorMessage.contains("foreign key constraint")) {
	                returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
	            }else {
	                returnData.setMessage("Issue in deleting tourist in DB: " + errorMessage);
	            }
	        } else {
	            returnData.setMessage("An unknown error occurred.");
	        }
	        returnData.setSuccess(false);
	    }
	    return returnData;
	}
	
	public ReturnObjectUtility<Tourist> updateTourist(Tourist tourist) {
			ReturnObjectUtility<Tourist> returnData=new ReturnObjectUtility();
			PreparedStatement pstmt;
			try {
				 //first update travel agency owner
				 String sql = "UPDATE tourist SET tname = ?, dob = ?, cnic= ? where touristID= ?";
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
				 sql = "UPDATE Account SET username = ?, email= ?, accPassword= ?, balance=balance+? where AccountID= ?";
				 pstmt = conn.prepareStatement(sql);
				    
				 Account account=tourist.getAccount();
				 
				 // Set parameters
				 pstmt.setString(1, account.getUsername());
				 pstmt.setObject(2, account.getEmail());
				 pstmt.setString(3, account.getPassword());
				 pstmt.setFloat(4, account.getBalance());
				 pstmt.setInt(5, account.getAccountID());
				 
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
	        else if(rSet.getBoolean("rentalStatus")){
	        	 returnData.setMessage("Car is already rented.");
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
		            returnData.setMessage("Successfully retrieved generated Transaction ID.");
		            returnData.setSuccess(true);
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

	public ReturnObjectUtility<Integer> removeCarFromRentedCars(int touristId,int carID){
		ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility();
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
		    	rs = pstmt.getGeneratedKeys();
		        if (rs.next()) {
		        	int tID = rs.getInt(1);
		        	returnData.setObject(tID); 
		            returnData.setMessage("Car returned successfully.");
		            returnData.setSuccess(true);
		        } else {
		            returnData.setMessage("Failed to retrieve generated Transaction ID.");
		            returnData.setSuccess(false);
		            return returnData;
		        }
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
	        else if(rSet.getInt("isBooked")==1){
	        	 returnData.setMessage("Seat is already booked.");
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
					 returnData.setMessage("Retrieved generated Transaction ID.");
			         returnData.setSuccess(true);
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

	public ReturnObjectUtility<Integer> orderFood(int touristId,int foodID){
		ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility();
		PreparedStatement pstmt;
		ResultSet rs;

		try {
		    String sql = "INSERT INTO TransactionHistory (touristID, serviceID, typeOfTransaction) VALUES ( ?, ?, ?)";
		    pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    pstmt.setInt(1, touristId);
		    pstmt.setInt(2, foodID);
		    pstmt.setString(3, "Order Food");
		    
		    // Execute the insert
		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	rs = pstmt.getGeneratedKeys();
		        if (rs.next()) {
		        	int tID = rs.getInt(1);
		        	returnData.setObject(tID); 
					 returnData.setMessage("Retrieved generated Transaction ID.");
			         returnData.setSuccess(true);
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

	public ReturnObjectUtility<Integer> addRoomToBookedRooms(int touristId,int roomID){
		ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility();
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
	        else if(rSet.getBoolean("isBooked")){
	        	 returnData.setMessage("Room is already booked.");
		         returnData.setSuccess(false);
		         return returnData;
	        }
	        
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
		    	rs = pstmt.getGeneratedKeys();
		        if (rs.next()) {
		        	int tID = rs.getInt(1);
		            returnData.setMessage("Successfully retrieved generated Transaction ID.");
		        	returnData.setObject(tID); 
		            returnData.setSuccess(true);
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
	public ReturnObjectUtility<Tourist> retrieveTouristData(int touristID) {
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
	
	
	public ReturnObjectUtility<Tourist> retrieveAllTouristData(int touristID) {
	    ReturnObjectUtility<Tourist> returnData = new ReturnObjectUtility<>();

	    try {
	        Statement stmt = conn.createStatement();
	        String query = "SELECT t.touristId, t.tName, t.dob, t.cnic, a.accountID, a.username, a.accpassword, a.email, a.balance FROM Tourist t JOIN Account a ON t.accountID = a.accountID WHERE t.touristId = " + touristID;

	        ResultSet rSet = stmt.executeQuery(query);

	        if (rSet.next()) { // Check if a result was found
	            // Retrieve Tourist details
	            int touristIDRetrieved = rSet.getInt("touristId");
	            String name = rSet.getString("tName");
	            Date date = rSet.getDate("dob");
	            LocalDate dob = date.toLocalDate();
	            String cnic = rSet.getString("cnic");

	            // Create a Tourist object
	            Tourist tourist = new Tourist(touristIDRetrieved, name, dob, cnic);

	            // Retrieve Account details
	            int accountID = rSet.getInt("accountID");
	            String username = rSet.getString("username");
	            String email = rSet.getString("email");
	            String password = rSet.getString("accPassword");
	            float balance = rSet.getFloat("balance");

	            // Create an Account object
	            Account account = new Account(accountID, username, password, email, balance);

		        tourist.setAccount(account);
	            // Set the result and success message
	            returnData.setObject(tourist);
	            returnData.setMessage("Tourist and account data retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Tourist does not exist.");
	            returnData.setSuccess(false);
	        }

	    } catch (SQLException e) {
	        String errorMessage = e.getMessage().toLowerCase();

	        if (errorMessage.contains("no such tourist") || errorMessage.contains("does not exist") || errorMessage.contains("no current")) {
	            returnData.setMessage("Error: Tourist does not exist.");
	        } else {
	            // General case for other SQL exceptions
	            returnData.setMessage("Issue in retrieving tourist from database: " + e.getMessage());
	        }
	    }
	    return returnData;
	}

	public ReturnObjectUtility<Integer> removeRoomFromBookedRooms(int touristId,int roomID){
		ReturnObjectUtility<Integer> returnData = new ReturnObjectUtility();
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
		    rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		    	rs = pstmt.getGeneratedKeys();
		        if (rs.next()) {
		        	int tID = rs.getInt(1);
		            returnData.setMessage("Successfully retrieved generated Transaction ID.");
		        	returnData.setObject(tID); 
		            returnData.setSuccess(true);
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

		        if (rowsAffected <= 0) {
		            returnData.setMessage("Failed to update transaction");
		            returnData.setSuccess(false);
		        }
		        else {
		        	 returnData.setMessage("Transaction updated");
			         returnData.setSuccess(true);
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
		        else {
		        	returnData.setMessage("Transac");
		            returnData.setSuccess(true);
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
