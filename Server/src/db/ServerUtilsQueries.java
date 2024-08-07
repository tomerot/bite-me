package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;

import containers.ServerResponseDataContainer;
import enums.ServerResponse;

public class ServerUtilsQueries {

    public ServerResponseDataContainer fetchOrdersReportData(Connection dbConn, LocalDate startOfLastMonth, LocalDate endOfLastMonth, String branch) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        HashMap<String, Integer> categoryQuantities = new HashMap<>();
        String query = "SELECT iio.Category, iio.Quantity \r\n"
                + "FROM orders o \r\n"
                + "LEFT JOIN items_in_orders iio ON iio.OrderID = o.OrderID \r\n"
                + "WHERE RequestDate >= ? AND RequestDate < ? AND o.Branch = ? \r\n";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            // Convert LocalDate to java.sql.Date and set the date parameters in the query
            stmt.setDate(1, java.sql.Date.valueOf(startOfLastMonth));
            stmt.setDate(2, java.sql.Date.valueOf(endOfLastMonth));
            stmt.setString(3, branch);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String category = rs.getString("Category");
                    int quantity = rs.getInt("Quantity");

                    // Skip null categories and quantities
                    if (category != null && quantity != 0) {
                        categoryQuantities.merge(category, quantity, Integer::sum);
                    }
                }

                // Check if the map has any entries
                if (!categoryQuantities.isEmpty()) {
                    response.setMessage(categoryQuantities);
                    response.setResponse(ServerResponse.DATA_FOUND);
                } else {
                    response.setResponse(ServerResponse.NO_DATA_FOUND);
                    response.setMessage("No valid data found for the given criteria.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                response.setResponse(ServerResponse.ERROR);
                response.setMessage("SQL Error: " + e.getMessage());
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            response.setResponse(ServerResponse.ERROR);
            response.setMessage("Database connection error: " + e1.getMessage());
        }
        return response;
    }
    
    
    public void insertOrdersReportData(Connection dbConn, HashMap<String, Integer> data, String branch, int year, int month) {
        String sql = "INSERT INTO orders_reports (Year, Month, Branch, Category, Orders) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(sql)) {
            dbConn.setAutoCommit(false); // Ensure control over transaction commit
            int totalInserted = 0; // Counter to keep track of inserted rows

            for (Entry<String, Integer> entry : data.entrySet()) {
                pstmt.setInt(1, year);
                pstmt.setInt(2, month);
                pstmt.setString(3, branch);
                pstmt.setString(4, entry.getKey());
                pstmt.setInt(5, entry.getValue());
                totalInserted += pstmt.executeUpdate(); // Execute the update and increment the counter
            }

            dbConn.commit(); // Commit the transaction
            System.out.println(totalInserted + " rows inserted successfully for branch: " + branch);

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            try {
                dbConn.rollback(); // Rollback in case of an error
                System.out.println("Transaction rolled back due to an error.");
            } catch (SQLException ex) {
                System.out.println("Error during transaction rollback: " + ex.getMessage());
                ex.printStackTrace();
            }
        } finally {
            try {
                dbConn.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException e) {
                System.out.println("Error setting auto-commit back to true: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
