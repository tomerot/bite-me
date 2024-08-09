package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import Server.Server;
import containers.ServerResponseDataContainer;
import enums.ServerResponse;

public class ReportGenerator implements Runnable {

    private volatile boolean running = true;
    @Override
    public void run() {
        while (running) {
            try {
                LocalDate today = LocalDate.now();
                if (today.getDayOfMonth() == 7) {
                    generateOrdersReport();
                    generatePerformanceReport();
                    generateCustomerReport();
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
        LocalDate endOfLastMonth = today.minusDays(1);  // till yesterday

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

    private void generateCustomerReport() {
        System.out.println("Generating Customer Report...");
        // Implement your database logic here
    }

    public void stop() {
        running = false;
    }
}
