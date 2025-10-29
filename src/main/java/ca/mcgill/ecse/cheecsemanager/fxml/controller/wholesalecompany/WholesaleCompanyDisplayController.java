package ca.mcgill.ecse.cheecsemanager.fxml.controller.wholesalecompany;

import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
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

public class WholesaleCompanyDisplayController extends BaseDisplayController implements Initializable {

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
        super.loadFXML("view/page/wholesalecompany/WholesaleCompanyDisplayMany.fxml");
    }

    public void onAdd(ActionEvent event) {
		NavigationState state = new NavigationState<>("Add WholesaleCompany", PageType.ADD, "view/page/wholesalecompany/WholesaleCompanyForm.fxml");
        parentContainer.fireEvent(new PageSwitchEvent(state));
    }

    public <T> void setData(String multiplicity, Map<String, AttributeInfo> scope, T data) {
        buttonAdd.setVisible(false);
        buttonAdd.setManaged(false);
        if (multiplicity.endsWith("*")) {
            WholesaleCompanyDisplayManyController controller = super.loadFXML("view/page/wholesalecompany/WholesaleCompanyDisplayMany.fxml").getController();
            controller.setData(scope, (Supplier<List<TOWholesaleCompany>>) data);
            return;
        }
        if (multiplicity.endsWith("1")) {
            WholesaleCompanyDisplayOneController controller = super.loadFXML("view/page/wholesalecompany/WholesaleCompanyDisplayOne.fxml").getController();
            controller.setData(scope, (Supplier<TOWholesaleCompany>) data);
        }
    }
}
