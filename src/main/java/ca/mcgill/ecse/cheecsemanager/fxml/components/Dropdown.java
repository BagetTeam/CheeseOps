package ca.mcgill.ecse.cheecsemanager.fxml.components;

import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * Reusable dropdown component with optional label and variant styling. Shoutout mingli for the inspo!
 *
 * <pre>{@code
 * <Dropdown label="Select Farmer"
 *           labelPosition="TOP"
 *           variant="OUTLINED"
 *           promptText="Choose a farmer..."/>
 * }</pre>
 *
 * @author Benjamin
 */
public class Dropdown extends VBox {
  public enum Variant { DEFAULT, OUTLINED }
  public enum LabelPosition { TOP, LEFT }

  private final ComboBox<String> comboBox = new ComboBox<>();
  private final Label labelNode = new Label();
  private final HBox fieldWrapper = new HBox();
  private final HBox inlineWrapper = new HBox();

  private final StringProperty promptText = new SimpleStringProperty("");
  private final ObjectProperty<Variant> variant =
      new SimpleObjectProperty<>(Variant.DEFAULT);
  private final StringProperty label = new SimpleStringProperty("");
  private final ObjectProperty<LabelPosition> labelPosition =
      new SimpleObjectProperty<>(LabelPosition.TOP);

  private long lastHideTime = 0;

  public Dropdown() {
    super();
    initialize();
  }

  public Dropdown(@NamedArg("items") List<String> items,
                  @NamedArg("promptText") String promptText,
                  @NamedArg("variant") Variant variant,
                  @NamedArg("label") String label,
                  @NamedArg("labelPosition") LabelPosition labelPosition) {
    this();

    if (items != null) {
      setItems(items);
    }

    if (promptText != null) {
      setPromptText(promptText);
    }

    if (variant != null) {
      setVariant(variant);
    }

    if (label != null) {
      setLabel(label);
    }

    if (labelPosition != null) {
      setLabelPosition(labelPosition);
    }
  }

  private void initialize() {
    getStylesheets().add(getClass().getResource("/ca/mcgill/ecse/cheecsemanager/style/Dropdown.css").toExternalForm());
    setAlignment(Pos.CENTER_LEFT);
    setSpacing(4);
    setFillWidth(true);

    fieldWrapper.setAlignment(Pos.CENTER_LEFT);
    fieldWrapper.setSpacing(8);
    fieldWrapper.getStyleClass().add("dropdown");
    fieldWrapper.getStyleClass().add(styleClassForVariant(this.variant.get()));

    inlineWrapper.setAlignment(Pos.CENTER_LEFT);
    inlineWrapper.setSpacing(8);
    inlineWrapper.getStyleClass().add("dropdown-inline");

    labelNode.getStyleClass().add("dropdown-label");
    // labelNode.getStyleClass().add("text-fg");
    labelNode.setVisible(false);
    labelNode.setManaged(false);

    comboBox.getStyleClass().add("dropdown-combobox");
    comboBox.setPromptText(promptText.get());
    comboBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
    comboBox.setMinHeight(Region.USE_PREF_SIZE);
    comboBox.setFocusTraversable(true);
    HBox.setHgrow(comboBox, Priority.ALWAYS);


    promptText.addListener(
        (observable, oldValue, newValue) -> comboBox.setPromptText(newValue));

    variant.addListener((observable, oldVariant, newVariant) -> {
      updateVariantStyle(oldVariant, newVariant);
    });

    label.addListener((observable, oldValue, newValue) -> {
      updateLabelVisibility(newValue);
      refreshLayout();
    });

    labelPosition.addListener(
        (observable, oldValue, newValue) -> { refreshLayout(); });

    javafx.beans.value.ChangeListener<Boolean> focusListener = (obs, oldVal, newVal) ->
        updateFocusState(comboBox.isFocused() || comboBox.isShowing());

    comboBox.focusedProperty().addListener(focusListener);
    comboBox.showingProperty().addListener(focusListener);

    fieldWrapper.getChildren().add(comboBox);

    comboBox.addEventHandler(ComboBox.ON_HIDING, e -> lastHideTime = System.currentTimeMillis());

    // Clicking anywhere on the root should toggle the combobox
    this.setOnMouseClicked(e -> {
      if (System.currentTimeMillis() - lastHideTime < 200) return;
      comboBox.requestFocus();
      if (comboBox.isShowing()) comboBox.hide();
      else comboBox.show();
    });

    updateLabelVisibility(label.get());
    refreshLayout();
  }

  public ComboBox<String> getComboBox() { return comboBox; }

  public final String getSelectedValue() { return comboBox.getValue(); }
  public final void setSelectedValue(String value) { comboBox.setValue(value); }

  public final String getPromptText() { return promptText.get(); }
  public final void setPromptText(String value) {
    promptText.set(value == null ? "" : value);
  }
  public final StringProperty promptTextProperty() { return promptText; }

  public final Variant getVariant() { return variant.get(); }
  public final void setVariant(Variant value) {
    variant.set(value == null ? Variant.DEFAULT : value);
  }
  public final ObjectProperty<Variant> variantProperty() { return variant; }

  public final String getLabel() { return label.get(); }
  public final void setLabel(String value) {
    label.set(value == null ? "" : value);
  }
  public final StringProperty labelProperty() { return label; }

  public final LabelPosition getLabelPosition() { return labelPosition.get(); }
  public final void setLabelPosition(LabelPosition value) {
    labelPosition.set(value == null ? LabelPosition.TOP : value);
  }
  public final ObjectProperty<LabelPosition> labelPositionProperty() {
    return labelPosition;
  }

  public final void setItems(List<String> items) {
    if (items != null) {
      comboBox.setItems(FXCollections.observableArrayList(items));
    }
  }

  public final List<String> getItems() {
    return comboBox.getItems() != null ? new java.util.ArrayList<>(comboBox.getItems())
        : new java.util.ArrayList<>();
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
    return "dropdown-" + resolved.name().toLowerCase();
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

  private void updateFocusState(boolean active) {
    if (active) {
      if (!fieldWrapper.getStyleClass().contains("dropdown-focused")) {
        fieldWrapper.getStyleClass().add("dropdown-focused");
      }

      // If the current configured variant is DEFAULT, temporarily show the outlined
      // variant while the combo is focused. We record this temporary change in the
      // node properties so we can roll it back when focus is lost.
      if (variant.get() == Variant.DEFAULT && !fieldWrapper.getStyleClass().contains("dropdown-outlined")) {
        fieldWrapper.getStyleClass().add("dropdown-outlined");
        fieldWrapper.getProperties().put("tempOutlined", Boolean.TRUE);
      }
    } else {
      fieldWrapper.getStyleClass().remove("dropdown-focused");

      // Remove the temporary outlined variant if we added it on focus.
      Object temp = fieldWrapper.getProperties().remove("tempOutlined");
      if (temp != null) {
        fieldWrapper.getStyleClass().remove("dropdown-outlined");
      }
    }
  }
}
