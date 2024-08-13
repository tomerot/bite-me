package gui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import client.ClientConsole;
import client.ClientMainController;
import containers.ServerResponseDataContainer;
import entities.Customer;
import entities.Item;
import entities.ItemInOrder;
import entities.Supplier;
import gui.loader.Screen;
import gui.loader.ScreenLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

/**
 * Controller for managing the menu screen of the chosen supplier.
 * Handles item selection, cart and returning to the previous screen.
 * @author Tomer Rotman
 */
public class RestaurantMenuScreenController {
	
	private ChooseRestaurantScreenController prevController;
	private HBox wholeScreen;
	
	private Customer customer;
	private Supplier supplier;
	
	private List<Item> salads = new ArrayList<>();
	private List<Item> starters = new ArrayList<>();
	private List<Item> mainCourses = new ArrayList<>();
	private List<Item> desserts = new ArrayList<>();
	private List<Item> beverages = new ArrayList<>();
	
	private Map<ItemInOrder, Integer> cart = new HashMap<>(); //Key is ItemInOrder, value is it's quantity
	private Map<ItemInOrder, AnchorPane> cartCards = new HashMap<>(); //Key is ItemInOrder, value is it's card
	
	private float totalPrice;
	
	private boolean firstInitialization = true; //If it's the first time this controller is initialized, this is true
	
	@FXML
	private AnchorPane dashboard;
	
	@FXML
	private GridPane gridPane;
	
	@FXML
	private AnchorPane itemOptionsPane;
	
	@FXML
	private Label pickedItem;
	
	@FXML
	private Label sizeTitle;
	
	@FXML
	private RadioButton small;
	
	@FXML
	private RadioButton medium;
	
	@FXML
	private RadioButton large;
	
	@FXML
	private Label donenessTitle;
	
	@FXML
	private RadioButton MR;
	
	@FXML
	private RadioButton M;
	
	@FXML
	private RadioButton MW;
	
	@FXML
	private RadioButton WD;
	
	@FXML
	private Label restrictionsTitle;
	
	@FXML
	private TextField restrictions;
	
	@FXML
	private Button addToCartBtn;
	
	@FXML
	private Rectangle mustSelectSize;
	
	@FXML
	private Rectangle mustSelectDoneness;
	
	@FXML
	private Label emptyCart;
	
	@FXML
	private Button checkoutBtn;
	
	@FXML
	private Button garbageBtn;
	
	@FXML
	private ScrollPane cartScroll;
	
	@FXML
	private ScrollPane itemsScroll;
	
	@FXML
	private GridPane cartGrid;
	
	@FXML
	private Label ttlPrice;
	
	@FXML
	private Button saladsBtn;
	
	@FXML
	private Button startersBtn;
	
	@FXML
	private Button mainCoursesBtn;
	
	@FXML
	private Button dessertsBtn;
	
	@FXML
	private Button beveragesBtn;
	
	@FXML
	private ImageView optionsImage;
	
	/**
     * Constructs a RestaurantMenuScreenController.
     * 
     * @param wholeScreen the HBox container for the whole screen
     * @param prevController the previous controller which is {@code ChooseRestaurantScreenController}
     */
	public RestaurantMenuScreenController(HBox wholeScreen, Object prevController) {
		this.wholeScreen = wholeScreen;
		this.prevController = (ChooseRestaurantScreenController) prevController;
		customer = this.prevController.getCustomer();
		supplier = this.prevController.getPreferredSupplier();
	}
	
	public void initialize() {
		if(firstInitialization) {
			fetchItemsAndSort();
			firstInitialization = false;
		}
		else {
			enableCart();
			int row = 0;
			for(AnchorPane card : cartCards.values()) {
				cartGrid.add(card, 0, row);
				row++;
			}
			ttlPrice.setText(String.format("%.2f₪", totalPrice));
		}
		if(salads.isEmpty())
			saladsBtn.setDisable(true);
		if(starters.isEmpty())
			startersBtn.setDisable(true);
		if(mainCourses.isEmpty())
			mainCoursesBtn.setDisable(true);
		if(desserts.isEmpty())
			dessertsBtn.setDisable(true);
		if(beverages.isEmpty())
			beveragesBtn.setDisable(true);
		cartScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //Hide horizontal scrollbar
		itemsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //Hide horizontal scrollbar
	}
	
