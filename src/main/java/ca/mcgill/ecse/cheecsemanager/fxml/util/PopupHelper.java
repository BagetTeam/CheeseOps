package ca.mcgill.ecse.cheecsemanager.fxml.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class PopupHelper {

    public static void openSelectionPopup(String title, Parent tempContainer, Class<?> aClass) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(tempContainer, 640, 480);
        scene.getStylesheets().add(aClass.getResource(PACKAGE_ID.concat("style/main.css")).toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
    }
}

