package entities;

import java.io.Serializable;

public class SupplierIncome implements Serializable {
	private static final long serialVersionUID = 1L;
	private int supplierID;
	private String supplierName;
	private int incomes;
	public SupplierIncome(int supplierID, String supplierName, int incomes) {
		this.incomes=incomes;
		this.supplierID=supplierID;
		this.supplierName=supplierName;
	}
	public int getSupplierID() {
		return this.supplierID;
	}
	public int getIncome() {
		return this.incomes;
	}
	public String getSupplierName() {
		return this.supplierName;
	}
}