	public void pressSalads(ActionEvent event) {
		loadCategory(salads);
	}
	
	public void pressStarters(ActionEvent event) {
		loadCategory(starters);
	}
	
	public void pressMainCourses(ActionEvent event) {
		loadCategory(mainCourses);
	}
	
	public void pressDesserts(ActionEvent event) {
		loadCategory(desserts);
	}
	
	public void pressBeverages(ActionEvent event) {
		loadCategory(beverages);
	}
	
	private void fetchItemsAndSort() {
		ClientMainController.requestSupplierItems(supplier);
		ServerResponseDataContainer response = ClientConsole.responseFromServer;
		List<Item> items = ((ArrayList<Item>) response.getMessage());
		for(Item item : items) {
			switch(item.getType()) {
				case SALAD:
					salads.add(item);
					break;
				case FIRSTCOURSE:
					starters.add(item);
					break;
				case MAINCOURSE:
					mainCourses.add(item);
					break;
				case DESSERT:
					desserts.add(item);
					break;
				case BEVERAGE:
					beverages.add(item);
					break;
			}
		}
	}
	
	private void loadCategory(List<Item> items) {
		gridPane.getChildren().clear();
		int itemsCount = 0;
		
		for (Item item : items) {
			AnchorPane card = createItemCard(item);
			gridPane.add(card, 0, itemsCount);
			itemsCount++;
		}
		
	    gridPane.setVgap(15);
	}
	
	private String insertNewline(String description) {
        int maxLength = 80;
        
        // If the description is shorter than or equal to the maximum length, return it as is
        if (description.length() <= maxLength) {
            return description;
        }
        
        // Find the last space before or at the max length
        int newlineIndex = description.lastIndexOf(' ', maxLength);

        // If no space found and the string is longer than the max length, place the newline at maxLength
        if (newlineIndex == -1) {
            newlineIndex = maxLength;
        }
        
        // Insert the newline character at the determined index
        String firstPart = description.substring(0, newlineIndex).trim();
        String secondPart = description.substring(newlineIndex).trim();
        
        return firstPart + "\n" + secondPart;
    }
	
	/**
     * Creates an AnchorPane button for a single item
     * 
     * @param supplier the restaurant to display
     * @return an AnchorPane containing supplier's picture, city and address
     */
	private AnchorPane createItemCard(Item item) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/ItemCard.fxml"));
		AnchorPane card = null;
        try {
            card = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Labels adjustments
        Label itemNameLabel = (Label) card.lookup("#itemName");
        Label itemDescriptionLabel = (Label) card.lookup("#description");
        Label priceLabel = (Label) card.lookup("#price");
        itemNameLabel.setText(item.getName());
        String desc = insertNewline(item.getDescription());
        itemDescriptionLabel.setText(desc);
        priceLabel.setText(String.format("%.2f₪", item.getPrice()));
        
        //Image adjustments
        ImageView itemImage = (ImageView) card.lookup("#itemImage");
        String imgPath = item.getImageURL();
        try {
        	Image image = new Image(imgPath);
        	itemImage.setImage(image);
        } catch(IllegalArgumentException e) { //Image not found
        	Image no_image = new Image("/gui/resource/no_image.png");
        	itemImage.setImage(no_image);
        }
        
        card.setOnMouseClicked(event -> {
        		enableItemOptionsPane(item);
        });
        
        return card;
	}
	
	private void showAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	private void setSizePane(boolean set) {
		if(set)
			sizeTitle.setOpacity(1);
		else
			sizeTitle.setOpacity(0.5);
		small.setDisable(!set);
		medium.setDisable(!set);
		large.setDisable(!set);
		small.setSelected(false);
		medium.setSelected(false);
		large.setSelected(false);
		mustSelectSize.setVisible(false); //Rectangle to alert user
	}
	
