package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.fxml.components.Icon;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PopupController;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ViewFarmerController extends PopupController implements PageNavigator.DataReceiver{
  @FXML private Button backBtn;

  @FXML private VBox farmerDescriptionCard;

  @FXML private ImageView photoView;
  @FXML private Label nameLabel;
  @FXML private Label emailLabel;
  @FXML private Label addressLabel;
  @FXML private Button updateBtn;
  @FXML private Button deleteBtn;

  @FXML private TableView<CheeseWheel> cheeseTable;
  @FXML private TableColumn<CheeseWheel, Integer> idColumn;
  @FXML private TableColumn<CheeseWheel, String> ageColumn;
  @FXML private TableColumn<CheeseWheel, String> spoiledColumn;
  @FXML private TableColumn<CheeseWheel, String> dateColumn;
  @FXML private TableColumn<CheeseWheel, Void> actionColumn;

  @FXML private AnchorPane viewFarmerRoot;

  private Region contentToBlur;

  private Farmer farmer;

  @FXML
  public void initialize() {
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

    dateColumn.setCellValueFactory(cellData -> {
      Purchase p = cellData.getValue().getPurchase();
      return new SimpleStringProperty(p != null &&
                                              p.getTransactionDate() != null
                                          ? p.getTransactionDate().toString()
                                          : "N/A");
    });

    farmerDescriptionCard.setMaxHeight(Region.USE_PREF_SIZE);

    // Set placeholder for empty table
    Label placeholder = new Label("No cheese wheels purchased yet");
    placeholder.setStyle("-fx-text-fill: -color-muted; -fx-font-size: 14px;");
    cheeseTable.setPlaceholder(placeholder);

    // Hide empty rows
    cheeseTable.setRowFactory(tv -> {
      javafx.scene.control.TableRow<CheeseWheel> row =
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
          CheeseWheel cheeseWheel = getTableView().getItems().get(getIndex());
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

  public void setFarmer(Farmer farmer) {
    this.farmer = farmer;
    if (farmer != null) {
      nameLabel.setText(farmer.getName());
      emailLabel.setText(farmer.getEmail());
      addressLabel.setText(farmer.getAddress());

      // Populate table
      List<CheeseWheel> wheels = new ArrayList<>();
      for (Purchase p : farmer.getPurchases()) {
        wheels.addAll(p.getCheeseWheels());
      }
      ObservableList<CheeseWheel> observableWheels =
          FXCollections.observableArrayList(wheels);
      cheeseTable.setItems(observableWheels);
    }
  }

  @Override
  public void setData(Object data) {
    if (data instanceof Farmer) {
      setFarmer((Farmer)data);
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
    System.out.println("Delete farmer: " + farmer.getName());
    if (farmer.getPurchases().size() > 0) {
        int numCheeseWheels = 0;
        for (Purchase p : farmer.getPurchases()) {
            numCheeseWheels += p.numberOfCheeseWheels();
        }
        if (numCheeseWheels > 0) {
            // TODO Ewen: Implement alert popup
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot delete farmer with purchases.");
            alert.setContentText("The farmer has " + numCheeseWheels + " cheese wheels.");
            alert.showAndWait();
            return;
        }
    }

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
    System.out.println("Buy cheese for farmer: " + farmer.getName());
    // TODO: Navigate to purchase page
  }

  private void handleViewCheeseWheel(CheeseWheel cheeseWheel) {
    System.out.println("View cheese wheel: " + cheeseWheel.getId());
    // TODO: Navigate to cheese wheel details
  }

  public String deleteFarmer(Farmer farmer, StackPane overlay) {
        String error = ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller.deleteFarmer(farmer.getEmail());
        
        if (error == null || error.isEmpty()) {
            // Success - close popup and go back to farmer list
            removePopup(overlay);
            PageNavigator.getInstance().goBack();
            return "";
        } else {
            // Error - keep popup open and return error message
            return error;
        }
    }


  public void removePopup(StackPane overlay) {
        super.removePopup(overlay, viewFarmerRoot);
    }
}
