package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ButtonCell<S> extends TableCell<S, Button> {

    private final Button button;
    private final Consumer<S> action;
    private Predicate<S> disableAction = null;

    public ButtonCell(Consumer<S> action, Predicate<S> disableAction) {
        this.button = new Button("i");
        this.action = action;
        this.disableAction = disableAction;
        this.button.getStyleClass().add("details-button");
    }

    @Override
    protected void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            button.setOnAction(e -> {
                S data  = getTableRow().getItem();
                if(data != null)
                    action.accept(data);
            });
            if (disableAction != null) {
                button.setDisable(disableAction.test(getTableRow().getItem()));
            }
            setGraphic(button);
        }

    }
}
