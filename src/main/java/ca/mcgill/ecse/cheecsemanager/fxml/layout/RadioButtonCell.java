package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleGroup;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class RadioButtonCell<S> extends TableCell<S, RadioButton> {

    private final RadioButton button;
    private final Consumer<S> action;
    private S preselectionValue;
    private BiPredicate<S, S> preselectionCondition;

    public RadioButtonCell(ToggleGroup group, Consumer<S> action) {
        this.button = new RadioButton();
        button.setToggleGroup(group);
        this.action = action;
    }

    public RadioButtonCell(ToggleGroup group, Consumer<S> action, BiPredicate<S, S> preselectionCondition, S preselectionValue) {
        this.button = new RadioButton();
        button.setToggleGroup(group);
        this.action = action;
        this.preselectionCondition = preselectionCondition;
        this.preselectionValue = preselectionValue;
    }

    @Override
    protected void updateItem(RadioButton radioButton, boolean empty) {
        super.updateItem(radioButton, empty);
        if (empty) {
            setGraphic(null);
        } else {
            button.setOnAction(e -> {
                S data = getTableRow().getItem();
                if (data != null) {
                    action.accept(data);
                }
            });
            // Handle Preselection
            boolean notNull = getTableRow().getItem() != null && preselectionValue != null && preselectionCondition != null;
            boolean isPreselected = notNull && preselectionCondition.test(getTableRow().getItem(), preselectionValue);
            if (isPreselected) {
                button.setSelected(true);
            }
            setGraphic(button);
        }
    }
}
