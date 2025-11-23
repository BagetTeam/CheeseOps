package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class StyledButton extends Button {
  public enum Variant { DEFAULT, PRIMARY, DESTRUCTIVE, MUTED }
  public enum Size { BASE, SM, LG, THIN }

  private final StringProperty variant =
      new SimpleStringProperty(Variant.DEFAULT.name());

  private final StringProperty size =
      new SimpleStringProperty(Size.BASE.name());

  public StyledButton() {
    super();
    this.initialize();
  }

  public StyledButton(String text) {
    super(text);
    this.initialize();
  }

  public StyledButton(String text, Node graphic) {
    super(text, graphic);
    this.initialize();
  }

  public void initialize() {
    getStylesheets().add(
        getClass()
            .getResource(CheECSEManagerApplication.PACKAGE_ID.concat(
                "view/components/StyledButton/StyledButton.css"))
            .toExternalForm());

    this.variant.addListener((observable, oldValue, newValue) -> {
      this.setStyle("-fx-background-color: " + this.variant.get() +
                    "; -fx-text-fill: " + this.variant.get());
    });

    this.size.addListener((observable, oldValue, newValue) -> {
      this.setStyle("-fx-font-size: " + this.size.get() +
                    "; -fx-padding: " + this.size.get() + ";");
    });
  }
}
