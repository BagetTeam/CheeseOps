package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class CheckBoxCell <S> extends TableCell<S, CheckBox> {

    private final CheckBox box;
    private final BiConsumer<S, Boolean> action;
    private final Predicate<S> mappedToSource;

    public CheckBoxCell(BiConsumer<S, Boolean> action, Predicate<S> mappedToSource) {
        this.box = new CheckBox();
        this.action = action;
        this.mappedToSource = mappedToSource;
    }

    @Override
    protected void updateItem(CheckBox checkBox, boolean empty) {
        super.updateItem(checkBox, empty);
        if (empty) {
            setGraphic(null);
        } else {
            box.setOnAction(e -> {
                S data = getTableRow().getItem();
                if (data != null) {
                    action.accept(data, box.isSelected());
                }

            });
            S data = getTableRow().getItem();
            // Prefill for current Object
            if (mappedToSource != null && mappedToSource.test(data)) {
                box.setSelected(true);
            }
            setGraphic(box);
        }
    }
}
