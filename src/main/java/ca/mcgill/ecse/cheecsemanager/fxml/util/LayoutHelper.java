package ca.mcgill.ecse.cheecsemanager.fxml.util;

import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.*;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

/**
 * Helper Methods to manipulate the Table / Grid layout
 */
public class LayoutHelper {

    /**
     * 
     * @param table TableView
     * @param scope Hashmap of attributes and scopes
     * @param <T> Table Type (Can be ignored)
     * Sorts the Table column in the order specified in the Scope map
     * Items with -1 order are added at last
     */
    public static <T> void sortTableColumns(TableView<T> table, Map<String, AttributeInfo> scope) {
        ObservableList<TableColumn<T, ?>> columns = table.getColumns();
        columns.sort(Comparator.comparingInt(col -> {
            // If index (Order) is -1 then move it to the end
            int order = scope.getOrDefault(col.getText(), new AttributeInfo("NA", -1)).getOrder();
            return order < 0 ? Integer.MAX_VALUE : order;
        }));
    }

    /**
     *
     * @param gridPane Grid which has been rendered on the screen
     * @param scope Hashmap of attributes and scopes
     * @return Next empty row
     *
     * Takes GridPane and scope list. Rearranges the Rows in the order defined in scope list
     */
    public static int sortGrid(GridPane gridPane, Map<String, AttributeInfo> scope) {
        ObservableList<Node> children = gridPane.getChildren();

        Map<String, List<Node>> keyToNodes = new HashMap<>();

        // Adds key as column names and values as Nodes (2 Nodes for each key i.e., label (0) and formfiled (1))
        for (Node node : children) {
            String id = node.getId();
            if (id == null || !id.contains(",")) continue;

            // IDs are in format "keyName, colIndex"
            String[] parts = id.split(",");
            String key = parts[0].trim();

            keyToNodes.computeIfAbsent(key, k -> new ArrayList<>()).add(node);
        }

        // Sort the keys based on their priority order
        List<String> sortedKeys = keyToNodes.keySet().stream()
                .sorted(Comparator.comparingInt(key -> scope.getOrDefault(key, new AttributeInfo("NA", Integer.MAX_VALUE)).getOrder()))
                .toList();

        // Assign new Rows and Columns to the nodes
        int currentRow = 0;
        for (String key : sortedKeys) {
            for (Node node : keyToNodes.get(key)) {
                String[] parts = node.getId().split(",");

                GridPane.setColumnIndex(node, Integer.parseInt(parts[1].trim()));
                GridPane.setRowIndex(node, currentRow);
            }
            currentRow++; // move to next row for the next group
        }

        return currentRow;
    }

    /**
     * 
     * @param table TableView with all columns added
     * Workaround to resize the Columns
     * Fixes an issue where the first column becomes too large when the table is refreshed
     */
    public static void refreshColumnWidths(TableView<?> table) {
            double totalWidth = table.getWidth();
            int totalColumns = table.getColumns().size();
            double perColumnWidth = totalWidth / totalColumns;

            for (TableColumn<?, ?> column : table.getColumns()) {
                column.setPrefWidth(perColumnWidth);
            }
            table.requestLayout(); // force relayout

            // If there are more than 6 columns, set a fixed width to avoid stretching and show a scrollbar
            if (table.getColumns().size() <= 6) return;

            table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            table.getColumns().forEach(col -> col.setPrefWidth(132));
    }

    /**
     *
     * @param aClass Class through which the image is loaded
     * @return Edit Button with an icon
     */
    public static Button createGridEditButton(Class <?> aClass) {
        Button updateButton = new Button();
        Image img = new Image(aClass.getResourceAsStream(PACKAGE_ID.concat("image/edit.png")));
        ImageView icon = new ImageView(img);
        icon.setFitWidth(16);
        icon.setFitHeight(16);
        updateButton.setGraphic(icon);
        updateButton.getStyleClass().add("edit-button");
        return updateButton;
    }
}
