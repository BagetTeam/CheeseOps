package ca.mcgill.ecse.cheecsemanager.fxml.components;

import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Reusable input component with optional icon, label, and variant styling.
 *
 * <pre>{@code
 * <Input placeholder="Search cheese"
 *        label="Search"
 *        labelPosition="TOP"
 *        variant="OUTLINED">
 *   <icon>
 *     <Icon icon="Search"/>
 *   </icon>
 * </Input>
 * }</pre>
 */
public class Input extends VBox {
  public enum Variant { DEFAULT, OUTLINED }
  public enum LabelPosition { TOP, LEFT }

  private final TextField textField = new TextField();
  private final StackPane iconContainer = new StackPane();
  private final HBox fieldWrapper = new HBox();
  private final Label labelNode = new Label();
  private final HBox inlineWrapper = new HBox();

  private final StringProperty placeholder = new SimpleStringProperty("");
  private final ObjectProperty<Node> icon = new SimpleObjectProperty<>();
  private final ObjectProperty<Variant> variant = new SimpleObjectProperty<>(Variant.DEFAULT);
  private final StringProperty label = new SimpleStringProperty("");
  private final ObjectProperty<LabelPosition> labelPosition =
      new SimpleObjectProperty<>(LabelPosition.TOP);

  public Input() {
    super();
    initialize();
  }

  public Input(@NamedArg("placeholder") String placeholder, @NamedArg("variant") Variant variant,
      @NamedArg("icon") Node icon, @NamedArg("label") String label,
      @NamedArg("labelPosition") LabelPosition labelPosition) {
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

    if (label != null) {
      setLabel(label);
    }

    if (labelPosition != null) {
      setLabelPosition(labelPosition);
    }
  }

  private void initialize() {
    setAlignment(Pos.CENTER_LEFT);
    setSpacing(4);
    setFillWidth(true);

    fieldWrapper.setAlignment(Pos.CENTER_LEFT);
    fieldWrapper.setSpacing(8);
    fieldWrapper.getStyleClass().add("input");
    fieldWrapper.getStyleClass().add(styleClassForVariant(this.variant.get()));

    inlineWrapper.setAlignment(Pos.CENTER_LEFT);
    inlineWrapper.setSpacing(8);
    inlineWrapper.getStyleClass().add("input-inline");

    iconContainer.getStyleClass().add("input-icon");
    iconContainer.setManaged(false);

    labelNode.getStyleClass().add("input-label");
    labelNode.setVisible(false);
    labelNode.setManaged(false);

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

    label.addListener((observable, oldValue, newValue) -> {
      updateLabelVisibility(newValue);
      refreshLayout();
    });

    labelPosition.addListener((observable, oldValue, newValue) -> { refreshLayout(); });

    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        if (!fieldWrapper.getStyleClass().contains("input-focused")) {
          fieldWrapper.getStyleClass().add("input-focused");
        }
      } else {
        fieldWrapper.getStyleClass().remove("input-focused");
      }
    });

    fieldWrapper.getChildren().add(textField);
    refreshIcon(icon.get());
    updateLabelVisibility(label.get());
    refreshLayout();
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

  public final String getLabel() {
    return label.get();
  }
  public final void setLabel(String value) {
    label.set(value == null ? "" : value);
  }
  public final StringProperty labelProperty() {
    return label;
  }

  public final LabelPosition getLabelPosition() {
    return labelPosition.get();
  }
  public final void setLabelPosition(LabelPosition value) {
    labelPosition.set(value == null ? LabelPosition.TOP : value);
  }
  public final ObjectProperty<LabelPosition> labelPositionProperty() {
    return labelPosition;
  }

  private void refreshIcon(Node node) {
    iconContainer.getChildren().clear();

    if (node == null) {
      fieldWrapper.getChildren().remove(iconContainer);
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

    if (!fieldWrapper.getChildren().contains(iconContainer)) {
      fieldWrapper.getChildren().add(0, iconContainer);
    }
  }

  private void updateVariantStyle(Variant oldVariant, Variant newVariant) {
    if (oldVariant != null) {
      fieldWrapper.getStyleClass().remove(styleClassForVariant(oldVariant));
    }

    if (newVariant != null) {
      String newClass = styleClassForVariant(newVariant);
      if (!fieldWrapper.getStyleClass().contains(newClass)) {
        fieldWrapper.getStyleClass().add(newClass);
      }
    }
  }

  private String styleClassForVariant(Variant value) {
    Variant resolved = value == null ? Variant.DEFAULT : value;
    return "input-" + resolved.name().toLowerCase();
  }

  private void updateLabelVisibility(String value) {
    boolean hasLabel = value != null && !value.isBlank();
    labelNode.setText(hasLabel ? value : "");
    labelNode.setVisible(hasLabel);
    labelNode.setManaged(hasLabel);
  }

  private void refreshLayout() {
    detachFromParent(fieldWrapper);
    detachFromParent(labelNode);
    detachFromParent(inlineWrapper);

    getChildren().clear();
    boolean hasLabel = labelNode.isVisible();

    if (hasLabel && labelPosition.get() == LabelPosition.LEFT) {
      inlineWrapper.getChildren().setAll(labelNode, fieldWrapper);
      getChildren().add(inlineWrapper);
    } else {
      if (hasLabel) {
        getChildren().add(labelNode);
      }
      getChildren().add(fieldWrapper);
    }
  }

  private void detachFromParent(Node node) {
    if (node.getParent() instanceof Pane pane) {
      pane.getChildren().remove(node);
    }
  }
}
