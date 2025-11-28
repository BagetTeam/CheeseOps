package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.fxml.components.Icon;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PopupController;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.FarmerDataProvider;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;

import java.io.IOException;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;

/**
 * Controller for the view farmer page
 * Handles displaying a farmer's data including cheese wheel purchases for that farmer 
 * Handles updating, deleting, and buying cheese for a farmer
 * @author Ewen Gueguen
 */
public class ViewFarmerController extends PopupController implements PageNavigator.DataReceiver{
  @FXML private Button backBtn;

  @FXML private VBox farmerDescriptionCard;

  @FXML private ImageView photoView;
  @FXML private Label nameLabel;
  @FXML private Label emailLabel;
  @FXML private Label addressLabel;
  @FXML private Button updateBtn;
  @FXML private Button deleteBtn;

  @FXML private TableView<TOCheeseWheel> cheeseTable;
  @FXML private TableColumn<TOCheeseWheel, Integer> idColumn;
  @FXML private TableColumn<TOCheeseWheel, String> ageColumn;
  @FXML private TableColumn<TOCheeseWheel, String> spoiledColumn;
  @FXML private TableColumn<TOCheeseWheel, String> dateColumn;
  @FXML private TableColumn<TOCheeseWheel, Void> actionColumn;

  @FXML private AnchorPane viewFarmerRoot;

  private Region contentToBlur;

  private final FarmerDataProvider farmerDataProvider = FarmerDataProvider.getInstance();
  private TOFarmer farmer;

  public AnchorPane getViewFarmerRoot() {
    return viewFarmerRoot;
  }

  @FXML
  public void initialize() {
    // Get the first child of viewFarmerRoot (the VBox containing all content)
    if (!viewFarmerRoot.getChildren().isEmpty()) {
      contentToBlur = (Region) viewFarmerRoot.getChildren().get(0);
    }
    
    // Initialize columns
    idColumn.setCellValueFactory(
        cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

    ageColumn.setCellValueFactory(
        cellData
        -> new SimpleStringProperty(
            cellData.getValue().getMonthsAged().toString()));

    spoiledColumn.setCellValueFactory(
        cellData
        -> new SimpleStringProperty(cellData.getValue().getIsSpoiled() ? "Yes"
                                                                       : "No"));

    dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurchaseDate().toString()));

    farmerDescriptionCard.setMaxHeight(Region.USE_PREF_SIZE);

    // Set placeholder for empty table
    Label placeholder = new Label("No cheese wheels purchased yet");
    placeholder.setStyle("-fx-text-fill: -color-muted; -fx-font-size: 14px;");
    cheeseTable.setPlaceholder(placeholder);

