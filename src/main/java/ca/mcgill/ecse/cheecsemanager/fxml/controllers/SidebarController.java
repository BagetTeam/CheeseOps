package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class SidebarController {
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

  @FXML
  public void initialize() {
    sidebar.setOnMouseClicked(e -> sidebar.fireEvent(new HidePopupEvent()));
    this.updateButtonStyles();
  }

  public void setNavigationCallback(Callback<Page, Boolean> callback) {
    this.navigationCallback = callback;
  }

  @FXML
  private void handleShelves(ActionEvent event) {
    navigateTo(Page.SHELVES);
  }

  @FXML
  private void handleFarmers(ActionEvent event) {
    navigateTo(Page.FARMERS);
  }

  @FXML
  private void handleCompanies(ActionEvent event) {
    navigateTo(Page.COMPANIES);
  }

  @FXML
  private void handleRobot(ActionEvent event) {
    navigateTo(Page.ROBOT);
  }

  @FXML
  private void handleSettings(ActionEvent event) {
    navigateTo(Page.SETTINGS);
  }

  @FXML
  private void handleCheeseWheels(ActionEvent event) {
    navigateTo(Page.CHEESEWHEELS);
  }

  private void navigateTo(Page page) {
    sidebar.fireEvent(new HidePopupEvent());
    if (navigationCallback != null) {
      if (navigationCallback.call(page)) {
        this.page = page;
        this.updateButtonStyles();
      };
    }
  }

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
