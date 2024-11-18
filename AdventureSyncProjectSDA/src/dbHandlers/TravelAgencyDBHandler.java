package dbHandlers;

import java.sql.Connection;
import java.sql.Date;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import accountAndPersonModels.Account;
import accountAndPersonModels.HotelOwner;
import accountAndPersonModels.TravelAgencyOwner;
import travelAgencyModels.Car;
import travelAgencyModels.Tour;

public class TravelAgencyDBHandler {
	static private Connection conn;
	
	//constructors
	public TravelAgencyDBHandler() {}
	
	public TravelAgencyDBHandler(Connection c) {
		conn = c;
	}
	
	public static Connection getConnection() {
		return conn;
	}

	public static void setConnection(Connection conn) {
		TravelAgencyDBHandler.conn = conn;
	}

	//car related functions
	public ReturnObjectUtility<Boolean> addCar(Car car) {
		ReturnObjectUtility<Boolean> returnData=new ReturnObjectUtility<Boolean>();
		PreparedStatement pstmt;
		try {
			 String sql = "INSERT INTO Car (brand, model, manufactureYear, plateNumber, rentalStatus, rentalFee, costPerKm) VALUES (?, ?, ?, ?, 0, ?, ?);";
			 pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			    
			 // Set parameters
			 pstmt.setString(1, car.getBrand());
			 pstmt.setString(2, car.getModel());
			 pstmt.setInt(3, car.getYear());
			 pstmt.setString(4, car.getPlateNumber());
			 pstmt.setFloat(5, car.getRentalFee());
			 pstmt.setFloat(6, car.getCostPerKm()); 

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
		                if (generatedKeys.next()) {
		                    int newCarId = generatedKeys.getInt(1);
		                    returnData.setMessage("Car added successfully with ID: " + newCarId);
		                } else {
		                	returnData.setMessage("Car added, but ID retrieval failed.");
		                }
		            }
                 returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to add car.");
                 returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("unique constraint") || errorMessage.contains("duplicate")) {
		        	returnData.setMessage("Please enter a unique plate number.");
		        } else if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in adding car to db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

