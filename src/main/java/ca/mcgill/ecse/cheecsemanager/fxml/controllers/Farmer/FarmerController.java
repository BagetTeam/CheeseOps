package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;

public class FarmerController {
    @FXML private AnchorPane farmerRoot;
    @FXML private FlowPane cardsContainer;
    @FXML private TextField searchField;
    @FXML private StyledButton addFarmerBtn;

    @FXML
    public void addFarmer(javafx.event.ActionEvent event) {
        cardsContainer.setEffect(new BoxBlur(5, 5, 3));
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

    @FXML
    public void initialize() {
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

    private void addFarmerCard(Farmer farmer) {
        FarmerCard card = new FarmerCard();
        card.setFarmer(farmer);
        card.setFarmerController(this);
        cardsContainer.getChildren().add(card);
    }

    public void deleteFarmerPopup(FarmerCard card) {
        cardsContainer.setEffect(new BoxBlur(5, 5, 3));
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

    private void centerPopup(AnchorPane popup, StackPane overlay) {
        // Center the popup using AnchorPane constraints
        overlay.applyCss();
        overlay.layout();
        System.out.println("Centering popup " + farmerRoot.getWidth() + " " + farmerRoot.getHeight());
        System.out.println("Centering popup " + popup.getPrefWidth() + " " + popup.getPrefHeight());
        System.out.println("Centering popup " + popup.getLayoutBounds().getWidth() + " " + popup.getLayoutBounds().getHeight());

        double popupWidth = popup.getLayoutBounds().getWidth();
        double popupHeight = popup.getLayoutBounds().getHeight();

        // double centerX = (overlay.getWidth() - popupWidth) / 2;
        // double centerY = (overlay.getHeight() - popupHeight) / 2;
        
        // popup.setLayoutX(centerX);
        // popup.setLayoutY(centerY);

        popup.layoutXProperty().bind(
            overlay.widthProperty().subtract(popupWidth).divide(2)
        );
        popup.layoutYProperty().bind(
            overlay.heightProperty().subtract(popupHeight).divide(2)
        );
    }

    // Create a semi-transparent overlay behind the popup
    private StackPane createOverlay() {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        
        AnchorPane.setTopAnchor(overlay, 0.0);
        AnchorPane.setBottomAnchor(overlay, 0.0);
        AnchorPane.setLeftAnchor(overlay, 0.0);
        AnchorPane.setRightAnchor(overlay, 0.0);
        
        return overlay;
    }

    // Remove popup and clear blur
    public void removePopup(StackPane overlay) {
        cardsContainer.setEffect(null);
        farmerRoot.getChildren().remove(overlay);
    }

    public void deleteFarmerCard(FarmerCard card, StackPane overlay) {
        removePopup(overlay);
        cardsContainer.getChildren().remove(card);
        
        card.getFarmer().delete();
        CheECSEManagerPersistence.save();
    }
}