package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import containers.ServerResponseDataContainer;
import entities.SupplierIncome;
import enums.ServerResponse;

public class ServerUtilsQueries {

    /**
     * Fetches the order report data for a given branch and date range.
     *
     * @param dbConn            The database connection to use for the query.
     * @param startOfLastMonth  The start date of the range to fetch data for.
     * @param endOfLastMonth    The end date of the range to fetch data for.
     * @param branch            The branch for which to fetch the data.
     * @return A ServerResponseDataContainer containing the results of the query,
     *         either a map of category quantities or an error message.
     */
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

    /**
     * Inserts the order report data into the database for a specified branch and month.
     *
     * @param dbConn The database connection to use for the insert operation.
     * @param data   A HashMap containing categories and their corresponding order quantities.
     * @param branch The branch for which the report is being inserted.
     * @param year   The year for the report entry.
     * @param month  The month for the report entry.
     */
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

    /**
     * Fetches performance report data for a given branch and date range.
     *
     * @param dbConn           The database connection to use for the query.
     * @param startOfLastMonth The start date of the range to fetch data for.
     * @param endOfLastMonth   The end date of the range to fetch data for.
     * @param branch           The branch for which to fetch the data.
     * @return A ServerResponseDataContainer containing the results of the query,
     *         either a map of performance metrics or an error message.
     */
    public ServerResponseDataContainer fetchPerformanceReportData(Connection dbConn, LocalDate startOfLastMonth, LocalDate endOfLastMonth, String branch) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        Map<String, Integer> results = new HashMap<>();
        String query = "SELECT \r\n"
                + "    Branch,\r\n"
                + "    SUM(CASE WHEN Status = 'Late' THEN 1 ELSE 0 END) AS LateOrders,\r\n"
                + "    COUNT(CASE WHEN Status = 'On-time' THEN 1 ELSE NULL END) AS OnTimeOrders\r\n"
                + "FROM \r\n"
                + "    biteme.orders\r\n"
                + "WHERE \r\n"
                + "    RequestDate >= ? AND RequestDate < ? AND Branch = ?\r\n"
                + "GROUP BY \r\n"
                + "    Branch;";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            // Convert LocalDate to java.sql.Date and set the date parameters in the query
            stmt.setDate(1, java.sql.Date.valueOf(startOfLastMonth));
            stmt.setDate(2, java.sql.Date.valueOf(endOfLastMonth));
            stmt.setString(3, branch);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    results.put("OnTimeOrders", rs.getInt("OnTimeOrders"));
                    results.put("LateOrders", rs.getInt("LateOrders"));
                    response.setMessage(results);
                    response.setResponse(ServerResponse.DATA_FOUND);
                } else {
                    response.setResponse(ServerResponse.NO_DATA_FOUND);
                    response.setMessage("No data found for the specified criteria.");
                }
            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
                e.printStackTrace();
                response.setResponse(ServerResponse.ERROR);
                response.setMessage("SQL Error: " + e.getMessage());
            }
        } catch (SQLException e1) {
            System.out.println("Database connection error: " + e1.getMessage());
            e1.printStackTrace();
            response.setResponse(ServerResponse.ERROR);
            response.setMessage("Database connection error: " + e1.getMessage());
        }
        return response;
    }

    /**
     * Inserts the performance report data into the database for a specified branch and month.
     *
     * @param dbConn The database connection to use for the insert operation.
     * @param data   A HashMap containing performance metrics.
     * @param branch The branch for which the report is being inserted.
     * @param year   The year for the report entry.
     * @param month  The month for the report entry.
     * @throws SQLException If an error occurs during the SQL execution.
     */
    public void insertPerformanceReport(Connection dbConn, HashMap<String, Integer> data, String branch, int year, int month) throws SQLException {
        String query = "INSERT INTO performance_reports (Year, Month, Branch, OnTime, Late) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            stmt.setString(3, branch);
            stmt.setInt(4, data.getOrDefault("OnTimeOrders", 0));
            stmt.setInt(5, data.getOrDefault("LateOrders", 0));

            int affectedRows = stmt.executeUpdate();
            System.out.println("Inserted " + affectedRows + " rows into performance_reports.");
        }
    }

    /**
     * Fetches the income report data for a given branch and date range.
     *
     * @param dbConn            The database connection to use for the query.
     * @param startOfLastMonth  The start date of the range to fetch data for.
     * @param endOfLastMonth    The end date of the range to fetch data for.
     * @param branch            The branch for which to fetch the data.
     * @return A ServerResponseDataContainer containing the results of the query,
     *         either a list of SupplierIncome objects or an error message.
     */
    public ServerResponseDataContainer fetchIncomeReportData(Connection dbConn, LocalDate startOfLastMonth, LocalDate endOfLastMonth, String branch) {
        ServerResponseDataContainer response = new ServerResponseDataContainer();
        List<SupplierIncome> results = new ArrayList<>();

        String query = "SELECT "
                + "s.ID AS SupplierID, "
                + "s.Name AS supplierName, "
                + "ROUND(COALESCE(SUM(o.TotalPrice), 0), 2) AS incomes, "
                + "COUNT(o.OrderID) AS totalOrders "
                + "FROM "
                + "suppliers s "
                + "LEFT JOIN orders o ON s.ID = o.SupplierID "
                + "AND o.RequestDate BETWEEN ? AND ? "
                + "AND s.Branch = o.Branch "
                + "WHERE "
                + "s.Branch = ? "
                + "GROUP BY "
                + "s.ID, s.Name "
                + "ORDER BY incomes DESC";

        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(startOfLastMonth));
            stmt.setDate(2, java.sql.Date.valueOf(endOfLastMonth));
            stmt.setString(3, branch);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int supplierID = rs.getInt("SupplierID");
                    String supplierName = rs.getString("supplierName");
                    int incomes = rs.getInt("incomes");
                    int totalOrders = rs.getInt("totalOrders");
                    results.add(new SupplierIncome(supplierID, supplierName, incomes, totalOrders));
                }

                if (!results.isEmpty()) {
                    response.setMessage(results); // Set the List as the message instead of a String
                    response.setResponse(ServerResponse.DATA_FOUND);
                } else {
                    response.setResponse(ServerResponse.NO_DATA_FOUND);
                    response.setMessage("No data found for the specified criteria.");
                }
            }
        } catch (SQLException e) {
            response.setResponse(ServerResponse.ERROR);
            response.setMessage("SQL Error: " + e.getMessage());
        }
        return response;
    }

    /**
     * Inserts the income report data into the database for a specified branch and month.
     *
     * @param dbConn  The database connection to use for the insert operation.
     * @param incomes A list of SupplierIncome objects representing the data to insert.
     * @param branch  The branch for which the report is being inserted.
     * @param year    The year for the report entry.
     * @param month   The month for the report entry.
     */
    public void insertIncomeReport(Connection dbConn, List<SupplierIncome> incomes, String branch, int year, int month) {
        String insertQuery = "INSERT INTO incomes_reports (Year, Month, Branch, SupplierID, SupplierName, Incomes) VALUES (?, ?, ?, ?, ?, ?)";
        int count = 0;  // To keep track of inserted rows

        try (PreparedStatement stmt = dbConn.prepareStatement(insertQuery)) {
            for (SupplierIncome income : incomes) {
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                stmt.setString(3, branch);
                stmt.setInt(4, income.getSupplierID());
                stmt.setString(5, income.getSupplierName());
                stmt.setInt(6, income.getIncome());
                stmt.addBatch();  
                count++;
            }
            int[] updateCounts = stmt.executeBatch();  
            System.out.println(count + " rows inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inserts customer and user data into the database from CSV files as an import simulation.
     * 
     * This method performs two separate bulk insert operations:
     * - Loads user data from 'Users.csv' into the 'users' table.
     * - Loads customer data from 'Customers.csv' into the 'customers' table.
     * 
     * Each CSV file should be properly formatted and located in the specified directory.
     * 
     * @param dbConn The database connection to use for the insert operations.
     * 
     * @throws SQLException If a database access error occurs or the SQL statements fail.
     */
    public void insertCustomersList(Connection dbConn, String path) throws SQLException {
        String query = "LOAD DATA INFILE '"+ path+  "Users.csv' INTO TABLE users FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES (Username,Password,IsLoggedIn,Type,Registered);";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            int affectedRows = stmt.executeUpdate();
            System.out.println("Inserted " + affectedRows + " users into users.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        query = "LOAD DATA INFILE '"+ path + "Customers.csv' INTO TABLE customers FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES (Username,ID,Type,CompanyID,FirstName,LastName,Email,Phone,HomeBranch,Credit,CVV,ValidMonth,ValidYear,WalletBalance);";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            int affectedRows = stmt.executeUpdate();
            System.out.println("Inserted " + affectedRows + " customers into customers.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
