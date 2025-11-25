package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.fxml.components.Icon;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ViewFarmerController implements PageNavigator.DataReceiver {

        farmerDescriptionCard.setMaxHeight(Region.USE_PREF_SIZE);
        
        // Set placeholder for empty table
        Label placeholder = new Label("No cheese wheels purchased yet");
        placeholder.setStyle("-fx-text-fill: -color-muted; -fx-font-size: 14px;");
        cheeseTable.setPlaceholder(placeholder);
        
        // Hide empty rows
        cheeseTable.setRowFactory(tv -> {
            javafx.scene.control.TableRow<CheeseWheel> row = new javafx.scene.control.TableRow<>();
            row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    row.setStyle("-fx-background-color: transparent; -fx-opacity: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-min-height: 0;");
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
    // TODO: Edit farmer popup
  }

  @FXML
  private void handleDelete() {
    System.out.println("Delete farmer: " + farmer.getName());
    // TODO: Show confirmation dialog and delete
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
}
