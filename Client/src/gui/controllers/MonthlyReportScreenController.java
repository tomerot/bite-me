package gui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.SupplierIncome;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Controller for managing the monthly report screen.
 * Handles navigation between different pages of the monthly report and returning to the previous screen.
 */
public class MonthlyReportScreenController {

	@FXML
	private Label errorLabel;
	@FXML
	private ComboBox<String> yearSelect;
	@FXML
	private ComboBox<String> monthSelect;
	@FXML
	private ComboBox<String> reportType;
	@FXML
	private ComboBox<String> branchSelect;
	@FXML
	private Button backBtn;
	@FXML
	private PieChart orderChart;
	@FXML
	private PieChart performanceChart;
	@FXML
	private TableView<SupplierIncome> incomeChart;
    @FXML
    private TableColumn<SupplierIncome, Integer> supplierID;
	@FXML
	private TableColumn<SupplierIncome, String> supplierName;
	@FXML
	private TableColumn<SupplierIncome, Integer> totalMonthIncome;
	@FXML
	private AnchorPane dashboard;
	
	private CeoHomeScreenController prevCeoController;
	private BranchManagerController prevManagerController;
	private HBox wholeScreen;
	private String branch;
	private String report;
	private String year;
	private String month;
	private ServerResponseDataContainer entityResponse=null;
    /**
     * Constructs a new MonthlyReportScreenController.
     * 
     * @param wholeScreen the HBox container for the current screen
     * @param prevController the previous controller, either CeoHomeScreenController or BranchManagerController
     */
	public MonthlyReportScreenController(HBox wholeScreen , Object prevController) {
		if(prevController instanceof CeoHomeScreenController) {
			this.prevCeoController = (CeoHomeScreenController) prevController;
			this.prevManagerController=null;
			this.branch=null;
		}
		if(prevController instanceof BranchManagerController) {
			this.prevCeoController = null;
			this.prevManagerController=(BranchManagerController) prevController;
			this.branch=prevManagerController.getBranchManager().getbranchType().toShortString();
		}
		this.wholeScreen = wholeScreen;
		this.reportType=null;
		this.year=null;
		this.month=null;
	}

    public String getBranch() { return branch; }

    public String getReportType() { return report; }
	
    /**
     * Navigates to the next page of the monthly report.
     * 
     * @param event the action event triggered by clicking the next page button
     * @throws Exception if an error occurs during the page transition
     */
//	public void displayNextPage(ActionEvent event) throws Exception {
//		ScreenLoader screenLoader = new ScreenLoader();
//		AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, "/gui/view/MonthlyReportScreen2.fxml", Screen.MONTHLY_REPORT_SCREEN_TWO, this);
//		dashboard.getChildren().clear();
//		dashboard.getChildren().add(nextDash);
//	}
    public String getYear() { return year; }
    
    public String getMonth() { return month; }

	
    /**
     * Returns to the previous screen based on the previous controller.
     * 
     * @param event the action event triggered by clicking the back button
     * @throws Exception if an error occurs during the screen transition
     */
	public void goBack(ActionEvent event) throws Exception {
		ScreenLoader screenLoader = new ScreenLoader();
		HBox prevWholeScreen=null;
		String path="";
		if(prevCeoController!=null && prevManagerController==null) {
			path = "/gui/view/CeoScreen.fxml";
			prevWholeScreen = screenLoader.loadPreviousScreen(path, Screen.CEO_SCREEN, prevCeoController);
			prevCeoController.UpdateLabel(prevCeoController.getCeo());
		}
		if(prevCeoController==null && prevManagerController!=null) {
			path = "/gui/view/BranchManagerScreen.fxml";
			prevWholeScreen = screenLoader.loadPreviousScreen(path, Screen.MANAGER_SCREEN, prevManagerController);
			prevManagerController.UpdateLabel(prevManagerController.getBranchManager());
		}

		wholeScreen.getChildren().clear();
		wholeScreen.getChildren().add(prevWholeScreen);
	}
	/**
	 * Sets up the screen by initializing various selection controls and configuring their visibility.
	 * The method performs the following actions:
	 * - Populates the `reportType` combo box with options for "Orders Reports", "Income Reports", and "Performance Reports".
	 * - Populates the `branchSelect` combo box with branch options ("North", "Center", "South").
	 * - Populates the `monthSelect` combo box with month values, formatted as "01" to "12".
	 * - Populates the `yearSelect` combo box with years from 1995 to the current year.
	 * - Hides the `branchSelect` control if `prevCeoController` is `null`.
	 * - Sets the visibility of `incomeChart`, `performanceChart`, and `orderChart` to `false`.
	 */
	public void setScreen() {
		reportType.getItems().addAll("Orders Reports", "Income Reports", "Performance Reports");
		branchSelect.getItems().addAll("North", "Center", "South");
		for(int i=1; i<10;i++) {
			monthSelect.getItems().add("0"+i);
		}
		monthSelect.getItems().addAll("10","11","12");
		for(int j=1995; j<=java.time.Year.now().getValue() ;j++) {
			yearSelect.getItems().add(Integer.toString(j));
		}
		if(this.prevCeoController==null) {
			branchSelect.setVisible(false);
		}
		incomeChart.setVisible(false);
		performanceChart.setVisible(false);
		orderChart.setVisible(false);
	}
	public void setBranchSelection() {
		this.branch = branchSelect.getValue();
	}
	
