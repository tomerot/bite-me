package gui.controllers;

import javafx.scene.layout.HBox;

public class RemoveItemScreenController {

	private EmployeeHomeScreenController prevController;
	private HBox wholeScreen;

	public RemoveItemScreenController(HBox wholeScreen, Object prevController) {
		this.prevController = (EmployeeHomeScreenController) prevController;
		this.wholeScreen = wholeScreen;
	}

}
