package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import containers.ServerResponseDataContainer;
import entities.Category;
import entities.Item;
import enums.ServerResponse;

public class EmployeeQuery {

	
	/**
	 * Adds a new item to the database if it does not already exist.
	 * 
	 * @param dbConn the database connection used to execute the SQL queries
	 * @param item the item to be added to the database
	 * @return a {@link ServerResponseDataContainer} containing the result of the operation and a message
	 */
		public ServerResponseDataContainer AddItemInfo(Connection dbConn, Item item) {
			// we distinguish between two items according to (name, supplierID), if there is  an item with these exact fields we don't permit to add it to the database.

			ServerResponseDataContainer response = new ServerResponseDataContainer();
			String query = "SELECT id FROM items WHERE Name = ? and SupplierID = ?";

			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setString(1, item.getName());
				stmt.setInt(2, item.getSupplierID());

				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						// There is already existing item like this in the database, we cannot add it.
						response.setMessage("This item alread exists in the database");
						response.setResponse(ServerResponse.ADD_FAIL);
					} else {
						// the item does not exist in the database, we can add it. id field we don't insert, the database defines it automatically.
						String insertQuery = "INSERT into items (SupplierID, Name, Category, Description, CustomSize, CustomDoneness, CustomRestrictions, Price)"
								+ "VALUES(?,?,?,?,?,?,?,?);";

						 int size = item.getCustomSize() ? 1 : 0;
		                 int doneness = item.getCustomDonenessDegree() ? 1 : 0;
		                 int restrictions = item.getCustomRestrictions() ? 1 : 0;

						try (PreparedStatement insertStm = dbConn.prepareStatement(insertQuery)) {
							insertStm.setInt(1, item.getSupplierID());
							insertStm.setString(2, item.getName());
							insertStm.setString(3, item.getType().toString());
							insertStm.setString(4, item.getDescription());
							insertStm.setInt(5, size);
							insertStm.setInt(6, doneness);
							insertStm.setInt(7, restrictions);
							insertStm.setFloat(8, item.getPrice());

							
							int res = insertStm.executeUpdate();
							if(res>0) {
								response.setMessage("The item was added successuflly!");
								response.setResponse(ServerResponse.ADD_SUCCESS);
							}
							
							else {
								response.setMessage("failed to add item because error in db");
								response.setResponse(ServerResponse.ADD_FAIL);
							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return response;
		}
		
		
		/**
		 * Fetches a list of items for a specific supplier from the database.
		 *
		 * @param dbConn the database connection used to execute the SQL query
		 * @param supplierID the ID of the supplier whose items are to be fetched
		 * @return a {@link ServerResponseDataContainer} containing a map of item names to categories and a response message
		 */
		public ServerResponseDataContainer FetchItemsListInfo(Connection dbConn, int supplierID) {
			String query = "SELECT Name, Category FROM items WHERE SupplierID = ?";
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			Map<String,String> map = new HashMap<>();

			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setInt(1, supplierID);

				try (ResultSet rs = stmt.executeQuery()) {
					while(rs.next()) {
						map.put(rs.getString("Name"), rs.getString("Category"));
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			response.setMessage(map);
			response.setResponse(null);
			return response;
		}
		
		/**
		 * Fetches a detailed list of items for a specific supplier from the database.
		 *
		 * @param dbConn the database connection used to execute the SQL query
		 * @param supplierID the ID of the supplier whose items are to be fetched
		 * @return a {@link ServerResponseDataContainer} containing a map of item names to {@link Item} instances and a response message
		 */
		public ServerResponseDataContainer FetchFullItemsListInfo(Connection dbConn, int supplierID) {
			String query = "SELECT * FROM items WHERE SupplierID = ?;";
			ServerResponseDataContainer response = new ServerResponseDataContainer();
			Map<String,Item> itemsMap = new HashMap<>();

			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
				stmt.setInt(1, supplierID);

				try (ResultSet rs = stmt.executeQuery()) {
					while(rs.next()) {
						//create item instance and put it in the map
						int itemID = rs.getInt("ID");
						int SupplierID = rs.getInt("SupplierID");
						String itemName = rs.getString("Name");
						String categoryName = rs.getString("Category");
						Category category;
						switch(categoryName) {
						case "Main Course":
							category = Category.MAINCOURSE;
							break;
						case "First Course":
							category = Category.FIRSTCOURSE;
							break;
						case "Salad":
							category = Category.SALAD;
							break;
						case "Dessert":
							category = Category.DESSERT;
							break;
						case "Beverage":
							category = Category.BEVERAGE;
							break;
						default:
							category = null;
					}
						String description = rs.getString("Description");
						boolean customSize = rs.getInt("CustomSize") == 1 ? true : false;
						boolean customDoneness = rs.getInt("CustomDoneness") == 1 ? true : false; 
						boolean customRestrictions = rs.getInt("CustomRestrictions") == 1 ? true : false; 
						int price  = rs.getInt("Price");
						
						Item item = new Item(itemID, SupplierID, itemName, category, description, customSize, customDoneness, customRestrictions, price);
						itemsMap.put(itemName, item);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			response.setMessage(itemsMap);
			response.setResponse(null);
			return response;
		}
		
		/**
		 * Removes an item from the database based on its name and supplier ID.
		 *
		 * @param dbConn the database connection used to execute the SQL query
		 * @param itemData a map containing the item name and supplier ID of the item to be deleted
		 * @return a {@link ServerResponseDataContainer} containing the result of the operation and a message
		 */
		public ServerResponseDataContainer RemoveItemInfo(Connection dbConn, Map<String,Integer> itemData) {
			String query = "DELETE FROM items WHERE Name = ? AND SupplierID = ?;";
			ServerResponseDataContainer response = new ServerResponseDataContainer();

			// get the data from the map  (we know that this map has exactly one entry.)
		    String itemName = itemData.keySet().iterator().next();
	        int supplierID = itemData.get(itemName);
	        
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {			
				stmt.setString(1, itemName);
				stmt.setInt(2, supplierID);

				 int isRowAffected = stmt.executeUpdate(); //returns how many rows were deleted.
				 
				 if(isRowAffected == 1) {
					response.setMessage("The item was deleted successfully");
					response.setResponse(ServerResponse.DELETE_ITEM_SUCCESS);
				 }
				 else {
					 response.setMessage("failed to delete item because error in db");
					 response.setResponse(ServerResponse.DELETE_ITEM_FAIL);
				 }
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return response;
		}
		
		/**
		 * Updates the details of an existing item in the database.
		 *
		 * @param dbConn the database connection used to execute the SQL query
		 * @param item the {@link Item} instance containing the updated information
		 * @return a {@link ServerResponseDataContainer} containing the result of the operation and a message
		 */
		public ServerResponseDataContainer UpdateItemInfo(Connection dbConn, Item item) {
			String query = "UPDATE items SET Description = ?, CustomSize = ?, CustomDoneness = ?, CustomRestrictions = ?, Price = ? WHERE ID = ?;";
			ServerResponseDataContainer response = new ServerResponseDataContainer();
	        
			int CustomSize = item.getCustomSize() ? 1:0;
			int CustomDoneness = item.getCustomDonenessDegree() ? 1:0;
			int CustomRestrictions = item.getCustomRestrictions() ? 1:0;
			
			try (PreparedStatement stmt = dbConn.prepareStatement(query)) {			
				stmt.setString(1, item.getDescription());
				stmt.setInt(2, CustomSize);
				stmt.setInt(3, CustomDoneness);
				stmt.setInt(4, CustomRestrictions);
				stmt.setFloat(5, item.getPrice());
				stmt.setInt(6, item.getItemID());

				 int isRowAffected = stmt.executeUpdate(); //returns how many rows were updated.
				 
				 if(isRowAffected == 1) {
					response.setMessage("The item was updated successfully");
					response.setResponse(ServerResponse.UPDATE_ITEM_SUCCESS);
				 }
				 else {
					 response.setMessage("failed to update item because error in db");
					 response.setResponse(ServerResponse.UPDATE_ITEM_FAIL);
				 }
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return response;
		}
}