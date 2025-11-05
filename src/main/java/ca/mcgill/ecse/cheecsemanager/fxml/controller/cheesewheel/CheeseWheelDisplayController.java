package ca.mcgill.ecse.cheecsemanager.fxml.controller.cheesewheel;

import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseDisplayController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class CheeseWheelDisplayController extends BaseDisplayController implements Initializable {

    @FXML
    public VBox parentContainer, childContainer;
    @FXML
    private Button buttonAdd;


    @Override
    protected Pane getChildContainer() {
        return childContainer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.loadFXML("view/page/cheesewheel/CheeseWheelDisplayMany.fxml");
    }

    public void onAdd(ActionEvent event) {
		NavigationState state = new NavigationState<>("Add CheeseWheel", PageType.ADD, "view/page/cheesewheel/CheeseWheelForm.fxml");
        parentContainer.fireEvent(new PageSwitchEvent(state));
    }

    public <T> void setData(String multiplicity, Map<String, AttributeInfo> scope, T data) {
        buttonAdd.setVisible(false);
        buttonAdd.setManaged(false);
        if (multiplicity.endsWith("*")) {
            CheeseWheelDisplayManyController controller = super.loadFXML("view/page/cheesewheel/CheeseWheelDisplayMany.fxml").getController();
            controller.setData(scope, (Supplier<List<TOCheeseWheel>>) data);
            return;
        }
        if (multiplicity.endsWith("1")) {
            CheeseWheelDisplayOneController controller = super.loadFXML("view/page/cheesewheel/CheeseWheelDisplayOne.fxml").getController();
            controller.setData(scope, (Supplier<TOCheeseWheel>) data);
        }
    }
}