	private void setDonenessPane(boolean set) {
		if(set)
			donenessTitle.setOpacity(1);
		else
			donenessTitle.setOpacity(0.5);
		MR.setDisable(!set);
		M.setDisable(!set);
		MW.setDisable(!set);
		WD.setDisable(!set);
		MR.setSelected(false);
		M.setSelected(false);
		MW.setSelected(false);
		WD.setSelected(false);
		mustSelectDoneness.setVisible(false); //Rectangle to alert user
	}
	
	private void setRestrictionsPane(boolean set) {
		if(set) {
			restrictionsTitle.setOpacity(1);
			restrictions.setPromptText("Example: Add extra lettuce");
		}
		else {
			restrictionsTitle.setOpacity(0.5);
			restrictions.setPromptText("");
		}
		restrictions.setDisable(!set);
		restrictions.clear();
	}
	
	private void enableItemOptionsPane(Item item) {

		boolean isSizeCustomizable = item.getCustomSize();
		boolean isDonenessCustomizable = item.getCustomDonenessDegree();
		boolean isRestrictionsCustomizable = item.getCustomRestrictions();

		itemOptionsPane.setOpacity(1);
		addToCartBtn.setDisable(false);
		pickedItem.setText(item.getName());
		pickedItem.setVisible(true);
		optionsImage.setVisible(true);
		try {
			Image img = new Image(item.getImageURL());
			optionsImage.setImage(img);
		} catch(IllegalArgumentException e) {
			Image img = new Image("/gui/resource/no_image.png");
			optionsImage.setImage(img);
		}

		setSizePane(isSizeCustomizable); // Enables size pane in case item has an option to change size
		setDonenessPane(isDonenessCustomizable); // Enables doneness pane in case item has an option to change doneness
		setRestrictionsPane(isRestrictionsCustomizable); // Enables restrictions pane in case item has an option to
															// change restrictions

		// Gather size radio buttons together
		ToggleGroup sizes = new ToggleGroup();
		small.setToggleGroup(sizes);
		medium.setToggleGroup(sizes);
		large.setToggleGroup(sizes);

		// Gather doneness radio buttons together
		ToggleGroup doneness = new ToggleGroup();
		MR.setToggleGroup(doneness);
		M.setToggleGroup(doneness);
		MW.setToggleGroup(doneness);
		WD.setToggleGroup(doneness);

		// In case of item edit
		if (item instanceof ItemInOrder) {
			addToCartBtn.setText("Edit");
			cartGrid.setDisable(true);
			gridPane.setDisable(true);
			garbageBtn.setDisable(true);
			checkoutBtn.setDisable(true);
			if (isSizeCustomizable) {
				switch (((ItemInOrder) item).getSize()) {
				case "Small":
					sizes.selectToggle(small);
					break;
				case "Medium":
					sizes.selectToggle(medium);
					break;
				case "Large":
					sizes.selectToggle(large);
					break;
				}
			}
			if (isDonenessCustomizable) {
				switch (((ItemInOrder) item).getDonenessDegree()) {
				case "MR":
					doneness.selectToggle(MR);
					break;
				case "M":
					doneness.selectToggle(M);
					break;
				case "MW":
					doneness.selectToggle(MW);
					break;
				case "WD":
					doneness.selectToggle(WD);
					break;
				}
			}
			if (isRestrictionsCustomizable) {
				String restrictionsTxt = ((ItemInOrder) item).getRestrictions();
				if (!restrictionsTxt.equals("None")) {
					restrictions.setText(restrictionsTxt);
				}
			}
		}

		addToCartBtn.setOnAction(event -> {
			ItemInOrder itemInOrder = new ItemInOrder(item);
			Toggle selectedSize = sizes.getSelectedToggle();
			Toggle selectedDoneness = doneness.getSelectedToggle();

			boolean missingInfo = false; // True in case user forgot to select options

			// Set user's size selection:
			if (isSizeCustomizable && selectedSize != null)
				itemInOrder.setSize(((RadioButton) selectedSize).getText());
			else if (isSizeCustomizable) {
				mustSelectSize.setVisible(true); // A mark for the user what's needed to be selected if not selected
				missingInfo = true;
			}

			// Set user's doneness selection:
			if (isDonenessCustomizable && selectedDoneness != null)
				itemInOrder.setDonenessDegree(((RadioButton) selectedDoneness).getText());
			else if (isDonenessCustomizable) {
				mustSelectDoneness.setVisible(true); // A mark for the user what's needed to be selected if not selected
				missingInfo = true;
			}

			if (isRestrictionsCustomizable) {
				String fill = restrictions.getText();
				if (!fill.trim().isEmpty()) // In case the fill is not empty
					itemInOrder.setRestrictions(fill);
			}

			if (missingInfo)
				showAlert(AlertType.ERROR, "Missing Information", "You must select marked options");
			else {
				if (item instanceof ItemInOrder) {
					int quantity = cart.get(item);
					AnchorPane card = cartCards.get(item);
					Integer row = GridPane.getRowIndex(card);
					AnchorPane newCard = createCartItemCard(itemInOrder);
					Label qty = (Label) newCard.lookup("#qty");
					qty.setText("Qty: " + quantity);
					cartGrid.getChildren().remove(card);
					cartGrid.add(newCard, 0, row);
					cart.remove(item);
					cartCards.remove(item);
					cart.put(itemInOrder, quantity);
					cartCards.put(itemInOrder, newCard);
					addToCartBtn.setText("Add to Cart");
					cartGrid.setDisable(false);
					gridPane.setDisable(false);
					garbageBtn.setDisable(false);
					checkoutBtn.setDisable(false);
				} else {
					addToCart(itemInOrder);
				}
				disableItemOptionsPane();
			}
		});
	}
	
