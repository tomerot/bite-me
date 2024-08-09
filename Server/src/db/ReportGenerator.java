package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import Server.Server;
import containers.ServerResponseDataContainer;
import entities.SupplierIncome;
import enums.ServerResponse;

public class ReportGenerator implements Runnable {

    private volatile boolean running = true;
    @Override
    public void run() {
        while (running) {
            try {
                LocalDate today = LocalDate.now();
                if (today.getDayOfMonth() == 9) {
                    //generateOrdersReport();
                    //generatePerformanceReport();
                    generateIncomeReport();
                }

                // Sleep until the next day
                Thread.sleep(ChronoUnit.DAYS.getDuration().toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Report generation thread interrupted");
            } catch (Exception e) {
                e.printStackTrace();
                // Handle other exceptions
            }
        }
    }

    private void generateOrdersReport() {
        System.out.println("Generating Orders Report...");

        String[] branches = {"North", "Center", "South"};
        LocalDate today = LocalDate.now();
        LocalDate startOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = today.minusDays(1);  // till yesterday

        for (String branch : branches) {
            System.out.println("Processing report for region: " + branch);
            ServerResponseDataContainer response = Server.fetchDataForReport(startOfLastMonth, endOfLastMonth, branch);
            if (response != null && response.getResponse() == ServerResponse.DATA_FOUND) {
            	System.out.println(response.getMessage().toString());
                if (response.getMessage() instanceof HashMap) {
                    HashMap<String, Integer> data = (HashMap<String, Integer>) response.getMessage();
                    Server.insertDataForReport(data, branch, today.getYear(), today.getMonthValue() - 1);
                } else {
                    System.out.println("Data type mismatch: Expected HashMap, received " + response.getMessage().getClass().getSimpleName());
                }
            } else {
                System.out.println("No data available or error for region: " + branch);
            }
        }
    }



    private void generatePerformanceReport() throws SQLException {
        System.out.println("Generating Inventory Report...");
        String[] branches = {"North", "Center", "South"};
        LocalDate today = LocalDate.now();
        LocalDate startOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = today.minusDays(1);  

        for (String branch : branches) {
            System.out.println("Processing report for region: " + branch);
            ServerResponseDataContainer response = Server.fetchDataForPerformanceReport(startOfLastMonth, endOfLastMonth, branch);
            if (response != null && response.getResponse() == ServerResponse.DATA_FOUND) {
            	System.out.println(response.getMessage().toString());
                if (response.getMessage() instanceof HashMap) {
                    HashMap<String, Integer> data = (HashMap<String, Integer>) response.getMessage();
                    Server.insertDataForPerformanceReport(data, branch, today.getYear(), today.getMonthValue() - 1);
                } else {
                    System.out.println("Data type mismatch: Expected HashMap, received " + response.getMessage().getClass().getSimpleName());
                }
            } else {
                System.out.println("No data available or error for region: " + branch);
            }
        }
    }

    private void generateIncomeReport() throws SQLException {
        System.out.println("Generating Income Report...");
        String[] branches = {"North", "Center", "South"};
        LocalDate today = LocalDate.now();
        LocalDate startOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());  // Correctly set the end of the last month

        for (String branch : branches) {
            System.out.println("Processing report for region: " + branch);
            ServerResponseDataContainer response = Server.fetchDataForIncomeReport(startOfLastMonth, endOfLastMonth, branch);
            if (response != null && response.getResponse() == ServerResponse.DATA_FOUND) {
                if (response.getMessage() instanceof List<?>) {
                    List<?> rawData = (List<?>) response.getMessage();
                    if (!rawData.isEmpty() && rawData.get(0) instanceof SupplierIncome) {
                        List<SupplierIncome> data = (List<SupplierIncome>) rawData;
                        Server.insertDataForIncomeReport(data, branch, today.getYear(), today.getMonthValue() - 1);
                    } else {
                        System.out.println("Data type mismatch: Expected List<SupplierIncome>, received List<" + rawData.get(0).getClass().getSimpleName() + ">");
                    }
                } else {
                    System.out.println("Data type mismatch: Expected List<SupplierIncome>, received " + response.getMessage().getClass().getSimpleName());
                }
            } else if (response != null && response.getResponse() == ServerResponse.NO_DATA_FOUND) {
                System.out.println("No data available for region: " + branch);
            } else {
                System.out.println("Error processing data for region: " + branch);
                if (response != null) {
                    System.out.println("Error message: " + response.getMessage());
                }
            }
        }
    }


    public void stop() {
        running = false;
    }
}
