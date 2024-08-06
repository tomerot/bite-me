package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import Server.Server;

import containers.ServerResponseDataContainer;

public class ReportGenerator implements Runnable {

    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            try {
                LocalDate today = LocalDate.now();
                if (today.getDayOfMonth() == 7) {
                    generateOrdersReport();
                    generateInventoryReport();
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
        ServerResponseDataContainer response = Server.fetchDataForReport();
        System.out.println(response.getMessage() + " " + response.getResponse());
        		
        
    }

    private void generateInventoryReport() {
        System.out.println("Generating Inventory Report...");
        // Implement your database logic here
    }

    private void generateCustomerReport() {
        System.out.println("Generating Customer Report...");
        // Implement your database logic here
    }

    public void stop() {
        running = false;
    }
}
