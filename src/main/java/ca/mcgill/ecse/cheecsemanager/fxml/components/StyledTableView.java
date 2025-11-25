package ca.mcgill.ecse.cheecsemanager.fxml.components;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * A reusable TableView that automatically creates columns from a class
 * and supports optional action buttons at the end of each row.
 *
 * @param <T> The type of items in the table
 */
public class StyledTableView<T> extends TableView<T> {

  private final List<RowAction<T>> rowActions = new ArrayList<>();
  private boolean actionsColumnAdded = false;

  /**
   * Represents an action button for a table row
   */
  public static class RowAction<T> {
    private final String text;
    private final Consumer<T> handler;
    private final String styleClass;

    public RowAction(String text, Consumer<T> handler) {
      this(text, handler, null);
    }

    public RowAction(String text, Consumer<T> handler, String styleClass) {
      this.text = text;
      this.handler = handler;
      this.styleClass = styleClass;
    }

    public String getText() { return text; }

    public Consumer<T> getHandler() { return handler; }

    public String getStyleClass() { return styleClass; }
  }

  /**
   * Creates a SmartTableView that automatically generates columns from the
   * given class
   *
   * @param itemClass The class to introspect for columns
   */
  public StyledTableView(Class<T> itemClass) { this(itemClass, true); }

  /**
   * Creates a SmartTableView that automatically generates columns from the
   * given class
   *
   * @param itemClass The class to introspect for columns
   * @param includeActionsColumn Whether to include the actions column if
   *     actions are added
   */
  public StyledTableView(Class<T> itemClass, boolean includeActionsColumn) {
    setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    generateColumns(itemClass);

    if (includeActionsColumn) {
      // Add actions column placeholder (will be populated when actions are
      // added)
      getColumns().add(createActionsColumn());
    }
  }

  /**
   * Automatically generates columns based on the class fields
   */
  private void generateColumns(Class<T> itemClass) {
    Field[] fields = itemClass.getDeclaredFields();

    for (Field field : fields) {
      // Skip static fields
      if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
        continue;
      }

      String fieldName = field.getName();
      Class<?> fieldType = field.getType();

      // Create column with proper type
      TableColumn<T, ?> column = createColumn(fieldName, fieldType);
      if (column != null) {
        // Convert camelCase to human-readable header
        column.setText(formatColumnHeader(fieldName));
        getColumns().add(column);
      }
    }
  }

  /**
   * Creates a typed column based on the field type
   */
  @SuppressWarnings("unchecked")
  private TableColumn<T, ?> createColumn(String fieldName, Class<?> fieldType) {
    // Handle JavaFX Property types
    if (Property.class.isAssignableFrom(fieldType)) {
      if (fieldType == StringProperty.class) {
        return createTypedColumn(fieldName, String.class);
      } else if (fieldType == IntegerProperty.class ||
                 fieldType == LongProperty.class) {
        return createTypedColumn(fieldName, Number.class);
      } else if (fieldType == DoubleProperty.class ||
                 fieldType == FloatProperty.class) {
        return createTypedColumn(fieldName, Number.class);
      } else if (fieldType == BooleanProperty.class) {
        return createTypedColumn(fieldName, Boolean.class);
      } else if (fieldType == ObjectProperty.class) {
        return createTypedColumn(fieldName, Object.class);
      }
    }
    // Handle primitive and common types
    else {
      if (fieldType == String.class) {
        return createTypedColumn(fieldName, String.class);
      } else if (fieldType == Integer.class || fieldType == int.class ||
                 fieldType == Long.class || fieldType == long.class) {
        return createTypedColumn(fieldName, Number.class);
      } else if (fieldType == Double.class || fieldType == double.class ||
                 fieldType == Float.class || fieldType == float.class) {
        return createTypedColumn(fieldName, Number.class);
      } else if (fieldType == Boolean.class || fieldType == boolean.class) {
        return createTypedColumn(fieldName, Boolean.class);
      }
    }

    // Default to Object type for unknown types
    return createTypedColumn(fieldName, Object.class);
  }

  /**
   * Creates a typed column with the appropriate cell value factory
   */
  private <S> TableColumn<T, S> createTypedColumn(String fieldName,
                                                  Class<S> type) {
    TableColumn<T, S> column = new TableColumn<>();
    column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
    return column;
  }

  /**
   * Creates the actions column with buttons
   */
  private TableColumn<T, Void> createActionsColumn() {
    TableColumn<T, Void> actionsColumn = new TableColumn<>("Actions");
    actionsColumn.setSortable(false);
    actionsColumn.setCellFactory(createActionsCellFactory());
    actionsColumn.setVisible(!rowActions.isEmpty());
    return actionsColumn;
  }

  /**
   * Creates a cell factory for rendering action buttons
   */
  private Callback<TableColumn<T, Void>, TableCell<T, Void>>
  createActionsCellFactory() {
    return param -> new TableCell<>() {
      private final HBox buttonContainer = new HBox(5);

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || rowActions.isEmpty()) {
          setGraphic(null);
        } else {
          buttonContainer.getChildren().clear();

          // Create buttons for each action
          for (RowAction<T> action : rowActions) {
            Button button = new Button(action.getText());
            button.setOnAction(event -> {
              T rowData = getTableView().getItems().get(getIndex());
              action.getHandler().accept(rowData);
            });

            // Apply style class if provided
            if (action.getStyleClass() != null) {
              button.getStyleClass().add(action.getStyleClass());
            }

            buttonContainer.getChildren().add(button);
          }

          setGraphic(buttonContainer);
        }
      }
    };
  }

  /**
   * Adds an action button to each row
   *
   * @param text The button text
   * @param handler The handler to call when button is clicked
   */
  public void addRowAction(String text, Consumer<T> handler) {
    rowActions.add(new RowAction<>(text, handler));
    updateActionsColumn();
  }

  /**
   * Adds an action button with a CSS style class
   *
   * @param text The button text
   * @param handler The handler to call when button is clicked
   * @param styleClass The CSS style class for the button
   */
  public void addRowAction(String text, Consumer<T> handler,
                           String styleClass) {
    rowActions.add(new RowAction<>(text, handler, styleClass));
    updateActionsColumn();
  }

  /**
   * Updates the actions column visibility
   */
  private void updateActionsColumn() {
    // Find actions column (should be the last one)
    if (!getColumns().isEmpty()) {
      TableColumn<T, ?> lastColumn = getColumns().get(getColumns().size() - 1);
      if ("Actions".equals(lastColumn.getText())) {
        lastColumn.setVisible(!rowActions.isEmpty());
      }
    }
  }

  /**
   * Formats a camelCase field name into a human-readable header
   */
  private String formatColumnHeader(String fieldName) {
    StringBuilder result = new StringBuilder();
    for (char c : fieldName.toCharArray()) {
      if (Character.isUpperCase(c)) {
        result.append(' ');
      }
      result.append(c);
    }
    // Capitalize first letter
    String header = result.toString();
    if (!header.isEmpty()) {
      header = Character.toUpperCase(header.charAt(0)) + header.substring(1);
    }
    return header;
  }
}
