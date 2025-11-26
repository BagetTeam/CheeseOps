package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PopupController;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator;

import java.io.IOException;

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
        
        CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
        for (Farmer farmer : manager.getFarmers()) {
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
                    Farmer farmer = card.getFarmer();
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

    public void addNewFarmerToList(Farmer farmer) {
        addFarmerCard(farmer);
    }

    private void addFarmerCard(Farmer farmer) {
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

    public void deleteFarmerCard(FarmerCard card, StackPane overlay) {
        removePopup(overlay);
        cardsContainer.getChildren().remove(card);
        
        card.getFarmer().delete();
        CheECSEManagerPersistence.save();
    }

    public void refreshAllCards() {
        cardsContainer.getChildren().forEach(node -> {
            if (node instanceof FarmerCard) {
                ((FarmerCard) node).refresh();
            }
        });
    }
}