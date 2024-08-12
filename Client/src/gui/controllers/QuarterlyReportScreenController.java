package gui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.SupplierQuarterReportData;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Controller for managing the monthly report screen.
 * Handles navigation between different pages of the monthly report and returning to the previous screen.
 */
public class QuarterlyReportScreenController {
	@FXML
	private Label headlineLabel;
	@FXML
	private Button backBtn;
	@FXML
	private Button clearSelection;
	@FXML
	private Button ordersGraph;
	@FXML
	private Button incomeGraph;
	@FXML
	private Button mini1OrderGraph;
	@FXML
	private Button mini1IncomeGraph;
	@FXML
	private Button mini2OrderGraph;
	@FXML
	private Button mini2IncomeGraph;
	@FXML
	private ComboBox<String> yearSelect;
	@FXML
	private ComboBox<String> quarterSelect;
	@FXML
	private ComboBox<String> branchSelect;
	@FXML
	private ComboBox<String> yearSelect2;
	@FXML
	private ComboBox<String> quarterSelect2;
	@FXML
	private ComboBox<String> branchSelect2;
	@FXML
	private ScrollPane mainHistogramIncomeScroll;
	@FXML
	private BarChart<String, Number> mainHistogramIncome;
	@FXML
	private ScrollPane mainHistogramOrdersScroll;
	@FXML
	private BarChart<String, Number> mainHistogramOrders;
	@FXML
	private ScrollPane miniHistogram1IncomeScroll;
	@FXML
	private BarChart<String, Number> miniHistogram1Income;
	@FXML
	private ScrollPane miniHistogram1OrdersScroll;
	@FXML
	private BarChart<String, Number> miniHistogram1Orders;
	@FXML
	private ScrollPane miniHistogram2IncomeScroll;
	@FXML
	private BarChart<String, Number> miniHistogram2Income;
	@FXML
	private ScrollPane miniHistogram2OrdersScroll;
	@FXML
	private BarChart<String, Number> miniHistogram2Orders;
	@FXML
	private AnchorPane dashboard;
	
	private CeoHomeScreenController prevCeoController;
	private HBox wholeScreen;
	private String branch;
	private String year;
	private String quarter;
	private String branch2;
	private String year2;
	private String quarter2;

	/**
	 * Constructs a new QuarterlyReportScreenController with the specified HBox and previous controller.
	 * This constructor initializes the fields to manage the screen layout and previous controller state.
	 *
	 * @param wholeScreen    the HBox representing the entire screen layout for the report
	 * @param prevController the previous controller, which is cast to CeoHomeScreenController
	 */
	public QuarterlyReportScreenController(HBox wholeScreen , Object prevController) {
		this.prevCeoController = (CeoHomeScreenController) prevController;
		this.wholeScreen = wholeScreen;
		this.branch=null;
		this.year=null;
		this.quarter=null;
		this.branch2=null;
		this.year2=null;
		this.quarter2=null;
	}

    public String getBranch() { return branch; }
	
    public String getYear() { return year; }
    
    public String getQuarter() { return quarter; }
    
    public String getBranch2() { return branch2; }
	
    public String getYear2() { return year2; }
    
