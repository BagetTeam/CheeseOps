package ca.mcgill.ecse.cheecsemanager.fxml.basecontroller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public abstract class BaseDisplayController {

    protected abstract Pane getChildContainer();

    protected FXMLLoader loadFXML(String fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PACKAGE_ID.concat(fxml)));
        try {
            Parent child = loader.load();
            getChildContainer().getChildren().clear();
            getChildContainer().getChildren().add(child);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loader;
    }
}

