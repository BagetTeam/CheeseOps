package ca.mcgill.ecse.cheecsemanager.application;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CheECSEManagerApplication extends Application {
  private static CheECSEManager cheecsemanager;
  public static final String PACKAGE_ID = "/ca/mcgill/ecse/cheecsemanager/";

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(CheECSEManagerApplication.class.getResource(
            PACKAGE_ID.concat("view/page/cheecsemanager/Main.fxml")));
    Scene scene = new Scene(fxmlLoader.load(), 980, 640);
    scene.getStylesheets().add(
        CheECSEManagerApplication
            .class.getResource(PACKAGE_ID.concat("style/main.css"))
            .toExternalForm());
    stage.setTitle("CheECSEManager");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) { launch(); }

  public static CheECSEManager getCheecseManager() {
    if (cheecsemanager == null) {
      // load model from saved file
      cheecsemanager = CheECSEManagerPersistence.load();
    }
    return cheecsemanager;
  }
}
