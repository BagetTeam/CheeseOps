package ca.mcgill.ecse.cheecsemanager.fxml.controller.farmer;

import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
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

public class FarmerDisplayController extends BaseDisplayController implements Initializable {

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
        super.loadFXML("view/page/farmer/FarmerDisplayMany.fxml");
    }

    public void onAdd(ActionEvent event) {
		NavigationState state = new NavigationState<>("Add Farmer", PageType.ADD, "view/page/farmer/FarmerForm.fxml");
        parentContainer.fireEvent(new PageSwitchEvent(state));
    }

    public <T> void setData(String multiplicity, Map<String, AttributeInfo> scope, T data) {
        buttonAdd.setVisible(false);
        buttonAdd.setManaged(false);
        if (multiplicity.endsWith("*")) {
            FarmerDisplayManyController controller = super.loadFXML("view/page/farmer/FarmerDisplayMany.fxml").getController();
            controller.setData(scope, (Supplier<List<TOFarmer>>) data);
            return;
        }
        if (multiplicity.endsWith("1")) {
            FarmerDisplayOneController controller = super.loadFXML("view/page/farmer/FarmerDisplayOne.fxml").getController();
            controller.setData(scope, (Supplier<TOFarmer>) data);
        }
    }
}