	public void setReportTypeSelection() {
		this.report = reportType.getValue();
	}
	
	public void setMonthSelection() {
		this.month = monthSelect.getValue();
	}
	
	public void setYearSelection() {
		this.year = yearSelect.getValue();
	}
	
    public void updateLabel(String msg) {
	    Platform.runLater(() -> {
	    	errorLabel.setText(msg);
	    });
    }
    /**
     * Handles the display of the selected report based on user input.
     * 
     * This method first checks if all required fields (`report`, `month`, `year`, and `branch`) are filled. If any field is missing, it updates the label to prompt the user to fill all required fields.
     * If all fields are filled, it clears the visibility and data of the charts and retrieves the report data from the server. The method then processes the report data based on the type of report received.
     * 
     * @param event the action event that triggered the report display (e.g., a button click)
     * @throws IOException if an I/O error occurs during report data retrieval from the server
     * 
     * The method performs the following actions:
     * - Checks if `report`, `month`, `year`, or `branch` is null and updates the label if any field is missing.
     * - Sets visibility of `orderChart`, `performanceChart`, and `incomeChart` to false and clears their data.
     * - Prepares a list of report information and requests the report data from the server via `ClientMainController.requestReportData`.
     * - Processes the received data based on the type of report:
     *   - If the report is an order report, it processes order report data using `processOrderReport`.
     *   - If the report is a performance report, it processes performance report data using `processPerformanceReport`.
     *   - If the report is an income report, it processes income report data using `processIncomeReport`.
     */
	public void displayReport(ActionEvent event) throws IOException {
		if(this.report==null||this.month==null||this.year==null||this.branch==null) {
			updateLabel("Please fill all the required fields");
		}
		else {
			updateLabel("");
			orderChart.setVisible(false);
			performanceChart.setVisible(false);
			incomeChart.setVisible(false);
			orderChart.getData().clear();
			performanceChart.getData().clear();
			incomeChart.getItems().clear();
			String path=null;
			List<String> reportInfo=new ArrayList<String>();
			reportInfo.add(this.report);reportInfo.add(this.branch);reportInfo.add(this.month);reportInfo.add(this.year);
			ClientMainController.requestReportData(reportInfo);
			entityResponse = ClientConsole.responseFromServer;
			switch (entityResponse.getResponse()) {
			case ORDER_REPORT:{
				List<String> orderReportData=(ArrayList<String>)entityResponse.getMessage();
				processOrderReport(orderReportData);
				break;
			}
			case PERFORMANCE_REPORT:{
				List<String> performanceReportData=(ArrayList<String>)entityResponse.getMessage();
				processPerformanceReport(performanceReportData);
				break;
			}
			case INCOME_REPORT:{
				List<SupplierIncome> SupplierIncomeList=(List<SupplierIncome>)entityResponse.getMessage();
				processIncomeReport(SupplierIncomeList);
				break;
			}}

		}
	}
	/**
	 * Processes and displays the order report data in a pie chart.
	 * 
	 * This method updates the label to indicate the order report details and then populates a pie chart with the order data divided by food category. It runs on the JavaFX Application Thread using `Platform.runLater` to ensure that UI updates are made on the correct thread.
	 * 
	 * @param orderReportData a list of strings where each element represents the total orders for a specific food category
	 * 
	 * The method performs the following actions:
	 * - Checks if the `orderReportData` list is not empty.
	 * - Updates the label with the report details for the specified branch, month, and year.
	 * - Clears any existing data from the `orderChart`.
	 * - Creates and populates a `PieChart` with data from `orderReportData`, categorizing by food type.
	 * - Sets the name of each slice in the pie chart to include the value of the slice.
	 * - Sets the visibility of the `orderChart` to true.
	 * 
	 * If `orderReportData` is empty, it updates the label to indicate that the order report does not exist.
	 */
	public void processOrderReport(List<String> orderReportData) {
		if (!orderReportData.isEmpty()) {
			updateLabel("Order report for the "+this.branch+" branch, "+ this.month+"/"+this.year+", divided by food category");
			Platform.runLater(() -> {
				orderChart.getData().clear();
				ObservableList<PieChart.Data> pieChartData =FXCollections.observableArrayList(
						new PieChart.Data("Salad", Double.valueOf(orderReportData.get(0))),
						new PieChart.Data("First Course", Double.valueOf(orderReportData.get(1))),
						new PieChart.Data("Main Course", Double.valueOf(orderReportData.get(2))),
						new PieChart.Data("Dessert", Double.valueOf(orderReportData.get(3))),
						new PieChart.Data("Beverage", Double.valueOf(orderReportData.get(4))));
				orderChart.getData().addAll(pieChartData);
	            for (PieChart.Data data : pieChartData) {
	                data.nameProperty().set(String.format("%s: %.1f", data.getName(), data.getPieValue()));
	            }
				
				orderChart.setVisible(true);

			});
		}
		else {
			updateLabel(this.branch+" branch "+ this.month+"/"+this.year+" order report does not exist");
		}
	}
	/**
	 * Processes and displays the income report data in a table.
	 * 
	 * This method updates the label to indicate the income report details and then populates a table with the supplier income data. It sets the cell value factories for each column in the table and updates the table with the provided data.
	 * 
	 * @param SupplierIncomeList a list of {@link SupplierIncome} objects, where each object contains information about a supplier's income
	 * 
	 * The method performs the following actions:
	 * - Checks if the `SupplierIncomeList` is not empty.
	 * - Updates the label with the income report details for the specified branch, month, and year.
	 * - Configures the cell value factories for the table columns to display supplier ID, supplier name, and total income.
	 * - Creates an `ObservableList` from the `SupplierIncomeList` and sets it as the items for the table.
	 * - Sets the visibility of the `incomeChart` to true.
	 * 
	 * If `SupplierIncomeList` is empty, it updates the label to indicate that the income report does not exist.
	 */
	public void processIncomeReport(List<SupplierIncome> SupplierIncomeList) {
		if (!SupplierIncomeList.isEmpty()) {
			updateLabel(this.branch+" branch "+ this.month+"/"+this.year+" income report");
			supplierID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSupplierID()).asObject());
			supplierName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplierName()));
			totalMonthIncome.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIncome()).asObject());
			ObservableList<SupplierIncome> SupplierIncomeData = FXCollections.observableArrayList();
			for (SupplierIncome supplierIncome : SupplierIncomeList) {
				SupplierIncomeData.add(supplierIncome);
			}
			incomeChart.setItems(SupplierIncomeData);
			incomeChart.setVisible(true);
		}
		else {
			updateLabel(this.branch+" branch "+ this.month+"/"+this.year+" income report does not exist");
		}
	}
	/**
	 * Processes and displays the performance report data in a pie chart.
	 * 
	 * This method updates the label to indicate the performance report details and then populates a pie chart with the performance data, showing the distribution of on-time and late orders. It updates the chart with the provided data and sets its visibility to true.
	 * 
	 * @param performanceReportData a list of {@link String} values representing the performance report data; the first value is the count of on-time orders and the second value is the count of late orders
	 * 
	 * The method performs the following actions:
	 * - Checks if the `performanceReportData` list is not empty.
	 * - Updates the label with the performance report details for the specified branch, month, and year.
	 * - Clears any existing data from the `performanceChart`.
	 * - Creates an `ObservableList` of `PieChart.Data` from the `performanceReportData`, with each data point representing either on-time or late orders.
	 * - Adds the pie chart data to the `performanceChart`.
	 * - Sets formatted names and values for each pie chart data point.
	 * - Sets the visibility of the `performanceChart` to true.
	 * 
	 * If `performanceReportData` is empty, it updates the label to indicate that the performance report does not exist.
	 */
	public void processPerformanceReport(List<String> performanceReportData) {
		if (!performanceReportData.isEmpty()) {
			updateLabel("Performance report for the "+this.branch+" branch, "+ this.month+"/"+this.year+", divided by Late/On-Time orders");
			Platform.runLater(() -> {
				performanceChart.getData().clear();
				ObservableList<PieChart.Data> pieChartData =FXCollections.observableArrayList(
						new PieChart.Data("On Time", Double.valueOf(performanceReportData.get(0))),
						new PieChart.Data("Late", Double.valueOf(performanceReportData.get(1))));
				performanceChart.getData().addAll(pieChartData);
				for (PieChart.Data data : pieChartData) {
	                data.nameProperty().set(String.format("%s: %.1f", data.getName(), data.getPieValue()));
				}
				performanceChart.setVisible(true);
			});
		}
		else {
			updateLabel(this.branch+" branch "+ this.month+"/"+this.year+" performance report does not exist");
		}
	}
}
