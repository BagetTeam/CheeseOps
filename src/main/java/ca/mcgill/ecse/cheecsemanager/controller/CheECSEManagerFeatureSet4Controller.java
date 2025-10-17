package ca.mcgill.ecse.cheecsemanager.controller;

import java.sql.Date;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.*;

/**
 * @author Eun-jun Chang 
 */
public class CheECSEManagerFeatureSet4Controller {
	
	/**
	 * Buys multiple cheese wheels from a farmer and register in the system.
	 * @param emailFarmer Email of the farmer selling the cheese.
	 * @param purchaseDate Date of the purchase.
	 * @param nrCheeseWheels Number of cheese wheels to buy.
	 * @param monthsAged monthsAged maturation period.
	 * @return error message, empty string if successful. 
	 */
	public static String buyCheeseWheels(String emailFarmer, Date purchaseDate, Integer nrCheeseWheels,
			String monthsAged) {

		var manager = CheECSEManagerApplication.getCheecseManager();

		if(emailFarmer == null || emailFarmer.trim().isEmpty()) {
			return "Farmer email cannot be empty.";
		}
		if(purchaseDate == null) {
			return "Purchase date cannot be empty.";
		}
		if(nrCheeseWheels == null || nrCheeseWheels <= 0) {
			return "Number of cheese wheels must be positive.";
		}
		if(monthsAged == null || monthsAged.trim().isEmpty()) {
			return "Maturation period must be specified.";
		}
		
		//Find the farmer by email
		Farmer farmer = null;
		for (Farmer f : manager.getFarmers()){
			if (f.getEmail().equalsIgnoreCase(emailFarmer.trim())){
				farmer = f;
				break;
			}
		}
		if(farmer == null){
			return "Farmer with email " + emailFarmer + " not found.";
		}

		try {
			var purchase = new Purchase(purchaseDate, manager, farmer);							//Create purchase
			for (int i = 0; i < nrCheeseWheels; i++) {											//Add cheese wheels to purchase
				try {
					var period = CheeseWheel.MaturationPeriod.valueOf(monthsAged);				//Invalid maturation period
					purchase.addCheeseWheel(period, false, manager);
				}catch (IllegalArgumentException e) {
					return "Invalid maturation period: " + monthsAged;
				}
			}
		}catch (Exception e) {
			return "Error while purchasing: " + e.getMessage();
		}
		return "";
	}

	/**
	 * Assigns cheese wheel to a specific shelf and position.
	 * @param cheeseWheelID ID of the cheese wheel.
	 * @param shelfID ID of the shelf.
	 * @param columnNr Column number of the shelf.
	 * @param rowNr Row number of the shelf.
	 * @return error message, empty string if successful.
	 */
	public static String assignCheeseWheelToShelf(Integer cheeseWheelID, String shelfID, Integer columnNr,
			Integer rowNr) {
		
		var manager = CheECSEManagerApplication.getCheecseManager();
		
		if(cheeseWheelID == null) {
			return "Cheese wheel ID cannot be null";
		}
		if(shelfID == null || shelfID.trim().isEmpty()) {
			return "Shelf ID cannot be empty.";
		}
		if(columnNr == null || rowNr == null) {
			return "Column and row numbers cannot be null.";
		}
		
		//Find cheese wheel
		CheeseWheel cheese = null;
		for(CheeseWheel c : manager.getCheeseWheels()) {
			if(c.getId() == cheeseWheelID) {
				cheese = c;
				break;
			}
		}
		if(cheese == null) {
			return "Cheese wheel with ID " + cheeseWheelID + " not found.";
		}
		
		//Find shelf
		Shelf shelf = null;
		for(Shelf s : manager.getShelves()) {
			if(s.getId().equals(shelfID)) {
				shelf = s;
				break;
			}
		}
		if(shelf == null) {
			return "Shelf with ID " + shelfID + " not found.";
		}
		
		//Check if cheese is already assigned
		if(cheese.hasLocation()) {
			return "Cheese wheel is already assigned to a shelf.";
		}
		
		//Create shelf location and link cheese
		ShelfLocation newLocation = new ShelfLocation(columnNr, rowNr, shelf);
		boolean linked = newLocation.setCheeseWheel(cheese);
		if(!linked) {
			return "Failed to assign cheese wheel to shelf location.";
		}
		
		//Add location to shelf
		boolean added = shelf.addLocation(newLocation);
		if(!added) {
			return "Failed to add location to shelf.";
		}
		
		return "";
	}

	/**
	 * Removes a cheese wheel from its assigned shelf location.
	 * @param cheeseWheelID ID of the cheese wheel to remove.
	 * @return error message, empty string if successful.
	 */
	public static String removeCheeseWheelFromShelf(Integer cheeseWheelID) {
		
		var manager = CheECSEManagerApplication.getCheecseManager();
		
		if(cheeseWheelID == null) {
			return "Cheese wheel ID cannot be null.";
		}
		
		//Find cheese wheel
		CheeseWheel cheese = null;
		for(CheeseWheel c : manager.getCheeseWheels()) {
			if(c.getId() == cheeseWheelID) {
				cheese = c;
				break;
			}
		}
		if(cheese == null) {
			return "Cheese wheel with ID " + cheeseWheelID + " not found.";
		}
		
		//Get location
		ShelfLocation location = cheese.getLocation();
		if(location == null) {
			return "This cheese wheel is not currently assigned to a shelf.";
		}
		
		//Remove cheese link from shelf location
		boolean removed = location.setCheeseWheel(null);
		if(!removed) {
			return "Failed to remove cheese wheel from the shelf.";
		}
		
		return "";
	}

}
