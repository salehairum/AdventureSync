package dbHandlers;

import java.sql.Connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import accountAndPersonModels.Account;
import accountAndPersonModels.TravelAgencyOwner;
import travelAgencyModels.Car;

public class TravelAgencyDBHandler {
	private Connection conn;
	public TravelAgencyDBHandler(Connection c) {
		conn=c;
	}
	
	//car related functions
	public String addCar(Car car) {
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
		                    return "Car added successfully with ID: " + newCarId;
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
		        if (errorMessage.contains("unique constraint") || errorMessage.contains("duplicate")) {
		            return "Please enter a unique plate number.";
		        } else if (errorMessage.contains("foreign key constraint")) {
		            return "Error: Invalid reference. Check if the related data exists.";
		        } else {
		            return "Issue in adding car to db: " + errorMessage;
		        }
		    } else {
		        return "An unknown error occurred.";
		    }
		}
	}
	
	public String updateCar(Car car) {
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
		         return "Car updated successfully.";
			 } else {
				 return "Failed to add car.";
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("unique constraint") || errorMessage.contains("duplicate")) {
		            return "Please enter a unique plate number.";
		        } else if (errorMessage.contains("foreign key constraint")) {
		            return "Error: Invalid reference. Check if the related data exists.";
		        } else {
		            return "Issue in updating car in db: " + errorMessage;
		        }
		    } else {
		        return "An unknown error occurred.";
		    }
		}
	}
	
	public String deleteCar(int carID) {
		try {
			Statement stmt=conn.createStatement();
	        ResultSet rSet=stmt.executeQuery("select rentalStatus from car where carID="+carID);
	        
	        //car is already rented
	        while(rSet.getBoolean(1)) {
	        	return "Car is already rented";
	        }

	        //now delete car
			String deleteQuery = "DELETE FROM car WHERE carId = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, carID);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
            	return "Car with id "+carID+" deleted successfully";
            } else {
            	return "Car could not be deleted";
            }
		}
		catch(SQLException e){
			String errorMessage = e.getMessage().toLowerCase();
		    
		    if (errorMessage.contains("foreign key constraint") || errorMessage.contains("integrity constraint")) {
		        return "Error: Cannot delete car as it is referenced in rental records. Remove related records first.";
		    } else if (errorMessage.contains("no such car") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
		       return "Error: Car does not exist.";
		    } else {
		        return "Issue in deleting car from database: " + e.getMessage();
		    }
		}
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
	        } else {
	            // If no result is found, set an error message
	            returnData.setMessage("Error: Car does not exist.");
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
		}
        return returnData;
	}
	
	public String updateCarRentalStatus(int carID, boolean rentalStatus) {
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
				 if(rentalStatus)
					 return "Car is now rented.";
				 else
					 return "Car is no longer rented.";
			 } else {
				 return "Failed to update car rental status.";
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("no such car") || errorMessage.contains("does not exist") ||errorMessage.contains("no current")) {
				       return "Error: Car does not exist.";
		        }else {
		            return "Issue in updating car in db: " + errorMessage;
		        }
		    } else {
		        return "An unknown error occurred.";
		    }
		}
	}

	//travel agency owner functions
	public String updateAgencyOwner(TravelAgencyOwner owner) {
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
				 return "Failed to update travel agency owner.";
			 }
			 
			 //now update account
			 sql = "UPDATE Account SET username = ?, email= ?, accPassword= ? where AccountID= ?";
			 pstmt = conn.prepareStatement(sql);
			    
			 Account account=owner.getAccount();
			 
			 // Set parameters
			 pstmt.setString(1, account.getUsername());
			 pstmt.setObject(2, account.getEmail());
			 pstmt.setString(3, account.getPassword());
			 
			 //get acc id
			 //pstmt.setInt(4, account.get);

			 // Execute the insert
			 rowsAffected = pstmt.executeUpdate();
			    
			 if (rowsAffected > 0) {
		         return "Travel Agency Owner updated successfully.";
			 } else {
				 return "Failed to update travel agency owner.";
			 }
			 
		} catch (SQLException e) {
			String errorMessage = e.getMessage().toLowerCase();
			
			if (errorMessage != null) {
		        if (errorMessage.contains("foreign key constraint")) {
		            return "Error: Invalid reference. Check if the related data exists.";
		        } else {
		            return "Issue in updating travel agency owner in db: " + errorMessage;
		        }
		    } else {
		        return "An unknown error occurred.";
		    }
		}
	}
}
