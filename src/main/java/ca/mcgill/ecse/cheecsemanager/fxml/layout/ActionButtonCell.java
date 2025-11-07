package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ActionButtonCell<S> extends TableCell<S, HBox> {
  private Button updateButton;
  private Consumer<S> updateAction;

  private Button deleteButton;
  private Consumer<S> deleteAction;
  private final HBox hBox = new HBox();

  public ActionButtonCell(Consumer<S> updateAction, Consumer<S> deleteAction) {
    super();
    if (updateAction != null) {
      this.updateButton = new Button("");
      this.updateAction = updateAction;
      Image img = new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/edit.png")));
      ImageView icon = new ImageView(img);
      icon.setFitWidth(16);
      icon.setFitHeight(16);
      this.updateButton.setGraphic(icon);
      this.updateButton.getStyleClass().add("edit-button");
    }

    if (deleteAction != null) {
      this.deleteButton = new Button("");
      this.deleteAction = deleteAction;
      Image img = new Image(getClass().getResourceAsStream(PACKAGE_ID.concat("image/delete.png")));
      ImageView icon = new ImageView(img);
      icon.setFitWidth(16);
      icon.setFitHeight(16);
      this.deleteButton.setGraphic(icon);
      this.deleteButton.getStyleClass().add("delete-button");
    }
  }

  @Override
  protected void updateItem(HBox item, boolean empty) {
    super.updateItem(item, empty);
    if (empty) {
      setGraphic(null);
    } else {
      hBox.getChildren().clear();
      if (updateAction != null) {
        updateButton.setOnAction(e -> {
          S data = getTableRow().getItem();
          if (data != null)
            updateAction.accept(data);
        });
        hBox.getChildren().add(updateButton);
      }
      if (deleteAction != null) {
        deleteButton.setOnAction(e -> {
          S data = getTableRow().getItem();
          if (data != null)
            deleteAction.accept(data);
        });
        hBox.getChildren().add(deleteButton);
      }
      hBox.setSpacing(16);
      hBox.setAlignment(Pos.CENTER);
      setGraphic(hBox);
    }
  }
}
