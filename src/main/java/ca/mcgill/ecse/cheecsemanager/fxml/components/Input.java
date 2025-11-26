package ca.mcgill.ecse.cheecsemanager.fxml.components;

import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Reusable input component with optional icon and variant styling.
 *
 * <pre>{@code
 * <Input placeholder="Search cheese" variant="OUTLINED">
 *   <icon>
 *     <Icon icon="Search"/>
 *   </icon>
 * </Input>
 * }</pre>
 */
public class Input extends HBox {
  public enum Variant { DEFAULT, OUTLINED }

  private final TextField textField = new TextField();
  private final StackPane iconContainer = new StackPane();

  private final StringProperty placeholder = new SimpleStringProperty("");
  private final ObjectProperty<Node> icon = new SimpleObjectProperty<>();
  private final ObjectProperty<Variant> variant = new SimpleObjectProperty<>(Variant.DEFAULT);

  public Input() {
    super();
    initialize();
  }

  public Input(@NamedArg("placeholder") String placeholder, @NamedArg("variant") Variant variant,
      @NamedArg("icon") Node icon) {
    this();

    if (placeholder != null) {
      setPlaceholder(placeholder);
    }

    if (variant != null) {
      setVariant(variant);
    }

    if (icon != null) {
      setIcon(icon);
    }
  }

  private void initialize() {
    setAlignment(Pos.CENTER_LEFT);
    setSpacing(8);
    setFocusTraversable(false);
    getStyleClass().add("input");
    getStyleClass().add(styleClassForVariant(this.variant.get()));

    iconContainer.getStyleClass().add("input-icon");
    iconContainer.setManaged(false);

    textField.getStyleClass().add("input-field");
    textField.setPromptText(placeholder.get());
    textField.setPrefHeight(Region.USE_COMPUTED_SIZE);
    textField.setMinHeight(Region.USE_PREF_SIZE);
    textField.setFocusTraversable(true);
    HBox.setHgrow(textField, Priority.ALWAYS);

    placeholder.addListener((observable, oldValue, newValue) -> textField.setPromptText(newValue));

    variant.addListener(
        (observable, oldVariant, newVariant) -> { updateVariantStyle(oldVariant, newVariant); });

    icon.addListener((observable, oldIcon, newIcon) -> refreshIcon(newIcon));

    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        if (!getStyleClass().contains("input-focused")) {
          getStyleClass().add("input-focused");
        }
      } else {
        getStyleClass().remove("input-focused");
      }
    });

    getChildren().add(textField);
    refreshIcon(icon.get());
  }

  public TextField getTextField() {
    return textField;
  }

  public final String getText() {
    return textProperty().get();
  }
  public final void setText(String value) {
    textProperty().set(value);
  }
  public final StringProperty textProperty() {
    return textField.textProperty();
  }

  public final String getPlaceholder() {
    return placeholder.get();
  }
  public final void setPlaceholder(String value) {
    placeholder.set(value == null ? "" : value);
  }
  public final StringProperty placeholderProperty() {
    return placeholder;
  }

  public final Variant getVariant() {
    return variant.get();
  }
  public final void setVariant(Variant value) {
    variant.set(value == null ? Variant.DEFAULT : value);
  }
  public final ObjectProperty<Variant> variantProperty() {
    return variant;
  }

  public final Node getIcon() {
    return icon.get();
  }
  public final void setIcon(Node value) {
    icon.set(value);
  }
  public final ObjectProperty<Node> iconProperty() {
    return icon;
  }

  private void refreshIcon(Node node) {
    iconContainer.getChildren().clear();

    if (node == null) {
      getChildren().remove(iconContainer);
      iconContainer.setManaged(false);
      return;
    }

    if (node.getParent() instanceof Pane pane) {
      pane.getChildren().remove(node);
    } else if (node.getParent() instanceof Group group) {
      group.getChildren().remove(node);
    }

    iconContainer.getChildren().add(node);
    iconContainer.setManaged(true);

    if (!getChildren().contains(iconContainer)) {
      getChildren().add(0, iconContainer);
    }
  }

  private void updateVariantStyle(Variant oldVariant, Variant newVariant) {
    if (oldVariant != null) {
      getStyleClass().remove(styleClassForVariant(oldVariant));
    }

    if (newVariant != null) {
      String newClass = styleClassForVariant(newVariant);
      if (!getStyleClass().contains(newClass)) {
        getStyleClass().add(newClass);
      }
    }
  }

  private String styleClassForVariant(Variant value) {
    Variant resolved = value == null ? Variant.DEFAULT : value;
    return "input-" + resolved.name().toLowerCase();
  }
}
