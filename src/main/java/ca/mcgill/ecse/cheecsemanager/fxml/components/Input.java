package ca.mcgill.ecse.cheecsemanager.fxml.components;

import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * Reusable input control with optional leading icon and variant styling.
 *
 * <pre>{@code
 * <Input placeholder="Search shelves" variant="OUTLINED">
 *   <icon>
 *     <Icon icon="Search" />
 *   </icon>
 * </Input>
 * }</pre>
 */
public class Input extends HBox {
  public enum Variant { DEFAULT, OUTLINED }

  private final TextField textField = new TextField();
  private final StackPane iconContainer = new StackPane();

  private final ObjectProperty<Node> icon = new SimpleObjectProperty<>();
  private final ObjectProperty<Variant> variant =
      new SimpleObjectProperty<>(Variant.DEFAULT);
  private final StringProperty placeholder = new SimpleStringProperty("");

  public Input() {
    super();
    initialize();
  }

  public Input(@NamedArg("placeholder") String placeholder,
               @NamedArg("icon") Node icon,
               @NamedArg("variant") Variant variant) {
    this();
    if (placeholder != null) {
      setPlaceholder(placeholder);
    }
    if (icon != null) {
      setIcon(icon);
    }
    if (variant != null) {
      setVariant(variant);
    }
  }

  private void initialize() {
    setAlignment(Pos.CENTER_LEFT);
    setSpacing(8);
    getStyleClass().add("input");
    getStyleClass().add(styleClassForVariant(variant.get()));

    textField.getStyleClass().add("input-text-field");
    textField.promptTextProperty().bind(placeholder);
    textField.setPrefColumnCount(10);
    textField.setMaxWidth(Double.MAX_VALUE);
    textField.disableProperty().bind(disableProperty());
    textField.focusedProperty().addListener((observable, oldValue, isFocused) -> {
      if (isFocused) {
        if (!getStyleClass().contains("input-focused")) {
          getStyleClass().add("input-focused");
        }
      } else {
        getStyleClass().remove("input-focused");
      }
    });
    HBox.setHgrow(textField, Priority.ALWAYS);

    iconContainer.getStyleClass().add("input-icon");
    icon.addListener((observable, oldNode, newNode) -> renderIcon(newNode));
    variant.addListener((observable, oldVariant, newVariant) -> {
      getStyleClass().remove(styleClassForVariant(oldVariant));
      getStyleClass().add(styleClassForVariant(newVariant));
    });

    getChildren().add(textField);
  }

  private void renderIcon(Node node) {
    if (node == null) {
      getChildren().remove(iconContainer);
      iconContainer.getChildren().clear();
      return;
    }

    if (!node.getStyleClass().contains("icon")) {
      node.getStyleClass().add("icon");
    }

    iconContainer.getChildren().setAll(node);
    if (!getChildren().contains(iconContainer)) {
      getChildren().add(0, iconContainer);
    }
  }

  private static String styleClassForVariant(Variant variant) {
    Variant resolved = (variant == null) ? Variant.DEFAULT : variant;
    return "input-".concat(resolved.name().toLowerCase());
  }

  public TextField getTextField() { return textField; }
  public StringProperty textProperty() { return textField.textProperty(); }

  public final void setIcon(Node value) { icon.set(value); }
  public final Node getIcon() { return icon.get(); }
  public final ObjectProperty<Node> iconProperty() { return icon; }

  public final void setVariant(Variant value) {
    variant.set(value == null ? Variant.DEFAULT : value);
  }
  public final Variant getVariant() { return variant.get(); }
  public final ObjectProperty<Variant> variantProperty() { return variant; }

  public final void setPlaceholder(String value) { placeholder.set(value); }
  public final String getPlaceholder() { return placeholder.get(); }
  public final StringProperty placeholderProperty() { return placeholder; }
}
