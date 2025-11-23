package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import java.io.IOException;
import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class StyledButton extends Button {
  public enum Variant { DEFAULT, PRIMARY, DESTRUCTIVE, MUTED }
  public enum Size { XS, SM, BASE, MD, LG }

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

  public StyledButton(@NamedArg("variant") Variant variant,
                      @NamedArg("size") Size size,
                      @NamedArg("text") String text) {
    super(text);
    this.initialize();

    this.variant.set(variant.name());
    this.size.set(size.name());
  }

  public void initialize() {
    getStyleClass().add("button");

    this.variant.addListener((observable, oldValue, newValue) -> {
      getStyleClass().removeAll("button-default", "button-primary",
                                "button-destructive", "button-muted");
      getStyleClass().add("button-".concat(this.variant.get().toLowerCase()));
    });

    this.size.addListener((observable, oldValue, newValue) -> {
      getStyleClass().removeAll("button-base", "button-sm", "button-lg",
                                "button-md", "button-xs");
      String sizeClass = "button-".concat(this.size.get().toLowerCase());
      getStyleClass().add(sizeClass);
    });
  }

  private void loadFXML() {
    FXMLLoader loader = new FXMLLoader(CheECSEManagerApplication.getResource(
        "view/components/StyledButton/StyledButton.fxml"));
    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
