package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class ViewFarmerController implements PageNavigator.DataReceiver {
    
    @FXML private Button backBtn;

    @FXML private ImageView photoView;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;

    @FXML private TableView<CheeseWheel> cheeseTable;
    @FXML private TableColumn<CheeseWheel, Integer> idColumn;
    @FXML private TableColumn<CheeseWheel, String> ageColumn;
    @FXML private TableColumn<CheeseWheel, String> spoiledColumn;
    @FXML private TableColumn<CheeseWheel, String> dateColumn;
    @FXML private TableColumn<CheeseWheel, Void> actionColumn;

    private Farmer farmer;

    @FXML
    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        
        ageColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getMonthsAged().toString()));
            
        spoiledColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getIsSpoiled() ? "Yes" : "No"));
            
        dateColumn.setCellValueFactory(cellData -> {
            Purchase p = cellData.getValue().getPurchase();
            return new SimpleStringProperty(p != null && p.getTransactionDate() != null ? p.getTransactionDate().toString() : "N/A");
        });

        // Add View Button column
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<CheeseWheel, Void> call(final TableColumn<CheeseWheel, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("View");

                    {
                        btn.setOnAction(event -> {
                            CheeseWheel cheeseWheel = getTableView().getItems().get(getIndex());
                            System.out.println("View cheese wheel: " + cheeseWheel.getId());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
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
            ObservableList<CheeseWheel> observableWheels = FXCollections.observableArrayList(wheels);
            cheeseTable.setItems(observableWheels);
        }
    }
    
    @Override
    public void setData(Object data) {
        if (data instanceof Farmer) {
            setFarmer((Farmer) data);
        }
    }
    
    @FXML
    private void handleBack() {
        PageNavigator.getInstance().goBack();
    }
}

