package gui.controllers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.Customer;
import entities.ItemInOrder;
import entities.Order;
import entities.OrderType;
import entities.Supplier;
import entities.SupplyMethod;
import enums.CustomerType;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

public class CheckoutScreenController {
	
	private ChooseRestaurantScreenController restaurantController;

	private Map<ItemInOrder, Integer> cart = new HashMap<>();
	private List<String> relevantDeliveryCities;
	private Customer customer;
	private Supplier supplier;
	private Order order;
	private float walletAmount;
	private int peopleInOrder;
	private float totalDeliveryPrice;
	
	private float itemsPrice;
	private float customerWalletBalance;
	private float totalPriceNoWallet;
	private String orderType;

	@FXML
	private AnchorPane dashboard;
	@FXML
	private RadioButton basicRadioBtn;

	@FXML
	private RadioButton SharedRadioBtn;

	@FXML
	private RadioButton RobotRadioBtn;

	@FXML
	private RadioButton takeAwayRadioBtn;

	@FXML
	private TextField supplyTimeField;

	@FXML
	private TextField reciverNameField;

	@FXML
	private TextField reciverPhoneField;


	@FXML
	private TextField addressField;

	@FXML
	private Button finishBtn;

	@FXML
	private Button backBtn;

	@FXML
	private TextField amountField;
	
    @FXML
    private DatePicker datePicker;
    

    @FXML
    private RadioButton WalletRadioBtn;

    @FXML
    private Label walletBalanceLbl;

    @FXML
    private Label itemsPriceLbl;

    @FXML
    private Label deliveryPriceLbl;

    @FXML
    private Label totalPriceLbl;
    
    @FXML
    private TextField WalletField;
    
    @FXML
    private Label orderTypeLbl;
    
    @FXML
    private Label preOrderStatusLbl;

    @FXML
    private ChoiceBox<String> CityChoise;


	private ToggleGroup deliveryTypeGroup;

	private HBox wholeScreen;
	RestaurantMenuScreenController prevController;

	/**
	 * Initializes a new `CheckoutScreenController` with the given screen and previous controller.
	 *
	 * @param wholeScreen the main screen container for the checkout view
	 * @param prevController the previous screen controller, expected to be of type `RestaurantMenuScreenController`
	 */
	public CheckoutScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (RestaurantMenuScreenController) prevController;
		this.restaurantController = this.prevController.getRestaurantController();
		this.cart = this.prevController.getCart();
		this.customer = this.prevController.getCustomer();
		this.supplier = this.prevController.getSupplier();
		this.itemsPrice = this.prevController.getTotalPrice();
		