	private void disableItemOptionsPane() {
		setSizePane(false);
		setDonenessPane(false);
		setRestrictionsPane(false);
		addToCartBtn.setDisable(true);
		pickedItem.setVisible(false);
		optionsImage.setVisible(false);
	}
	
	private enum Operation{
		REDUCE(-1),
		ADD(1);
		
		private final int value;
		
		Operation(int value){
			this.value = value;
		}
	}
	
	private void addToCart(ItemInOrder item) {
		
		totalPrice += item.getPrice();
		ttlPrice.setText(String.format("%.2f₪", totalPrice));
		
		//In case that's the first item in the cart
		if(cart.size() == 0) {
			enableCart();
		}
		
		//In case item already exists, just update it's quantity
		if(cart.containsKey(item)) {
			updateQuantity(item, Operation.ADD);
		}
		else {
			cart.put(item, 1);
			AnchorPane card = createCartItemCard(item);
			cartCards.put(item, card);
			cartGrid.add(card, 0, cartCards.size() - 1);
		}
	}
	
	private AnchorPane createCartItemCard(ItemInOrder item) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/CartItemCard.fxml"));
		AnchorPane card = null;
        try {
            card = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Labels
        Label nameAndPrice = (Label) card.lookup("#nameAndPrice");
        Label qty = (Label) card.lookup("#qty");
        Label choices = (Label) card.lookup("#choices");
        Label restrictions = (Label) card.lookup("#restrictions");
        
        //Labels adjustments
        nameAndPrice.setText(String.format("%s (%.2f₪)", item.getName(), item.getPrice()));
        qty.setText("Qty: 1");
        if(item.getCustomSize() && item.getCustomDonenessDegree())
        	choices.setText("Size: " + item.getSize().charAt(0) + ", " + "Doneness: " + item.getDonenessDegree());
        else if(item.getCustomSize()) 
        	choices.setText("Size: " + item.getSize().charAt(0));
        else if(item.getCustomDonenessDegree())
        	choices.setText("Doneness: " + item.getDonenessDegree());
        else
        	choices.setText("Size: N/A, Doneness: N/A");
        if(item.getCustomRestrictions())
        	restrictions.setText("Extras/Removals: " + item.getRestrictions());
        else
        	restrictions.setText("Extras/Removals: N/A");
        
        ImageView add = (ImageView) card.lookup("#add");
        ImageView reduce = (ImageView) card.lookup("#reduce");
        ImageView edit = (ImageView) card.lookup("#edit");
        //Set opacity of not editable item to 0.5
        if(!(item.getCustomSize() || item.getCustomDonenessDegree() || item.getCustomRestrictions())) {
        	edit.setOpacity(0.5);
        }
        
        add.setOnMouseClicked(event -> {
        	totalPrice += item.getPrice();
        	ttlPrice.setText(String.format("%.2f₪", totalPrice));
        	updateQuantity(item, Operation.ADD);
        });
        
        reduce.setOnMouseClicked(event -> {
        	totalPrice -= item.getPrice();
        	ttlPrice.setText(String.format("%.2f₪", totalPrice));
        	updateQuantity(item, Operation.REDUCE);
        });
        
        edit.setOnMouseClicked(event -> {
        	if(item.getCustomSize() || item.getCustomDonenessDegree() || item.getCustomRestrictions())
        		enableItemOptionsPane(item);
        });
        
        return card;
	}
	