    public String getQuarter2() { return quarter2; }
	
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
		path = "/gui/view/CeoScreen.fxml";
		prevWholeScreen = screenLoader.loadPreviousScreen(path, Screen.CEO_SCREEN, prevCeoController);
		prevCeoController.UpdateLabel(prevCeoController.getCeo());
		wholeScreen.getChildren().clear();
		wholeScreen.getChildren().add(prevWholeScreen);
	}
	/**
	 * Sets up the screen by populating the ComboBoxes with the appropriate branch, quarter, and year options.
	 * The branch and quarter ComboBoxes are populated with predefined values, while the year ComboBoxes
	 * are populated with values ranging from 1995 to the current year. 
	 * This method also calls `setAllInvisible()` to initialize the visibility of elements.
	 */
	public void setScreen() {
		branchSelect.getItems().addAll("North", "Center", "South");
		branchSelect2.getItems().addAll("North", "Center", "South");
		quarterSelect.getItems().addAll("1","2","3","4");
		quarterSelect2.getItems().addAll("1","2","3","4");
		for(int j=1995; j<=java.time.Year.now().getValue();j++) {
			yearSelect.getItems().add(Integer.toString(j));
		}
		for(int j=1995; j<=java.time.Year.now().getValue();j++) {
			yearSelect2.getItems().add(Integer.toString(j));
		}
		setAllInvisible();
	}
	public void setBranchSelection() {
		this.branch = branchSelect.getValue();
	}
	public void setBranch2Selection() {
		this.branch2 = branchSelect2.getValue();
	}
	
	public void setQuarterSelection() {
		this.quarter = quarterSelect.getValue();
	}
	
	public void setQuarter2Selection() {
		this.quarter2 = quarterSelect2.getValue();
	}
	
	public void setYearSelection() {
		this.year = yearSelect.getValue();
	}
	public void setYear2Selection() {
		this.year2 = yearSelect2.getValue();
	}
	
    public void updateLabel(String msg) {
	    Platform.runLater(() -> {
	    	headlineLabel.setText(msg);
	    });
    }
    /**
     * Displays the quarterly report based on the user's selection.
     * This method retrieves data for one or both selected reports, processes the data,
     * and updates the UI with the appropriate graphs and labels.
     * If no valid report selection is made, an error message is displayed to the user.
     *
     * @param event the ActionEvent that triggered the display of the report
     * @throws IOException if an I/O error occurs while retrieving the report data
     */
	public void displayReport(ActionEvent event) throws IOException {
		String labelText=null;
		ServerResponseDataContainer entityResponse1 = null;
		ServerResponseDataContainer entityResponse2 = null;
		List<SupplierQuarterReportData> quarterReportData1=null;
		List<SupplierQuarterReportData> quarterReportData2=null;
		List<String> reportInfo=new ArrayList<String>();
		boolean showGraph1 = !(this.quarter==null||this.year==null||this.branch==null);
		boolean showGraph2 = !(this.quarter2==null||this.year2==null||this.branch2==null);
		setAllInvisible();
		clearAllData();
		if(!showGraph1&&!showGraph2) {
			updateLabel("Please fill all required fields of at least 1 report");
		}
		else {
			if(showGraph1&&showGraph2) {//show both graphs
				updateLabel("");
				reportInfo.add(this.branch);reportInfo.add(this.quarter);reportInfo.add(this.year);
				ClientMainController.requestQuarterReportData(reportInfo);
				entityResponse1 = ClientConsole.responseFromServer;
				reportInfo.clear();
				reportInfo.add(this.branch2);reportInfo.add(this.quarter2);reportInfo.add(this.year2);
				ClientMainController.requestQuarterReportData(reportInfo);
				entityResponse2 = ClientConsole.responseFromServer;
				quarterReportData1=(List<SupplierQuarterReportData>)entityResponse1.getMessage();
				quarterReportData2 = (List<SupplierQuarterReportData>)entityResponse2.getMessage();
				if(quarterReportData1.isEmpty()||quarterReportData2.isEmpty()) {
					updateLabel("One of the reports is of the current/upcoming quarters, please choose again");
					incomeGraph.setVisible(false);
				    ordersGraph.setVisible(false);
					clearAllSelection();
				}
				else {
					updateLabel(this.branch + " branch, Q" + this.quarter + " " + this.year+ " report:" + "\t\t\t\t\t\t\t" + this.branch2 + " branch, Q" + this.quarter2 + " " + this.year2 + " report:");
					processSingleQuarterReport(quarterReportData1, miniHistogram1Income, miniHistogram1Orders, "Mini1");
					processSingleQuarterReport(quarterReportData2, miniHistogram2Income, miniHistogram2Orders, "Mini2");
				}
			}
			else {//show only 1 graph
				if(showGraph1) {
					updateLabel("");
					reportInfo.add(this.branch);reportInfo.add(this.quarter);reportInfo.add(this.year);
					ClientMainController.requestQuarterReportData(reportInfo);
					entityResponse1 = ClientConsole.responseFromServer;
					quarterReportData1=(List<SupplierQuarterReportData>)entityResponse1.getMessage();
					labelText=this.branch+" branch, Q"+this.quarter+ " "+ this.year;
					if(quarterReportData1.isEmpty()) {
						updateLabel(labelText + " is not completed yet. please choose again");
						incomeGraph.setVisible(false);
					    ordersGraph.setVisible(false);
					}
					else {
						updateLabel(labelText + " report:");
						processSingleQuarterReport(quarterReportData1, mainHistogramIncome, mainHistogramOrders, "Main");
					}
				}
				else {
					updateLabel("");
					reportInfo.add(this.branch2);reportInfo.add(this.quarter2);reportInfo.add(this.year2);
					ClientMainController.requestQuarterReportData(reportInfo);
					entityResponse2 = ClientConsole.responseFromServer;
					quarterReportData2 = (List<SupplierQuarterReportData>)entityResponse2.getMessage();
					labelText= this.branch2+" branch, Q"+ this.quarter2 + " "+ this.year2;
					if(quarterReportData2.isEmpty()) {
						updateLabel(labelText + " is not completed yet. please choose again");
						incomeGraph.setVisible(false);
					    ordersGraph.setVisible(false);
					}
					else {
						updateLabel(labelText + " report:");
						processSingleQuarterReport(quarterReportData2, mainHistogramIncome, mainHistogramOrders, "Main");
					}
				}
			}
		}

	}
	/**
	 * Processes a single quarterly report by populating the provided bar charts with data.
	 * The method iterates through the list of supplier report data, creates data points
	 * for total income and total orders, and then adds these points to the corresponding bar charts.
	 * Additionally, it adjusts the chart width based on the number of suppliers and displays the charts
	 * based on the specified type (main or mini charts).
	 *
	 * @param quarterReportData the list of {@link SupplierQuarterReportData} containing the data for the suppliers in the selected quarter
	 * @param incomeChart       the {@link BarChart} to display the total income data
	 * @param orderChart        the {@link BarChart} to display the total orders data
	 * @param type              a {@link String} indicating the type of chart to be displayed: 
	 *                          "Main" for the main chart, "Mini1" for the first mini chart, 
	 *                          or "Mini2" for the second mini chart
	 */
	public void processSingleQuarterReport(List<SupplierQuarterReportData> quarterReportData, BarChart<String, Number> incomeChart, BarChart<String, Number> orderChart, String type) {
	    XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
	    XYChart.Series<String, Number> ordersSeries = new XYChart.Series<>();

	    incomeSeries.setName("Total Income");
	    ordersSeries.setName("Total Orders");
	    adjustChartWidth(quarterReportData.size(),type);
	    // Iterate over each restaurant data
	    for (SupplierQuarterReportData data : quarterReportData) {
	    	String barData = String.format("Supplier: %s%nSupplier ID: %d%nTotal Income: %.2f%nTotal Orders: %d",
	            data.getSupplierName(), data.getSupplierID(), data.getTotalIncome(), data.getTotalOrders());
	        XYChart.Data<String, Number> incomeData = new XYChart.Data<>(data.getSupplierName(), data.getTotalIncome());
	        incomeData.setNode(createBarNode(barData));
	        incomeSeries.getData().add(incomeData);

	        // Add orders data point
	        XYChart.Data<String, Number> ordersData = new XYChart.Data<>(data.getSupplierName(), data.getTotalOrders());
	        ordersData.setNode(createBarNode(barData));
	        ordersSeries.getData().add(ordersData);
	    }

	    incomeChart.getData().add(incomeSeries);
	    orderChart.getData().add(ordersSeries);
	    applySeriesColor(incomeSeries, "#9bbfb6");
	    applySeriesColor(ordersSeries, "#bf9437");
	    switch(type) {
	    case "Main":
	    	displayIncomeMain();
	    	incomeGraph.setVisible(true);
		    ordersGraph.setVisible(true);
	    	break;
	    case "Mini1":
	    	displayIncomeMini1();
	    	mini1IncomeGraph.setVisible(true);
	    	mini1OrderGraph.setVisible(true);
	    	break;
	    
    	case "Mini2":
    		displayIncomeMini2();
	    	mini2IncomeGraph.setVisible(true);
	    	mini2OrderGraph.setVisible(true);
	    	break;
	    }
	}
	
	public void displayIncomeMain() {
		mainHistogramOrdersScroll.setVisible(false);
		mainHistogramIncomeScroll.setVisible(true);
	}
	public void displayIncomeMini1() {
		miniHistogram1OrdersScroll.setVisible(false);
		miniHistogram1IncomeScroll.setVisible(true);
	}
	public void displayIncomeMini2() {
		miniHistogram2OrdersScroll.setVisible(false);
		miniHistogram2IncomeScroll.setVisible(true);
	}
	public void displayOrdersMain() {
		mainHistogramIncomeScroll.setVisible(false);
		mainHistogramOrdersScroll.setVisible(true);
	}
	public void displayOrdersMini1() {
		miniHistogram1IncomeScroll.setVisible(false);
		miniHistogram1OrdersScroll.setVisible(true);
	}
	public void displayOrdersMini2() {
		miniHistogram2IncomeScroll.setVisible(false);
		miniHistogram2OrdersScroll.setVisible(true);
	}
	/**
	 * Creates a JavaFX node with a tooltip to be used as a data point in a bar chart.
	 * The node is a label with a tooltip that displays additional information when hovered over.
	 *
	 * @param tooltipText the text to be displayed in the tooltip
	 * @return a {@link javafx.scene.Node} (specifically a {@link javafx.scene.control.Label}) with the specified tooltip
	 */
	private javafx.scene.Node createBarNode(String tooltipText) {
	    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(tooltipText);
	    javafx.scene.control.Label label = new javafx.scene.control.Label();
	    label.setTooltip(tooltip);
	    return label;
	}
	/**
	 * Applies a specified color to all bars in the given chart series.
	 * This method sets the fill color of each bar in the series using the provided color hex code.
	 *
	 * @param series   the {@link XYChart.Series} to which the color should be applied
	 * @param colorHex the hex code of the color to be applied (e.g., "#FF0000" for red)
	 */
	private void applySeriesColor(XYChart.Series<String, Number> series, String colorHex) {
	    for (XYChart.Data<String, Number> data : series.getData()) {
	        data.getNode().setStyle("-fx-bar-fill: " + colorHex + ";");
	    }
	}
	/**
	 * Adjusts the width of the chart based on the number of suppliers and the type of chart.
	 * The method calculates the required width by considering the number of bars and gaps,
	 * and sets the minimum width and category gap for the specified chart type.
	 *
	 * @param numberOfSuppliers the number of suppliers, which determines the number of bars and gaps in the chart
	 * @param type              the type of chart to be adjusted; can be "Main", "Mini1", or "Mini2"
	 *
	 * The method performs the following adjustments based on the chart type:
	 * - "Main": Sets the minimum width and category gap for the main histogram charts.
	 * - "Mini1": Sets the minimum width and category gap for the first mini histogram charts.
	 * - "Mini2": Sets the minimum width and category gap for the second mini histogram charts.
	 */
	private void adjustChartWidth(int numberOfSuppliers, String type) {
		switch(type) {
		case "Main":{
			double minWidth = 1155.0;
			double CategoryGapTotal= numberOfSuppliers*50;//each gap is 50 pixal
		    double CategoryBarTotal= 150*numberOfSuppliers;//each bar is minimum of 150 pixal
		    double chartWidth = Math.max(minWidth, CategoryGapTotal + CategoryBarTotal);
		    mainHistogramOrders.setCategoryGap(50);
		    mainHistogramIncome.setCategoryGap(50);
		    mainHistogramOrders.setMinWidth(chartWidth);
		    mainHistogramIncome.setMinWidth(chartWidth);
		    break;
		}
		case "Mini1":{
			double minWidth = 573.0;
			double CategoryGapTotal= numberOfSuppliers*35;//each gap is 50 pixal
		    double CategoryBarTotal= 100*numberOfSuppliers;//each bar is minimum of 150 pixal
		    double chartWidth = Math.max(minWidth, CategoryGapTotal + CategoryBarTotal);
		    miniHistogram1Orders.setCategoryGap(35);
		    miniHistogram1Income.setCategoryGap(35);
		    miniHistogram1Income.setMinWidth(chartWidth);
		    miniHistogram1Orders.setMinWidth(chartWidth);
		    break;
		}
		case "Mini2":{
			double minWidth = 573.0;
			double CategoryGapTotal= numberOfSuppliers*35;//each gap is 50 pixal
		    double CategoryBarTotal= 100*numberOfSuppliers;//each bar is minimum of 150 pixal
		    double chartWidth = Math.max(minWidth, CategoryGapTotal + CategoryBarTotal);
		    miniHistogram2Income.setCategoryGap(35);
		    miniHistogram2Orders.setCategoryGap(35);
		    miniHistogram2Orders.setMinWidth(chartWidth);
		    miniHistogram2Income.setMinWidth(chartWidth);
		    break;
		}
		}
	}
	/**
	 * Clears all selected values from the various selection controls and resets their states.
	 * The method performs the following actions:
	 * - Clears the selection in `yearSelect`, `quarterSelect`, and `branchSelect`.
	 * - Clears the selection in `yearSelect2`, `quarterSelect2`, and `branchSelect2`.
	 * - Resets the branch, quarter, and year selections for both sets of controls to their default states.
	 */
	public void clearAllSelection() {
		yearSelect.getSelectionModel().clearSelection();
		quarterSelect.getSelectionModel().clearSelection();
		branchSelect.getSelectionModel().clearSelection();
		yearSelect2.getSelectionModel().clearSelection();
		quarterSelect2.getSelectionModel().clearSelection();
		branchSelect2.getSelectionModel().clearSelection();
		setBranchSelection();
		setBranch2Selection();
		setQuarterSelection();
		setQuarter2Selection();
		setYearSelection();
		setYear2Selection();
	}
	/**
	 * Hides all chart and scroll pane elements by setting their visibility to false.
	 * The method performs the following actions:
	 * - Hides the mini histogram charts for orders and income (`mini1OrderGraph`, `mini2OrderGraph`, `mini1IncomeGraph`, `mini2IncomeGraph`).
	 * - Hides the main histogram charts for orders and income (`ordersGraph`, `incomeGraph`).
	 * - Hides the scroll panes associated with the histograms (`mainHistogramIncomeScroll`, `mainHistogramOrdersScroll`, `miniHistogram1IncomeScroll`, `miniHistogram1OrdersScroll`, `miniHistogram2IncomeScroll`, `miniHistogram2OrdersScroll`).
	 */
	private void setAllInvisible() {
		mini1OrderGraph.setVisible(false);
		mini2OrderGraph.setVisible(false);
		mini1IncomeGraph.setVisible(false);
		mini2IncomeGraph.setVisible(false);
		ordersGraph.setVisible(false);
		incomeGraph.setVisible(false);
		mainHistogramIncomeScroll.setVisible(false);
		mainHistogramOrdersScroll.setVisible(false);
		miniHistogram1IncomeScroll.setVisible(false);
		miniHistogram1OrdersScroll.setVisible(false);
		miniHistogram2IncomeScroll.setVisible(false);
		miniHistogram2OrdersScroll.setVisible(false);
	}
	/**
	 * Clears all data from the histogram charts by removing all data series.
	 * The method performs the following actions:
	 * - Clears the data from the main histogram charts for income and orders (`mainHistogramIncome`, `mainHistogramOrders`).
	 * - Clears the data from the mini histogram charts for income and orders (`miniHistogram1Income`, `miniHistogram1Orders`, `miniHistogram2Income`, `miniHistogram2Orders`).
	 */
	private void clearAllData() {
		mainHistogramIncome.getData().clear();
		mainHistogramOrders.getData().clear();
		miniHistogram1Income.getData().clear();
		miniHistogram1Orders.getData().clear();
		miniHistogram2Income.getData().clear();
		miniHistogram2Orders.getData().clear();
	}
}
