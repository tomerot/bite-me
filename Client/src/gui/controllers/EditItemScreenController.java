package gui.controllers;

import javafx.scene.layout.HBox;

public class EditItemScreenController {

	private EmployeeHomeScreenController prevController;
	private HBox wholeScreen;

	public EditItemScreenController(HBox wholeScreen, Object prevController) {
		this.prevController = (EmployeeHomeScreenController) prevController;
		this.wholeScreen = wholeScreen;
	}
}