    // Hide empty rows
    cheeseTable.setRowFactory(tv -> {
      javafx.scene.control.TableRow<TOCheeseWheel> row =
          new javafx.scene.control.TableRow<>();
      row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
        if (isNowEmpty) {
          row.setStyle(
              "-fx-background-color: transparent; -fx-opacity: 0; "
              + "-fx-pref-height: 0; -fx-max-height: 0; -fx-min-height: 0;");
        } else {
          row.setStyle("");
        }
      });
      return row;
    });

    // Add View Button column
    actionColumn.setCellFactory(param -> new TableCell<>() {
      private final StyledButton viewBtn;
      {
        Icon eyeIcon = new Icon("Eye");

        viewBtn = new StyledButton("view", eyeIcon);
        viewBtn.setVariant(StyledButton.Variant.PRIMARY);
        viewBtn.setSize(StyledButton.Size.SM);

        viewBtn.setOnAction(event -> {
          TOCheeseWheel cheeseWheel = getTableView().getItems().get(getIndex());
          handleViewCheeseWheel(cheeseWheel);
        });
      }

      @Override
      public void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(viewBtn);
          setAlignment(Pos.CENTER);
        }
      }
    });
  }

  public void setFarmer(TOFarmer farmer) {
    this.farmer = farmer;
    if (farmer != null) {
      nameLabel.setText(farmer.getName());
      emailLabel.setText(farmer.getEmail());
      addressLabel.setText(farmer.getAddress());
      bindCheeseWheelsToFarmer(farmer.getEmail());
    } else {
      cheeseTable.setItems(FXCollections.emptyObservableList());
    }
  }

  @Override
  public void setData(Object data) {
    if (data instanceof TOFarmer) {
      setFarmer((TOFarmer)data);
    }
  }

  @FXML
  private void handleBack() {
    PageNavigator.getInstance().goBack();
  }

  @FXML
  private void handleEdit() {
    System.out.println("Edit farmer: " + farmer.getName());
    if (contentToBlur != null) {
            contentToBlur.setEffect(new BoxBlur(5, 5, 3));
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Farmer/UpdateFarmer.fxml"
            ));
            AnchorPane popup = loader.load();
            popup.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
            // Create overlay FIRST
            StackPane overlay = createOverlay();
            
            // Add popup to overlay, then overlay to root
            overlay.getChildren().add(popup);
            viewFarmerRoot.getChildren().add(overlay);
                        
            UpdateFarmerPopup controller = loader.getController();
            controller.setFarmerData(farmer);
            controller.setViewFarmerController(this);
            controller.setPopupOverlay(overlay);
        } catch (IOException e) {
            e.printStackTrace();
        }
  }

  @FXML
  private void handleDelete() {
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
            viewFarmerRoot.getChildren().add(overlay);
    
            DeleteFarmerPopup controller = loader.getController();
            controller.setFarmer(farmer);
            controller.setViewFarmerController(this);
            controller.setPopupOverlay(overlay);
        } catch (IOException e) {
            e.printStackTrace();
        }
  }

  @FXML
  private void handleBuy() {
    if (contentToBlur != null) {
            contentToBlur.setEffect(new BoxBlur(5, 5, 3));
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/page/farmers/BuyCheesePopup.fxml"
            ));
            AnchorPane popup = loader.load();
            popup.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            StackPane overlay = createOverlay();

            overlay.getChildren().add(popup);
            viewFarmerRoot.getChildren().add(overlay);
    
            BuyCheesePopupController controller = loader.getController();
            controller.setFarmer(farmer);
            controller.setViewFarmerController(this);
            controller.setPopupOverlay(overlay);
        } catch (IOException e) {
            e.printStackTrace();
        }
  }

  private void handleViewCheeseWheel(TOCheeseWheel cheeseWheel) {
    System.out.println("View cheese wheel: " + cheeseWheel.getId());
    // TODO: Navigate to cheese wheel details
  }

  public String deleteFarmer(TOFarmer farmer, StackPane overlay) {
        String error = ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller.deleteFarmer(farmer.getEmail());
        
        if (error == null || error.isEmpty()) {
            // Success - close popup and go back to farmer list
            removePopup(overlay);
            getViewFarmerRoot().fireEvent(new ToastEvent("Farmer deleted successfully.", ToastEvent.ToastType.SUCCESS));
            PageNavigator.getInstance().goBack();
            return "";
        } else {
            // Error - keep popup open and return error message
            return error;
        }
    }


  public void removePopup(StackPane overlay) {
        if (contentToBlur != null) {
            contentToBlur.setEffect(null);
        }
        viewFarmerRoot.getChildren().remove(overlay);
    }

  public void refreshFarmerCard(TOFarmer farmer) {
    if (farmer != null) {
      setFarmer(farmer);
    }
  }

  public void reloadFarmerDetails() {
    if (farmer == null) {
      return;
    }
    TOFarmer refreshed = CheECSEManagerFeatureSet7Controller.getFarmer(farmer.getEmail());
    if (refreshed != null) {
      setFarmer(refreshed);
    }
  }

  private void bindCheeseWheelsToFarmer(String email) {
    ObservableList<TOCheeseWheel> wheels = farmerDataProvider.getCheeseWheelsForFarmer(email);
    cheeseTable.setItems(wheels != null ? wheels : FXCollections.emptyObservableList());
  }
}
