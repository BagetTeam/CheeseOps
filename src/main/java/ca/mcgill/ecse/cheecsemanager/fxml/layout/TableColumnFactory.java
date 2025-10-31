package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;

import java.util.function.Consumer;
import java.util.function.Function;

public class TableColumnFactory {

    /**
     * Creates a table column with the given title and default width
     * @param title Name of the Object field
     * @param <Type> Type of the Table
     * @param <Element> Type of the Column
     * @return TableColumn<Type, Element>
     */
    public static<Type, Element> TableColumn<Type, Element> createTableColumn(String title) {
        String formattedTitle = FormHelper.convertCamelCaseToWords(title);
        TableColumn<Type, Element> col = new TableColumn<>(formattedTitle);
        col.setPrefWidth(75);
        col.setId(title);
        col.setStyle("-fx-alignment: center;");
        return col;
    }

    /**
     * Adds a column specifically for showing IDs
     * @param columnName
     * @param idExtractor Logic to Extract IDs
     * @return TableColumn<S, S>
     */
    public static <S> TableColumn<S, S> createIdColumn(String columnName, Function<S, String> idExtractor, Consumer<S> action) {
        TableColumn<S, S> column = TableColumnFactory.createTableColumn(columnName);
        column.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
        column.setCellFactory(col -> new IdCell<>(idExtractor, columnName, action));
        return column;
    }

    /**
     * Creates a table column for Checkboxes
     * @param title Name of the Object field
     * @param valueExtractor Logic to Extract Boolean value from the Object
     * @param <Type> Type of the Table
     * @return TableColumn<Type, CheckBox>
     */
    public static<Type> TableColumn<Type, CheckBox> createCheckboxColumn(String title, Function<Type, Boolean> valueExtractor) {
        TableColumn<Type, CheckBox> column = TableColumnFactory.createTableColumn(title);
        column.setCellValueFactory(cell -> {
            CheckBox checkBox = new CheckBox();
            checkBox.setMouseTransparent(true);
            checkBox.setSelected(valueExtractor.apply(cell.getValue())); // Set the value
            return new ReadOnlyObjectWrapper<>(checkBox);
        });
        return column;
    }
}