		// get all relevant cities for delivery for correct supplier
		ClientMainController.requestAllRelevantCitys(supplier);
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		relevantDeliveryCities = (List<String>) response.getMessage();
	}

	/**
	 * Initializes the checkout screen by setting up the date label, configuring radio buttons,
	 * and adding listeners for live validation. It sets the initial states for the various
	 * UI elements based on the customer's type and the supplier's details.
	 */
	@FXML
	private void initialize() {
		 setupInitialUI();
		 setupListeners();
		 configureAvailableRadioButtons();
		 addValidationListeners();
	}
	
	
	/**
	 * Sets up the initial UI state by configuring default values and states for UI components.
	 * This includes setting default text, hiding labels, updating wallet and price labels,
	 * populating DROPDOWN with cities, initializing the date picker, and configuring radio buttons.
	 */
	private void setupInitialUI() {
	    // Set default values and states for UI components
	    supplyTimeField.setText("00:00:00");
	    orderType = "Regular";
	    orderTypeLbl.setVisible(false);
	    orderTypeLbl.setText("Order Type: " + orderType);
	    
	    // Display wallet balance and item price
	    updateWalletAndPriceLabels();
	    
	    // Populate city dropdown
	    populateCityDropdown();
	    
	    // Initialize date picker
	    initializeDatePicker();
	    
	    // Configure radio buttons
	    setupRadioButtons();
	    
	    // Initialize wallet field
	    setupWalletField();
	}
	
	/**
	 * Updates the wallet balance and item price labels in the UI.
	 * Retrieves the customer's wallet balance and formats it for display,
	 * and updates the item price label with the current item price.
	 */
	private void updateWalletAndPriceLabels() {
	    float walletBalance = customer.getWalletBalance();
	    customerWalletBalance = walletBalance;
	    walletBalanceLbl.setText(String.format("%.2f₪", walletBalance));
	    itemsPriceLbl.setText(String.format("%.2f₪", itemsPrice));
	}

	/**
	 * Populates the city DROPDOWN with relevant delivery cities.
	 */
	private void populateCityDropdown() {
	    ObservableList<String> citiesObservableList = FXCollections.observableArrayList(relevantDeliveryCities);
	    CityChoise.setItems(citiesObservableList);
	    CityChoise.setValue(supplier.getCity());
	    CityChoise.setDisable(true);
	}

	/**
	 * Sets up the radio buttons and their associated ToggleGroup.
	 */
	private void setupRadioButtons() {
	    deliveryTypeGroup = new ToggleGroup();
	    basicRadioBtn.setToggleGroup(deliveryTypeGroup);
	    SharedRadioBtn.setToggleGroup(deliveryTypeGroup);
	    RobotRadioBtn.setToggleGroup(deliveryTypeGroup);
	    takeAwayRadioBtn.setToggleGroup(deliveryTypeGroup);
	    
	    RobotRadioBtn.setDisable(true);
	    takeAwayRadioBtn.setSelected(true);
	}

	/**
	 * Sets up the wallet field and its listeners.
	 */
	private void setupWalletField() {
	    WalletField.setText("0");
	    WalletRadioBtn.setSelected(false);
	    WalletField.setDisable(true);
	}

	/**
	 * Sets up the listeners for various UI components.
	 */
	private void setupListeners() {
	    setupWalletRadioButtonListener();
	    setupSharedRadioButtonListener();
	    setupBasicRadioButtonListener();
	    setupTakeAwayRadioButtonListener();
	    setupAmountFieldListener();
	    setupWalletFieldListener();
	    setupDatePickerListener();
	    setupSupplyTimeFieldListener();
	  //setupRobotRadioButtonListener(); //Set for future implementation
	}

	/**
	 * Sets up listener for the wallet radio button.
	 */
	private void setupWalletRadioButtonListener() {
	    WalletRadioBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue) {
	            WalletField.setText("0");
	            WalletField.setDisable(false);
	            validateWalletField();
	            updatePriceLabels();
	        } else {
	            WalletField.setText("0");
	            WalletField.setDisable(true);
	            WalletField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	            validateWalletField();
	        }
	    });
	}

	/**
	 * Sets up listener for the shared radio button.
	 */
	private void setupSharedRadioButtonListener() {
	    SharedRadioBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue) {
	            amountField.setDisable(false);
	            amountField.setText("1");
	            CityChoise.setDisable(false);
	            validateAmountField();
	            validateWalletField();
	            updatePriceLabels();
	            orderTypeLbl.setVisible(true);
	            addressField.clear();
	            CityChoise.requestFocus();
	        }
	    });
	}

	/**
	 * Sets up listener for the basic radio button.
	 */
	private void setupBasicRadioButtonListener() {
	    basicRadioBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue) {
	            amountField.setText("1");
	            amountField.setDisable(true);
	            amountField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	            CityChoise.setDisable(false);
	            updatePriceLabels();
	            validateWalletField();
	            orderTypeLbl.setVisible(true);
	            addressField.clear();
	            CityChoise.requestFocus();
	        }
	    });
	}
	
	/**
	 * Sets up listener for the Robot radio button.
	 */
	/*private void setupRobotRadioButtonListener() { //Set for future implementation
	    basicRadioBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue) {
	            amountField.setText("1");
	            amountField.setDisable(true);
	            amountField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	            CityChoise.setDisable(false);
	            updatePriceLabels();
	            validateWalletField();
	            orderTypeLbl.setVisible(true);
	            addressField.clear();
	            CityChoise.requestFocus();
	        }
	    });
	}*/
	
	/**
	 * Sets up listener for the takeaway radio button.
	 */
	private void setupTakeAwayRadioButtonListener() {
	    takeAwayRadioBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue) {
	            amountField.setText("1");
	            amountField.setDisable(true);
	            amountField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	            addressField.setText(supplier.getAddress());
	            addressField.setDisable(true);
	            CityChoise.setValue(supplier.getCity());
	            CityChoise.setDisable(true);
	            updatePriceLabels();
	            validateWalletField();
	            orderTypeLbl.setVisible(false);
	        } else {
	            addressField.setDisable(false);
	            updatePriceLabels();
	            validateWalletField();
	        }
	    });
	}

	/**
	 * Sets up listener for the amount field.
	 */
	private void setupAmountFieldListener() {
	    amountField.textProperty().addListener((obs, oldValue, newValue) -> {
	        updatePriceLabels();
	        validateWalletField();
	        checkSupplyTime(datePicker.getValue(), supplyTimeField.getText());
	        orderTypeLbl.setText("Order Type: " + orderType);
	    });
	}

	/**
	 * Sets up listener for the wallet field.
	 */
	private void setupWalletFieldListener() {
	    WalletField.textProperty().addListener((obs, oldValue, newValue) -> {
	        updatePriceLabels();
	    });
	}

	/**
	 * Sets up listener for the date picker.
	 */
	private void setupDatePickerListener() {
	    datePicker.valueProperty().addListener((observable, oldDate, newDate) -> {
	        String timeText = supplyTimeField.getText();
	        if (isValidTimeFormat(timeText)) {
	            validateSupplyTime();
	            checkSupplyTime(datePicker.getValue(), supplyTimeField.getText());
	            orderTypeLbl.setText("Order Type: " + orderType);
	            updatePriceLabels();
	        } else {
	            supplyTimeField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	            orderTypeLbl.setText("Invalid time format, please correct.");
	        }
	    });
	}

	/**
	 * Sets up listener for the supply time field.
	 */
	private void setupSupplyTimeFieldListener() {
	    supplyTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
	        if (isValidTimeFormat(newValue)) {
	            validateSupplyTime();
	            checkSupplyTime(datePicker.getValue(), supplyTimeField.getText());
	            orderTypeLbl.setText("Order Type: " + orderType);
	            updatePriceLabels();
	        } else {
	            supplyTimeField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	            orderTypeLbl.setText("Invalid Supply time");
	            updatePriceLabels();
	        }
	    });
	}
	
	/**
	 * Handles the action to proceed to the summary page. This method performs all necessary
	 * validations for the fields and ensures that all data is correctly entered before moving on.
	 * It checks each field and shows relevant error messages if any validation fails.
	 * calls the server to update order and items_in_orders tables with relevant info
	 * in the end open the summary screen
	 * @throws IOException 
	 */
	@FXML
	private void ContinueToSummary() throws IOException {
		// validate all fields (make sure MANYAK don't try to bypass the PreOrder)
		LocalDate selectedDate = datePicker.getValue();
		String supplyTime = supplyTimeField.getText();
		checkSupplyTime(selectedDate, supplyTime);
		updatePriceLabels();
		validateReciverName();
		validateReciverPhone();
		validateSupplyTime();
		validateAddressField();
		validateAmountField();
		validateWalletField();
		
		// Check if all fields are valid
		if (checkAllFields()) {
			return; // If any validation fails, return
		}
		
		//we got here if all field are ok
		//LocalDate selectedDate = datePicker.getValue();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateString = selectedDate.format(formatter);
		SupplyMethod deliveryType = getSelectedDeliveryType();
		peopleInOrder = Integer.parseInt(amountField.getText());
	    String recipientName = reciverNameField.getText();
	    String recipientPhone = reciverPhoneField.getText();
	    String city = CityChoise.getValue();
	    String address = addressField.getText();
	    boolean isWalletUsed = WalletRadioBtn.isSelected();
	    this.walletAmount = isWalletUsed ? Float.parseFloat(WalletField.getText()) : 0;
	    float totalPrice = this.totalPriceNoWallet; // Assuming this variable holds the total price
	    OrderType type = getOrderType();
	    
	    this.order = new Order(null, 0, recipientName, recipientPhone, deliveryType, supplier, customer, supplier.getHomeBranch(), dateString, supplyTime, type, totalPrice, null, null, null, city, address);
		    
	    //display summary
	    OrderSummaryScreen();
	}

	/**
	 * Handles the "Back" button action. Loads the previous screen (Restaurant Menu Screen) 
	 * and clears the current dashboard content.
	 *
	 * @throws IOException if an I/O error occurs while loading the previous screen.
	 */
	@FXML
	private void pressBack() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
		String path = "/gui/view/RestaurantMenuScreen.fxml";
		AnchorPane prevDash = screenLoader.loadPreviousDashboard(path, Screen.RESTAURANT_MENU_SCREEN, prevController);
		dashboard.getChildren().clear();
		dashboard.getChildren().add(prevDash);
	}
	
	/**
	 * Checks the validity of all fields on the checkout screen and displays appropriate alerts
	 * if any field is invalid. It aggregates the validity of all fields and provides feedback
	 * to the user on which fields need correction.
	 *
	 * @return true if any field is invalid, false otherwise.
	 */
	private boolean checkAllFields() {
		 // Check if the any field has an invalid style (contains "red")
		boolean nameField = reciverNameField.getStyle().contains("red");
		boolean phoneField = reciverPhoneField.getStyle().contains("red");
		boolean timeField = supplyTimeField.getStyle().contains("red");
		boolean addressFieldB = addressField.getStyle().contains("red");
		boolean amountFieldB = amountField.getStyle().contains("red");
		boolean wmountFieldB = WalletField.getStyle().contains("red");

		// If more than one field is invalid, show an alert with general instructions
		int x = (nameField ? 1 : 0) + (phoneField ? 1 : 0) + (timeField ? 1 : 0) + (addressFieldB ? 1 : 0) + (amountFieldB ? 1 : 0);;
		if(x>1) {
			showAlert("Empty Fields", "Please make sure all fields contains data! \n "
					+ "\"correct the highlighted fields\"");
			return true;
		}
		// Show specific alerts for each invalid field
		if (nameField) {
			showAlert("Invalid Name Field", "Make Sure Reciver name Field is correct!");
			return true;
		}
		if (phoneField) {
			showAlert("Invalid Phone Field", "Make Sure Reciver phone Field is correct!");
			return true;
		}
		if (timeField) {
			showAlert("Invalid Time Field", "Make Sure Time Field is correct! (format is HH:ss:mm)");
			return true;
		}
		if (addressFieldB) {
			showAlert("Invalid Address Field", "Make Sure Address Field is filled and correct!");
			return true;
		}
		if (amountFieldB) {
			showAlert("Invalid Amount Field", "Make Sure amount Field(shared delivery) is correct!");
			return true;
		}
		if (wmountFieldB) {
			showAlert("Invalid Wallet Field", "Make Sure Wallet Field(Use Amount) is correct!"
					+ "\n remember it cant be higher then you totalprice");
			return true;
		}
		return false;
	}

	/**
	 * Shows an alert with the specified title and message to provide feedback to the user
	 * regarding errors or important information.
	 *
	 * @param title   the title of the alert.
	 * @param message the content of the alert message.
	 */
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Configures the availability and state of radio buttons based on the customer's type.
	 * Enables or disables radio buttons and updates the state of associated fields as 
	 * appropriate.
	 */
	private void configureAvailableRadioButtons() {
		CustomerType customerType = customer.getCustomerType();

		if (CustomerType.PRIVATE.equals(customerType)) {
			// Enable only Private and TakeAway radio buttons
			basicRadioBtn.setDisable(false);
			SharedRadioBtn.setDisable(true);
			RobotRadioBtn.setDisable(true);
			takeAwayRadioBtn.setDisable(false);
		} else if (CustomerType.BUSINESS.equals(customerType)) {
			// Enable Private, TakeAway, and Business radio buttons
			basicRadioBtn.setDisable(false);
			SharedRadioBtn.setDisable(false);
			RobotRadioBtn.setDisable(true);
			takeAwayRadioBtn.setDisable(false);
		}
		// Check if TakeAway or Private is selected and update amountField
		if (takeAwayRadioBtn.isSelected() || basicRadioBtn.isSelected()) {
			amountField.setText("1");
			amountField.setDisable(true);
		} else {
			amountField.setDisable(false);
		}
		// Check if TakeAway is selected and update city/address fields
		if (takeAwayRadioBtn.isSelected()) {
			addressField.setText(supplier.getAddress());
			addressField.setDisable(true);
		}
	}

	/**
	 * Adds validation listeners to the fields on the checkout screen. These listeners provide
	 * live feedback on the validity of the fields as the user interacts with them.
	 */
	private void addValidationListeners() {
		reciverNameField.textProperty().addListener((observable, oldValue, newValue) -> validateReciverName());
		reciverPhoneField.textProperty().addListener((observable, oldValue, newValue) -> validateReciverPhone());
		supplyTimeField.textProperty().addListener((observable, oldValue, newValue) -> validateSupplyTime());
		addressField.textProperty().addListener((observable, oldValue, newValue) -> validateAddressField());
		amountField.textProperty().addListener((observable, oldValue, newValue) -> validateAmountField());
		WalletField.textProperty().addListener((observable, oldValue, newValue) -> validateWalletField());
	}

	/**
	 * Validates the receiver's name field. Checks if the name contains only letters and sets
	 * the field border color to red if invalid, or green if valid.
	 */
	private void validateReciverName() {
		String name = reciverNameField.getText();
		if (!name.matches("[a-zA-Z]+")) {
			reciverNameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		} else {
			reciverNameField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
	}

	/**
	 * Validates the receiver's phone field. Checks if the phone number starts with '05' and
	 * contains exactly 10 digits. Sets the field border color to red if invalid, or green if valid.
	 */
	private void validateReciverPhone() {
		String phone = reciverPhoneField.getText();
		if (!phone.matches("^05\\d{8}$")) {
			reciverPhoneField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		} else {
			reciverPhoneField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
	}


	/**
	 * Validates the supply time field. Ensures the time is within the acceptable range based
	 * on the selected date. If the date is today, the time must be between now and the end of the day.
	 * Sets the field border color to red if invalid, or green if valid.
	 */
	private void validateSupplyTime() {
		// Supply time validation logic here
		String timeText = supplyTimeField.getText();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		try {
			LocalTime enteredTime = LocalTime.parse(timeText, timeFormatter);
			LocalDate selectedDate = datePicker.getValue();
			LocalTime currentTime = LocalTime.now();
			LocalTime endOfDay = LocalTime.MAX;

	        if (selectedDate.isEqual(LocalDate.now())) {
	            // Date is today, so supply time must be between now and end of day
	            if (!enteredTime.isBefore(currentTime) && !enteredTime.isAfter(endOfDay)) {
	                supplyTimeField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	            } else {
	                supplyTimeField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	            }
	        } else {
	            // Date is not today, so supply time can be any valid time
	            supplyTimeField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	        }
	    } catch (DateTimeParseException e) {
	        supplyTimeField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	    }
		
	}

	/**
	 * Validates the address field. Checks if the address field is not empty and sets the field
	 * border color to red if empty, or green if valid.
	 */
	private void validateAddressField() {
	    String address = addressField.getText().trim(); // Trim leading and trailing spaces
	    String allowedCharsRegex = "^[a-zA-Z0-9\\s.,-/|\\\\]*$";
	    
	    boolean isLengthValid = address.length() >= 1 && address.length() <= 30;
	    boolean isValid = !address.isEmpty() && address.matches(allowedCharsRegex) && isLengthValid ;

	    // Update the style based on validity
	    if (isValid) {
	        addressField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	    } else {
	        addressField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	    }
	}

	/**
	 * Validates the amount field for shared delivery. Ensures that the amount is a positive integer
	 * and does not start with '0'. Sets the field border color to red if invalid, or green if valid.
	 */
	private void validateAmountField() {
		if (SharedRadioBtn.isSelected()) {
			String amount = amountField.getText();
			boolean isLengthValid = amount.length() <= 3;
			if (!amount.matches("\\d+") || amount.startsWith("0") || !isLengthValid) {
				amountField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
			} else {
				amountField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
			}
		}
	}
	
	/**
	 * Validates the wallet field. Ensures that the amount entered is within the customer's wallet
	 * balance and does not exceed the total price. Sets the field border color to red if invalid,
	 * or green if valid.
	 */
	private void validateWalletField() {
	    if (WalletRadioBtn.isSelected()) {
	        String amountText = WalletField.getText();
	        try {
	            // Convert the amount to a float
	            float amountF = Float.parseFloat(amountText);

	            // Check if the amount is valid
	            boolean isValidAmount = amountF >= 0 && amountF <= customerWalletBalance;
	            boolean isFloatFormat = amountText.matches("\\d+\\.?\\d*");

	            if (!isFloatFormat || !isValidAmount || (totalPriceNoWallet - amountF < 0)) {
	                // Invalid format or out of balance, set border to red
	                WalletField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	            } else {
	                // Valid amount and format, set border to green
	                WalletField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	            }
	        } catch (NumberFormatException e) {
	            // If the amount is not a valid number, set the border to red
	            WalletField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	        }
	    }
	}
	
	/**
	 * Updates the price labels for delivery and total price.
	 * 
	 * This method calculates the delivery price based on the selected radio button:
	 * - If {@code basicRadioBtn} is selected, the delivery price is set to 25.
	 * - If {@code SharedRadioBtn} is selected, the delivery price depends on the quantity entered in {@code amountField}:
	 *   - 1 : 25
	 *   - 2 user: 40 (20 each)
	 *   - 3 or more: 15 per user
	 * - If {@code takeAwayRadioBtn} or {@code RobotRadioBtn} is selected, the delivery price is 0.
	 * 
	 * The method also reads the wallet amount from {@code WalletField} and updates the labels {@code deliveryPriceLbl}
	 * and {@code totalPriceLbl} with the calculated delivery and total prices.
	 */
	private void updatePriceLabels() {
		preOrderStatusLbl.setText("");
	    float deliveryPrice = 0;
	    float totalPrice;
	    int amount = 1;
	    float walletF = 0;

	    try {
	    	walletF = Float.parseFloat(WalletField.getText());
	    }
	    catch(NumberFormatException e) {}
	    
	    // Determine the delivery price based on the selected radio button
	    if (basicRadioBtn.isSelected()) {
	        deliveryPrice = 25; 
	    } 
	    else if (SharedRadioBtn.isSelected()) {
	        try {
	            amount = Integer.parseInt(amountField.getText());
	        } 
	        catch (NumberFormatException e) {}
	        
	        if (amount == 1) {
	        	deliveryPrice = 25;
	        }  
	        else if (amount == 2) {
	        	deliveryPrice = 40;
	        }
	        else {
	        	deliveryPrice = amount * 15;
	        }       
	    } 
	    
	    else if (takeAwayRadioBtn.isSelected()) {
	        deliveryPrice = 0;
	    } 
	    else if(RobotRadioBtn.isSelected()) {
	    	deliveryPrice = 0;
	    }
	    
	    totalPriceNoWallet = itemsPrice + deliveryPrice;

	    // Apply the 10% discount if it's a pre-order
	    if (orderType.equals("PreOrder") && !takeAwayRadioBtn.isSelected()) {
	        totalPriceNoWallet *= 0.9; // Apply 10% discount
	        double temp = (itemsPrice + deliveryPrice) * 0.9 ;
	        preOrderStatusLbl.setText(String.format("PreOrder Detected, applied 10%% discount \n (%.2f₪ + %.2f₪) * 0.9 = %.2f₪ ", itemsPrice, deliveryPrice, temp));
	    }
	    
	    
	    totalPrice = totalPriceNoWallet - walletF;
	    this.totalDeliveryPrice = deliveryPrice;
	    deliveryPriceLbl.setText(String.format("%.2f₪", deliveryPrice));
	    //totalPrice = itemsPrice + deliveryPrice - walletF;
	    totalPriceLbl.setText(String.format("%.2f₪ - %.2f₪ = %.2f₪ ",totalPriceNoWallet, walletF, totalPrice));
	}
	
	/**
	 * Initializes the date picker to show today's date and restricts the selectable range to one week.
	 * 
	 * This method sets the date picker to default to today's date and restricts selectable dates to between today and one
	 * week from today. It also formats the displayed date as "yyyy-MM-dd".
	 * 
	 * The date picker disables dates before today and after one week from today. It includes a custom string converter to
	 * format the date display.
	 */
    private void initializeDatePicker() {
        // Get today's date
        LocalDate today = LocalDate.now();
        // Calculate the end of the range (one week from today)
        LocalDate oneWeekFromToday = today.plusWeeks(1);

        // Set the minimum and maximum dates for the DatePicker
        datePicker.setValue(today);
        datePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today) || date.isAfter(oneWeekFromToday));
            }
        });

        // Optional: Format the displayed date if needed
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if (date == null) {
                    return "";
                }
                return dateFormatter.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return LocalDate.parse(string, dateFormatter);
            }
        });
    }
    
    /**
     * Returns the selected delivery type based on the currently selected radio button.
     * 
     * @return The selected {@link SupplyMethod} which can be BASIC, SHARED, ROBOT, or TAKEAWAY.
     */
    private SupplyMethod getSelectedDeliveryType() {
        if (basicRadioBtn.isSelected()) {
        	return SupplyMethod.BASIC;
        } else if (SharedRadioBtn.isSelected()) {
            return SupplyMethod.SHARED;
        } else if (RobotRadioBtn.isSelected()) {
            return SupplyMethod.ROBOT;
        }
        else{return SupplyMethod.TAKEAWAY;}
    }
    
    /**
     * Determines the order type based on the current order type string.
     * 
     * @return The {@link OrderType} which can be PRE_ORDER or REGULAR.
     */
    private OrderType getOrderType() {
    	if (takeAwayRadioBtn.isSelected()) {return OrderType.REGULAR;}
    	else if (orderType.equals("PreOrder")) {return OrderType.PRE_ORDER;}
    	else {return OrderType.REGULAR;}
    }
    
    
    /**
     * Checks if the supply time is more than 2 hours from the current time and updates the order type accordingly.
     * 
     * @param selectedDate The date selected for the supply.
     * @param supplyTime The time of the supply in the format HH:mm:ss.
     */
    private void checkSupplyTime(LocalDate selectedDate, String supplyTime) {
        // Define the time format with seconds
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        try {
            // Parse the supply time to LocalTime with seconds
            LocalTime parsedSupplyTime = LocalTime.parse(supplyTime, timeFormatter);
            
            // Combine selectedDate and parsedSupplyTime into LocalDateTime
            LocalDateTime supplyDateTime = LocalDateTime.of(selectedDate, parsedSupplyTime);
            
            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            
            // Calculate the duration between now and the supply time
            Duration duration = Duration.between(now, supplyDateTime);
            
            // Check if the duration is more than 2 hours
            if (duration.toMinutes() >= 120) {
                orderType = "PreOrder";
            } else {
                orderType = "Regular";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Validates if the given time string is in the correct format of HH:mm:ss.
     * 
     * @param timeText The time string to be validated.
     * @return {@code true} if the time string is in the correct format, {@code false} otherwise.
     */
    private boolean isValidTimeFormat(String timeText) {
    	String timePattern = "([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)";
    	
        if (timeText.matches(timePattern)) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            try {
                LocalTime.parse(timeText, timeFormatter);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }
    
    
    /**
     * Loads and displays the order summary screen.
     * using `ScreenLoader` to load the `OrderSummaryScreen.fxml` file,
     * clears the current dashboard, and adds the new order summary screen to the dashboard.
     *
     * @throws IOException if an error occurs while loading the FXML file
     */
	@FXML
	private void OrderSummaryScreen() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/OrderSummaryScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, path, Screen.ORDER_SUMMARY_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
    
	
	//Few Getters
	public float getItemsPrice() {
		return itemsPrice;
	}
	public float getTotalDeliveryPrice() {
		return totalDeliveryPrice;
	}
	public int getPeopleInOrder() {
		return peopleInOrder;
	}
	public float getWalletAmount() {
		return walletAmount;
	}
	public Customer getCustomer() {
		return customer;
	}
	
	public Supplier getSupplier() {
		return supplier;
	}
	public Order getOrder() {
		return order;
	}
	public Map<ItemInOrder, Integer> getCart() {
	    return cart;
	}
	
	public ChooseRestaurantScreenController getRestaurantController(){
		return restaurantController;
	}
    
}
