package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class FarmerController {
  private TextField searchField;

  @FXML private BorderPane farmerRoot;
  @FXML private FlowPane cardsContainer;
  @FXML private Input searchInput;
  @FXML private StyledButton addFarmerBtn;

  @FXML
  public void addFarmer(javafx.event.ActionEvent event) {
    cardsContainer.setEffect(new BoxBlur(5, 5, 3));
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/"
                                 + "components/Farmer/AddFarmer.fxml"));
      AnchorPane popup = loader.load();
      centerPopup(popup);
      AnchorPane overlay = createOverlay(popup);
      farmerRoot.getChildren().add(overlay);

      AddFarmerPopup controller = loader.getController();
      controller.setFarmerController(this);
      controller.setPopupOverlay(overlay);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addNewFarmerToList(Farmer farmer) { addFarmerCard(farmer); }

  @FXML
  public void initialize() {
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    for (Farmer farmer : manager.getFarmers()) {
      addFarmerCard(farmer);
    }
    this.searchField = searchInput.getTextField();

    if (searchField != null) {
      searchField.textProperty().addListener(
          (observable, oldValue, newValue) -> { filterFarmers(newValue); });
    }
  }

  private void filterFarmers(String searchText) {
    if (searchText == null || searchText.trim().isEmpty()) {
      // Show all farmers
      cardsContainer.getChildren().forEach(node -> node.setVisible(true));
      cardsContainer.getChildren().forEach(node -> node.setManaged(true));
    } else {
      String lowerCaseSearch = searchText.toLowerCase();
      cardsContainer.getChildren().forEach(node -> {
        if (node instanceof FarmerCard) {
          FarmerCard card = (FarmerCard)node;
          Farmer farmer = card.getFarmer();
          boolean matches =
              farmer.getName().toLowerCase().contains(lowerCaseSearch) ||
              farmer.getEmail().toLowerCase().contains(lowerCaseSearch) ||
              farmer.getAddress().toLowerCase().contains(lowerCaseSearch);
          node.setVisible(matches);
          node.setManaged(matches);
        }
      });
    }
  }

  private void addFarmerCard(Farmer farmer) {
    FarmerCard card = new FarmerCard();
    card.setFarmer(farmer);
    card.setFarmerController(this);
    cardsContainer.getChildren().add(card);
  }

  public void deleteFarmerPopup(FarmerCard card) {
    cardsContainer.setEffect(new BoxBlur(5, 5, 3));
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/"
                                 + "components/Farmer/DeleteFarmerPopup.fxml"));
      AnchorPane popup = loader.load();
      centerPopup(popup);
      AnchorPane overlay = createOverlay(popup);
      farmerRoot.getChildren().add(overlay);

      DeleteFarmerPopup controller = loader.getController();
      controller.setFarmerCard(card);
      controller.setFarmerController(this);
      controller.setPopupOverlay(overlay);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void centerPopup(AnchorPane popup) {
    popup.setLayoutX((farmerRoot.getWidth() - popup.getPrefWidth()) / 2);
    popup.setLayoutY((farmerRoot.getHeight() - popup.getPrefHeight()) / 2);
  }

  // Create a semi-transparent overlay behind the popup
  private AnchorPane createOverlay(AnchorPane popup) {
    AnchorPane overlay = new AnchorPane();
    overlay.setPrefSize(farmerRoot.getWidth(), farmerRoot.getHeight());
    overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
    overlay.getChildren().add(popup);
    return overlay;
  }

  // Remove popup and clear blur
  public void removePopup(AnchorPane overlay) {
    cardsContainer.setEffect(null);
    farmerRoot.getChildren().remove(overlay);
  }

  public void deleteFarmerCard(FarmerCard card, AnchorPane overlay) {
    removePopup(overlay);
    cardsContainer.getChildren().remove(card);

    card.getFarmer().delete();
    CheECSEManagerPersistence.save();
  }
}
