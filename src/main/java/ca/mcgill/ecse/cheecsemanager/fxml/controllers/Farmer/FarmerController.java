package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PopupController;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.FarmerDataProvider;
import java.io.IOException;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Controller for the farmer page
 * Handles displaying and managing farmers
 * Includes functionality for adding, deleting, and searching farmers
 * @author Ewen Gueguen
 */
public class FarmerController
    extends PopupController implements PageNavigator.PageRefreshable {
  private final FarmerDataProvider farmerDataProvider =
      FarmerDataProvider.getInstance();

  @FXML private AnchorPane farmerRoot;
  @FXML private FlowPane cardsContainer;
  @FXML private TextField searchField;
  @FXML private StyledButton addFarmerBtn;

  private Region
      contentToBlur; // Reference to the content that should be blurred

  private FilteredList<TOFarmer> filteredFarmers;

  public AnchorPane getFarmerRoot() { return farmerRoot; }

  @FXML
  public void initialize() {
    // Setup blur effect reference
    if (!farmerRoot.getChildren().isEmpty()) {
      contentToBlur = (Region)farmerRoot.getChildren().get(0);
    }
    farmerRoot.setUserData(this);

    ObservableList<TOFarmer> farmers = farmerDataProvider.getFarmers();

    // Wrap in FilteredList for search (see Alternative 2 for details)
    filteredFarmers = new FilteredList<>(farmers, p -> true);

    // Add listener to the FILTERED list (so search works)
        filteredFarmers.addListener((ListChangeListener.Change<? extends TOFarmer> change) -> {
      while (change.next()) {
        if (change.wasPermutated()) {
          // Handle reordering if needed
          rebuildAllCards(); // Full refresh for permutations
          continue;
        }
        if (change.wasRemoved()) {
          // Remove only the deleted cards
          change.getRemoved().forEach(this::removeCardForFarmer);
        }
        if (change.wasAdded()) {
          // Add only the new cards
          change.getAddedSubList().forEach(this::addNewFarmerToList);
        }
        if (change.wasUpdated()) {
          // Refresh updated cards
          for (int i = change.getFrom(); i < change.getTo(); i++) {
            updateCardForFarmer(filteredFarmers.get(i));
          }
        }
      }
        });
        rebuildAllCards();

        // Setup search
        if (searchField != null) {
          searchField.textProperty().addListener(
              (obs, oldVal, newVal)
                  -> filteredFarmers.setPredicate(
                      farmer
                      -> newVal == null || newVal.isEmpty() ||
                             farmer.getName().toLowerCase().contains(
                                 newVal.toLowerCase()) ||
                             farmer.getEmail().toLowerCase().contains(
                                 newVal.toLowerCase()) ||
                             farmer.getAddress().toLowerCase().contains(
                                 newVal.toLowerCase())));
        }
  }

  @Override
  public void onPageAppear() {
    farmerDataProvider.refresh();
  }

  public void addNewFarmerToList(TOFarmer farmer) {
    FarmerCard card = new FarmerCard();
    card.setFarmer(farmer);
    card.setFarmerController(this);
    cardsContainer.getChildren().add(card);
  }

  private void removeCardForFarmer(TOFarmer farmer) {
    cardsContainer.getChildren().removeIf(
        node
        -> node instanceof FarmerCard && ((FarmerCard)node)
                                             .getFarmer()
                                             .getEmail()
                                             .equals(farmer.getEmail()));
  }

  private void updateCardForFarmer(TOFarmer farmer) {
    cardsContainer.getChildren()
        .stream()
        .filter(node -> node instanceof FarmerCard)
        .map(node -> (FarmerCard)node)
        .filter(card -> card.getFarmer().getEmail().equals(farmer.getEmail()))
        .findFirst()
        .ifPresent(FarmerCard::refresh);
  }

  private void rebuildAllCards() {
    cardsContainer.getChildren().clear();
    filteredFarmers.forEach(this::addNewFarmerToList);
  }

  @FXML
  public void addFarmer(javafx.event.ActionEvent event) {
    if (contentToBlur != null) {
      contentToBlur.setEffect(new BoxBlur(5, 5, 3));
    }
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/" +
                                 "components/Farmer/AddFarmer.fxml"));
      AnchorPane popup = loader.load();

      popup.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

      // Create overlay FIRST
      StackPane overlay = createOverlay();

      // Add popup to overlay, then overlay to root
      overlay.getChildren().add(popup);
      farmerRoot.getChildren().add(overlay);

      AddFarmerPopup controller = loader.getController();
      controller.setFarmerController(this);
      controller.setPopupOverlay(overlay);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void deleteFarmerPopup(FarmerCard card) {
    if (contentToBlur != null) {
      contentToBlur.setEffect(new BoxBlur(5, 5, 3));
    }
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/" +
                                 "components/Farmer/DeleteFarmerPopup.fxml"));
      AnchorPane popup = loader.load();
      popup.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

      StackPane overlay = createOverlay();

      overlay.getChildren().add(popup);
      farmerRoot.getChildren().add(overlay);

      DeleteFarmerPopup controller = loader.getController();
      controller.setFarmerCard(card);
      controller.setFarmerController(this);
      controller.setPopupOverlay(overlay);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void removePopup(StackPane overlay) {
    if (contentToBlur != null) {
      contentToBlur.setEffect(null);
    }
    farmerRoot.getChildren().remove(overlay);
  }

  public String deleteFarmerCard(TOFarmer farmer, FarmerCard card,
                                 StackPane overlay) {
    String error =
        CheECSEManagerFeatureSet7Controller.deleteFarmer(farmer.getEmail());

    if (error == null || error.isEmpty()) {
      // Success - remove the card from UI and close popup
      farmerDataProvider.getFarmers().removeIf(
          f -> f.getEmail().equals(farmer.getEmail()));
      removePopup(overlay);
      getFarmerRoot().fireEvent(new ToastEvent("Farmer deleted successfully.",
                                               ToastEvent.ToastType.SUCCESS));
      return "";
    } else {
      // Error - keep popup open and return error message
      return error;
    }
  }

  public void refreshAllCards() {
    List<TOFarmer> currentFarmers =
        CheECSEManagerFeatureSet7Controller.getFarmers();

    // Collect cards to remove (farmers that no longer exist)
    List<javafx.scene.Node> cardsToRemove = new java.util.ArrayList<>();
    List<javafx.scene.Node> cardsToAdd = new java.util.ArrayList<>();

    cardsContainer.getChildren().forEach(node -> {
      if (node instanceof FarmerCard) {
        FarmerCard card = (FarmerCard)node;
        TOFarmer cardFarmer = card.getFarmer();

        // Check if this farmer still exists in the manager
        boolean farmerStillExists = currentFarmers.stream().anyMatch(
            f -> f.getEmail().equals(cardFarmer.getEmail()));

        if (farmerStillExists) {
          // Farmer exists, just refresh the card
          card.refresh();
        } else {
          // Farmer was deleted, mark card for removal
          cardsToRemove.add(node);
        }
      }
    });

    // Remove cards for deleted farmers
    cardsContainer.getChildren().removeAll(cardsToRemove);
  }
}
