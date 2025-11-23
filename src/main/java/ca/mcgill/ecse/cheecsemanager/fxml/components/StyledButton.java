package ca.mcgill.ecse.cheecsemanager.fxml.components;

import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 *
 * <p>Custom styled button component</p>
 * <p>Usage:</p>
 *
 * {@code
 * <StyledButton variant="PRIMARY" size="BASE" text="Hello World" />
 * }
 *
 * <p>All variants: DEFAULT, PRIMARY, DESTRUCTIVE, MUTED</p>
 * <p>All sizes: XS, SM, BASE, MD, LG</p>
 *
 *
 * @author Ming Li Liu
 * */
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

  public StyledButton(String text, Node graphic) {
    super(text, graphic);
    this.initialize();
  }
  public StyledButton(@NamedArg("variant") Variant variant,
                      @NamedArg("size") Size size,
                      @NamedArg("text") String text,
                      @NamedArg("graphic") Node graphic) {
    super(text, graphic);

    this.initialize();
    this.ensureIconStyleClass();

    if (variant != null) {
      this.variant.set(variant.name());
    }

    if (size != null) {
      this.size.set(size.name());
    }
  }

  public void initialize() {
    this.getStyleClass().add("button");
    this.getStyleClass().add(
        "button-".concat(this.variant.get().toLowerCase()));
    this.getStyleClass().add("button-".concat(this.size.get().toLowerCase()));

    this.variant.addListener((observable, oldValue, newValue) -> {
      this.getStyleClass().remove("button-".concat(oldValue.toLowerCase()));
      this.getStyleClass().add("button-".concat(newValue.toLowerCase()));
    });

    this.size.addListener((observable, oldValue, newValue) -> {
      this.getStyleClass().remove("button-".concat(oldValue.toLowerCase()));
      this.getStyleClass().add("button-".concat(newValue.toLowerCase()));
    });
  }

  private void ensureIconStyleClass() {
    var graphic = this.graphicProperty().get();
    if (graphic != null && !graphic.getStyleClass().contains("icon")) {
      graphic.getStyleClass().add("icon");
    }
  }
}