	private void enableCart() {
		emptyCart.setVisible(false);
		checkoutBtn.setDisable(false);
		garbageBtn.setDisable(false);
		Platform.runLater(() -> {
			Node viewport = cartScroll.lookup(".viewport");
			if (viewport != null) {
                viewport.setStyle("-fx-background-color: transparent;");
            }
        });
		cartScroll.setVisible(true);
	}
	
	private void disableCart() {
		emptyCart.setVisible(true);
		checkoutBtn.setDisable(true);
		garbageBtn.setDisable(true);
		cartScroll.setVisible(false);
	}
	
	private void updateQuantity(ItemInOrder item, Operation op) {
		int quantity = cart.get(item) + op.value; //Adds or reduces quantity according to the operation
		cart.put(item, quantity);
		AnchorPane card = cartCards.get(item);
		
		if(quantity > 0) {
			Label qty = (Label) card.lookup("#qty");
			qty.setText("Qty: " + quantity);
		}
		else {
			cart.remove(item);
			cartCards.remove(item);
			cartGrid.getChildren().clear(); //Remove all cards
			
			//Place them back one by one
			int row = 0;
			for(AnchorPane cartItemCard : cartCards.values()) {
				cartGrid.add(cartItemCard, 0, row);
				row++;
			}
			
			//When the cart is empty, disable it
			if(cartCards.isEmpty()) {
				disableCart();
			}
		}
		
	}
	
	@FXML
	private void discardAllItems(ActionEvent event) {
		cart.clear();
		cartCards.clear();
		cartGrid.getChildren().clear();
		disableCart();
		totalPrice = 0;
		ttlPrice.setText(String.format("%.2f₪", totalPrice));
	}
	
	@FXML
	private void pressCheckout() throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
    	String path = "/gui/view/CheckoutScreen.fxml";
    	AnchorPane nextDash = screenLoader.loadOnDashboard(wholeScreen, path, Screen.CHECKOUT_SCREEN, this);
    	dashboard.getChildren().clear(); //Clear current dashboard
    	dashboard.getChildren().add(nextDash); //Assign the new dashboard
	}
	
	
	public Map<ItemInOrder, Integer> getCart() {
	    return cart;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public Supplier getSupplier() {
		return supplier;
	}
	
	public float getTotalPrice() {
		return totalPrice;
	}
	
	public ChooseRestaurantScreenController getRestaurantController(){
		return prevController;
	}
	
	@FXML
	private void pressBack(ActionEvent event) throws IOException {
		ScreenLoader screenLoader = new ScreenLoader();
		String path = "/gui/view/ChooseRestaurantScreen.fxml";
		AnchorPane prevDash = screenLoader.loadPreviousDashboard(path, Screen.CHOOSE_RESTAURANT_SCREEN, prevController);
		dashboard.getChildren().clear();
		dashboard.getChildren().add(prevDash);
	}
}
