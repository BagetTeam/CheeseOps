package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SidebarController {
  String page = "";

  @FXML StyledButton buttonShelves;
  @FXML StyledButton buttonFarmers;
  @FXML StyledButton buttonCompanies;
  @FXML StyledButton buttonRobot;
  @FXML StyledButton buttonCheeseWheels;
  @FXML StyledButton buttonSettings;

  // Callback for navigation - can be set by parent controller
  private Consumer<String> navigationCallback;

  @FXML
  public void initialize() {
    this.updateButtonStyles();
  }

  public void setNavigationCallback(Consumer<String> callback) {
    this.navigationCallback = callback;
  }

  @FXML
  private void handleShelves(ActionEvent event) {
    navigateTo("shelves");
  }

  @FXML
  private void handleFarmers(ActionEvent event) {
    navigateTo("farmers");
  }

  @FXML
  private void handleCompanies(ActionEvent event) {
    navigateTo("companies");
  }

  @FXML
  private void handleRobot(ActionEvent event) {
    navigateTo("robot");
  }

  @FXML
  private void handleSettings(ActionEvent event) {
    navigateTo("settings");
  }

  @FXML
  private void handleCheeseWheels(ActionEvent event) {
    navigateTo("cheeseWheels");
  }

  private void navigateTo(String page) {
    if (navigationCallback != null) {
      navigationCallback.accept(page);
      this.page = page;
      this.updateButtonStyles();
    }
  }

  private void updateButtonStyles() {
    buttonShelves.setVariant(page.equals("shelves")
                                 ? StyledButton.Variant.PRIMARY
                                 : StyledButton.Variant.DEFAULT);
    buttonFarmers.setVariant(page.equals("farmers")
                                 ? StyledButton.Variant.PRIMARY
                                 : StyledButton.Variant.DEFAULT);
    buttonCompanies.setVariant(page.equals("companies")
                                   ? StyledButton.Variant.PRIMARY
                                   : StyledButton.Variant.DEFAULT);
    buttonRobot.setVariant(page.equals("robot") ? StyledButton.Variant.PRIMARY
                                                : StyledButton.Variant.DEFAULT);
    buttonCheeseWheels.setVariant(page.equals("cheeseWheels")
                                      ? StyledButton.Variant.PRIMARY
                                      : StyledButton.Variant.DEFAULT);
    buttonSettings.setVariant(page.equals("settings")
                                  ? StyledButton.Variant.PRIMARY
                                  : StyledButton.Variant.DEFAULT);
  }
}
