package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PopupController;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;

public class FarmerController extends PopupController implements PageNavigator.PageRefreshable {
    @FXML private AnchorPane farmerRoot;
    @FXML private FlowPane cardsContainer;
    @FXML private TextField searchField;
    @FXML private StyledButton addFarmerBtn;
    
    private Region contentToBlur; // Reference to the content that should be blurred

    @FXML
    public void initialize() {
        // Get the first child of farmerRoot (the VBox containing all content)
        if (!farmerRoot.getChildren().isEmpty()) {
            contentToBlur = (Region) farmerRoot.getChildren().get(0);
        }
        
        // Store this controller in the root pane's userData for later refresh
        farmerRoot.setUserData(this);
        
        for (TOFarmer farmer : CheECSEManagerFeatureSet7Controller.getFarmers()) {
            addFarmerCard(farmer);
        }

        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterFarmers(newValue);
            });
        }
    }
    
    @Override
    public void onPageAppear() {
        refreshAllCards();
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
                    FarmerCard card = (FarmerCard) node;
                    TOFarmer farmer = card.getFarmer();
                    boolean matches = farmer.getName().toLowerCase().contains(lowerCaseSearch) ||
                                    farmer.getEmail().toLowerCase().contains(lowerCaseSearch) ||
                                    farmer.getAddress().toLowerCase().contains(lowerCaseSearch);
                    node.setVisible(matches);
                    node.setManaged(matches);
                }
            });
        }
    }

    @FXML
    public void addFarmer(javafx.event.ActionEvent event) {
        if (contentToBlur != null) {
            contentToBlur.setEffect(new BoxBlur(5, 5, 3));
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Farmer/AddFarmer.fxml"
            ));
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

    public void addNewFarmerToList(TOFarmer farmer) {
        addFarmerCard(farmer);
    }

    private void addFarmerCard(TOFarmer farmer) {
        FarmerCard card = new FarmerCard();
        card.setFarmer(farmer);
        card.setFarmerController(this);
        cardsContainer.getChildren().add(card);
    }

    public void deleteFarmerPopup(FarmerCard card) {
        if (contentToBlur != null) {
            contentToBlur.setEffect(new BoxBlur(5, 5, 3));
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Farmer/DeleteFarmerPopup.fxml"
            ));
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

    public String deleteFarmerCard(TOFarmer farmer, FarmerCard card, StackPane overlay) {
        String error = CheECSEManagerFeatureSet7Controller.deleteFarmer(farmer.getEmail());
        
        if (error == null || error.isEmpty()) {
            // Success - remove the card from UI and close popup
            if (card != null) {
                System.out.println("Farmer deleted successfully");
                cardsContainer.getChildren().remove(card);
            }
            removePopup(overlay);
            return "";
        } else {
            // Error - keep popup open and return error message
            return error;
        }
    }

    public void refreshAllCards() {
        List<TOFarmer> currentFarmers = CheECSEManagerFeatureSet7Controller.getFarmers();
        
        // Collect cards to remove (farmers that no longer exist)
        List<javafx.scene.Node> cardsToRemove = new java.util.ArrayList<>();
        
        cardsContainer.getChildren().forEach(node -> {
            if (node instanceof FarmerCard) {
                FarmerCard card = (FarmerCard) node;
                TOFarmer cardFarmer = card.getFarmer();
                
                // Check if this farmer still exists in the manager
                boolean farmerStillExists = currentFarmers.stream()
                    .anyMatch(f -> f.getEmail().equals(cardFarmer.getEmail()));
                
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