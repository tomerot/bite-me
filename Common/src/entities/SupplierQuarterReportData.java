package entities;

import java.io.Serializable;

public class SupplierQuarterReportData implements Serializable {
	private static final long serialVersionUID = 1L;
    private String supplierName;
    private int supplierID;
    private int totalOrders;
    private float totalIncome;

    public SupplierQuarterReportData(int supplierID, String supplierName, float totalIncome, int totalOrders) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.totalIncome = totalIncome;
        this.totalOrders = totalOrders;
    }

    // Getter for supplierName
    public String getSupplierName() {
        return supplierName;
    }

    // Setter for supplierName
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    // Getter for supplierID
    public int getSupplierID() {
        return supplierID;
    }

    // Setter for supplierID
    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    // Getter for totalOrders
    public int getTotalOrders() {
        return totalOrders;
    }

    // Setter for totalOrders
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    // Getter for totalIncome
    public float getTotalIncome() {
        return totalIncome;
    }

    // Setter for totalIncome
    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }
}
