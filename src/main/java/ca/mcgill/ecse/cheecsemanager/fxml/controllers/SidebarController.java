package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Controller backing the persistent sidebar navigation, responsible for routing
 * and button highlighting.
 * @author Ming Li Liu
 */
public class SidebarController {
  /** Supported main pages that the sidebar can navigate to. */
  public enum Page {
    SHELVES,
    FARMERS,
    COMPANIES,
    ROBOT,
    CHEESEWHEELS,
    SETTINGS
  }

  Page page = Page.SHELVES;

  @FXML StyledButton buttonShelves;
  @FXML StyledButton buttonFarmers;
  @FXML StyledButton buttonCompanies;
  @FXML StyledButton buttonRobot;
  @FXML StyledButton buttonCheeseWheels;
  @FXML StyledButton buttonSettings;
  @FXML VBox sidebar;

  // Callback for navigation - can be set by parent controller
  private Callback<Page, Boolean> navigationCallback;

  /** Hooks up popup hiding behavior and sets the initial button styles. */
  @FXML
  public void initialize() {
    sidebar.setOnMouseClicked(e -> sidebar.fireEvent(new HidePopupEvent()));
    this.updateButtonStyles();
  }

  /**
   * Registers a callback used to perform the actual navigation when a button is
   * pressed.
   * @param callback callback returning whether navigation succeeded
   */
  public void setNavigationCallback(Callback<Page, Boolean> callback) {
    this.navigationCallback = callback;
  }

  /** Handles the Shelves button press. */
  @FXML
  private void handleShelves(ActionEvent event) {
    navigateTo(Page.SHELVES);
  }

  /** Handles the Farmers button press. */
  @FXML
  private void handleFarmers(ActionEvent event) {
    navigateTo(Page.FARMERS);
  }

  /** Handles the Companies button press. */
  @FXML
  private void handleCompanies(ActionEvent event) {
    navigateTo(Page.COMPANIES);
  }

  /** Handles the Robot button press. */
  @FXML
  private void handleRobot(ActionEvent event) {
    navigateTo(Page.ROBOT);
  }

  /** Handles the Settings button press. */
  @FXML
  private void handleSettings(ActionEvent event) {
    navigateTo(Page.SETTINGS);
  }

  /** Handles the Cheese Wheels button press. */
  @FXML
  private void handleCheeseWheels(ActionEvent event) {
    navigateTo(Page.CHEESEWHEELS);
  }

  /**
   * Routes to the requested page, ensuring popups are closed and button styles
   * stay in sync.
   */
  private void navigateTo(Page page) {
    sidebar.fireEvent(new HidePopupEvent());
    if (page == this.page) {
      return;
    }
    if (navigationCallback != null) {
      if (navigationCallback.call(page)) {
        this.page = page;
        this.updateButtonStyles();
      };
    }
  }

  /** Applies the correct visual variant to each navigation button. */
  private void updateButtonStyles() {
    buttonShelves.setVariant(page.equals(Page.SHELVES)
                                 ? StyledButton.Variant.PRIMARY
                                 : StyledButton.Variant.DEFAULT);
    buttonFarmers.setVariant(page.equals(Page.FARMERS)
                                 ? StyledButton.Variant.PRIMARY
                                 : StyledButton.Variant.DEFAULT);
    buttonCompanies.setVariant(page.equals(Page.COMPANIES)
                                   ? StyledButton.Variant.PRIMARY
                                   : StyledButton.Variant.DEFAULT);
    buttonRobot.setVariant(page.equals(Page.ROBOT)
                               ? StyledButton.Variant.PRIMARY
                               : StyledButton.Variant.DEFAULT);
    buttonCheeseWheels.setVariant(page.equals(Page.CHEESEWHEELS)
                                      ? StyledButton.Variant.PRIMARY
                                      : StyledButton.Variant.DEFAULT);
    buttonSettings.setVariant(page.equals(Page.SETTINGS)
                                  ? StyledButton.Variant.PRIMARY
                                  : StyledButton.Variant.DEFAULT);
  }
}