            returnData.setSuccess(false);
		}
		return returnData;
	}
	
	public ReturnObjectUtility<Boolean> updateCar(Car car) {
		ReturnObjectUtility<Boolean> returnData=new ReturnObjectUtility<Boolean>();
		PreparedStatement pstmt;
		try {
			 String sql = "UPDATE Car SET brand = ?, model = ?, manufactureYear = ?, plateNumber = ?, rentalFee = ?, costPerKm = ? WHERE carID = ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setString(1, car.getBrand());
			 pstmt.setString(2, car.getModel());
			 pstmt.setInt(3, car.getYear());
			 pstmt.setString(4, car.getPlateNumber());
			 pstmt.setFloat(5, car.getRentalFee());
			 pstmt.setFloat(6, car.getCostPerKm()); 
			 pstmt.setInt(7, car.getID());

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 returnData.setMessage("Car with car ID "+car.getID()+" updated successfully.");			
				 returnData.setSuccess(true);
			 } else {
				returnData.setMessage("Failed to update car.");
				returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("unique constraint") || errorMessage.contains("duplicate")) {
		        	returnData.setMessage("Please enter a unique plate number.");
		        } else if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in updating car in db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }
			returnData.setSuccess(false);
		}
		return returnData;
	}
	
	public ReturnObjectUtility<Boolean> deleteCar(int carID) {
		ReturnObjectUtility<Boolean> returnData=new ReturnObjectUtility<Boolean>();
		try {
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select rentalStatus from car where carID="+carID);
	        
	        //car is already rented
	        while(rSet.getBoolean(1)) {
	        	returnData.setMessage("Car is already rented");
	        }

	        //now delete car
			String deleteQuery = "DELETE FROM car WHERE carId = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, carID);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
            	returnData.setMessage("Car with id "+carID+" deleted successfully");
            } else {
            	returnData.setMessage("Car could not be deleted");
            }
		}
		catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("foreign key constraint") || errorMessage.contains("integrity constraint")) {
		    	returnData.setMessage("Error: Cannot delete car as it is referenced in rental records. Remove related records first.");
		    } else if (errorMessage.contains("no such car") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		    	returnData.setMessage("Error: Car does not exist.");
		    } else {
		    	returnData.setMessage("Issue in deleting car from database: " + e.getMessage());
		    }
		}
		return returnData;
	}
	
	public ReturnObjectUtility<Car> retrieveCarObject(int carID) {
		ReturnObjectUtility<Car> returnData=new ReturnObjectUtility();
		
		try {
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from car where carID="+carID);
	        
	        if (rSet.next()) { // Check if a result was found
	            // Create and populate a Car object
	            int carIDRetrieved = rSet.getInt("carID");
	            String brand = rSet.getString("brand");
	            String model = rSet.getString("model");
	            int year = rSet.getInt("manufactureYear");
	            String plateNumber = rSet.getString("plateNumber");
	            boolean rentalStatus = rSet.getBoolean("rentalStatus");
	            float rentalFee = rSet.getFloat("rentalFee");
	            float costPerKm = rSet.getFloat("costPerKm");

	            Car car = new Car(carIDRetrieved, brand, model, year, plateNumber, rentalStatus, rentalFee,costPerKm);
	            
	            // Set the Car object and success message
	            returnData.setObject(car);
	            returnData.setMessage("Car data retrieved successfully.");
	            returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Car does not exist.");
	            returnData.setSuccess(false);
	        }
            
		}
		catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("no such car") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		       returnData.setMessage("Error: Car does not exist.");
		    } else {
		        // General case for other SQL exceptions
		    	returnData.setMessage("Issue in retrieving car from database: " + e.getMessage());
		    }

            returnData.setSuccess(false);
		}
        return returnData;
	}
	
	public ReturnListUtility<Car> retrieveCarList() {
		ReturnListUtility<Car> returnData=new ReturnListUtility();
		
		try {
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from car");
	        
	        HashMap<Integer, Car> carList=new HashMap<Integer, Car>();
	        
	        if(!rSet.next()) {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Car does not exist.");
	        }
	        else {
	        	 do { // Check if a result was found
	 	            // Create and populate a Car object
	 	            int carIDRetrieved = rSet.getInt("carID");
	 	            String brand = rSet.getString("brand");
	 	            String model = rSet.getString("model");
	 	            int year = rSet.getInt("manufactureYear");
	 	            String plateNumber = rSet.getString("plateNumber");
	 	            boolean rentalStatus = rSet.getBoolean("rentalStatus");
	 	            float rentalFee = rSet.getFloat("rentalFee");
	 	            float costPerKm = rSet.getFloat("costPerKm");

	 	            Car car = new Car(carIDRetrieved, brand, model, year, plateNumber, rentalStatus, rentalFee,costPerKm);
	 	            
	 	            carList.put(car.getID(), car);
	 	        } while(rSet.next());
	        	 
	        	returnData.setList(carList);
	        	returnData.setMessage("Cars retrieved successfully.");
	            returnData.setSuccess(true);
	        }
            
		}
		catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("no such car") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		       returnData.setMessage("Error: Car does not exist.");
		    } else {
		        // General case for other SQL exceptions
		    	returnData.setMessage("Issue in retrieving cars from database: " + e.getMessage());
		    }
            returnData.setSuccess(false);
		}
        return returnData;
	}

	public ReturnObjectUtility<Car> updateCarRentalStatus(int carID, boolean rentalStatus) {
		ReturnObjectUtility<Car> returnData=new ReturnObjectUtility();
		PreparedStatement pstmt;
		try {
			 String sql = "UPDATE Car SET rentalStatus = ? WHERE carID = ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setBoolean(1,rentalStatus);
			 pstmt.setInt(2, carID);

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 if(rentalStatus) {
					 returnData.setMessage("Car is now rented.");
				 }
				 else {
					 returnData.setMessage("Car is no longer rented.");
				 }

		         returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to update car rental status.");
		         returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("no such car") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		        	returnData.setMessage("Error: Car does not exist.");
		        }else {
		        	returnData.setMessage("Issue in updating car in db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

	        returnData.setSuccess(false);
		}
		return returnData;
	}

	//travel agency owner functions 
	public ReturnObjectUtility<Car> updateAgencyOwner(TravelAgencyOwner owner) {
		ReturnObjectUtility<Car> returnData=new ReturnObjectUtility();
		PreparedStatement pstmt;
		try {
			 //first update travel agency owner
			 String sql = "UPDATE travelAgencyOwner SET aname = ?, dob = ?, cnic= ? where TravelAgencyOwnerId= ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 // Set parameters
			 pstmt.setString(1, owner.getName());
			 pstmt.setObject(2, owner.getDob());
			 pstmt.setString(3, owner.getCnic());
			 pstmt.setInt(4, owner.getAgencyOwnerID());

			 // Execute the insert
			 int rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected <=0 ) {
				 returnData.setMessage("Failed to update travel agency owner.");
				 returnData.setSuccess(false);
				 return returnData;
			 }
			 
			 //now update account
			 sql = "UPDATE Account SET username = ?, email= ?, accPassword= ? where AccountID= ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 Account account=owner.getAccount();
			 
			 // Set parameters
			 pstmt.setString(1, account.getUsername());
			 pstmt.setObject(2, account.getEmail());
			 pstmt.setString(3, account.getPassword());
			 pstmt.setInt(4, account.getAccountID());
			 
			 // Execute the insert
			 rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
				 returnData.setMessage("Travel Agency Owner updated successfully.");
				 returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to update travel agency owner.");
				 returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the related data exists.");
		        } else {
		        	returnData.setMessage("Issue in updating travel agency owner in db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }
			returnData.setSuccess(false);
		}
		return returnData;
	}
	
	public ReturnObjectUtility<TravelAgencyOwner> retrieveTravelAgencyOwnerData(int travelAgencyOwnerID) {
		ReturnObjectUtility<TravelAgencyOwner> returnData = new ReturnObjectUtility();
		
		try {
			Statement stmt = conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select * from TravelAgencyOwner where TravelAgencyOwnerId="+travelAgencyOwnerID);
	        
	        if (rSet.next()) { 
	            int travelAgencyOwnerIDRetrieved = rSet.getInt("TravelAgencyOwnerId");
	            String name = rSet.getString("aName");
	            Date date = rSet.getDate("dob");
	            LocalDate dob = date.toLocalDate();
	            String cnic = rSet.getString("cnic");

	            TravelAgencyOwner travelAgencyOwner = new TravelAgencyOwner(travelAgencyOwnerIDRetrieved, name, dob, cnic);
	            
	            // Set the hotel owner object and success message
	            returnData.setObject(travelAgencyOwner);
	            returnData.setMessage("Travel Agency Owner data retrieved successfully.");
		        returnData.setSuccess(true);
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Travel Agency Owner does not exist.");
	            returnData.setSuccess(false);
	        }
            
		}
		catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("no such Travel Agency owner") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		       returnData.setMessage("Error: Travel Agency Owner does not exist.");
		    } else {
		        // General case for other SQL exceptions
		    	returnData.setMessage("Issue in retrieving Travel Agency owner from database: " + e.getMessage());
		    }
	        returnData.setSuccess(false);
		}
        return returnData;
	}

	//assign tour to bus
	public ReturnObjectUtility<Tour> assignTour(Tour tour){
		ReturnObjectUtility<Tour> returnData = new ReturnObjectUtility();
		
		PreparedStatement pstmt;
		try {
			 String sql = "INSERT INTO Tour (origin, destination, date, busID) VALUES (?, ?, ?, ?);";
			 pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// Set parameters
			pstmt.setString(1, tour.getOrigin());      // Set the origin
			pstmt.setString(2, tour.getDestination()); // Set the destination
			pstmt.setDate(3, new java.sql.Date(tour.getDate().getTime())); // Set the date (ensure it's a java.util.Date object)
			pstmt.setInt(4, tour.getBusID());          // Set the bus ID

			// Execute the insert
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				int newTourId=0;
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
		                newTourId = generatedKeys.getInt(1);
		                returnData.setMessage("Tour added successfully with ID: " + newTourId);
		            } else {
		            	returnData.setMessage("Tour added, but ID retrieval failed.");
		            }
		        }
                returnData.setSuccess(true);
			 } else {
				 returnData.setMessage("Failed to add tour.");
                 returnData.setSuccess(false);
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		        	returnData.setMessage("Error: Invalid reference. Check if the bus/tour exist.");
		        } else {
		        	returnData.setMessage("Issue in adding car to db: " + errorMessage);
		        }
		    } else {
		    	returnData.setMessage("An unknown error occurred.");
		    }

            returnData.setSuccess(false);
		}
		
		return returnData;
	}
}
