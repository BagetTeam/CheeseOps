package ca.mcgill.ecse.cheecsemanager.application;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CheECSEManagerApplication extends Application {
  private static CheECSEManager cheecsemanager;
  public static final String PACKAGE_ID = "/ca/mcgill/ecse/cheecsemanager/";

  @Override
  public void start(Stage stage) throws IOException {
    _loadFonts();

    FXMLLoader fxmlLoader =
        new FXMLLoader(CheECSEManagerApplication.class.getResource(
            PACKAGE_ID.concat("view/page/cheecsemanager/Main.fxml")));
    Scene scene = new Scene(fxmlLoader.load(), 980, 640);

    _loadStyleSheets(scene);

    stage.setTitle("CheECSEManager");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) { launch(); }

  public static URL getResource(String path) {
    return CheECSEManagerApplication.class.getResource(PACKAGE_ID.concat(path));
  }

  private static void _loadStyleSheets(Scene scene) {
    String[] sheets = {
        "style/main.css",
        "style/farmer.css",
        "style/utilities.css",
        "style/StyledButton.css",
    };

    for (String sheet : sheets) {
      URL resource =
          CheECSEManagerApplication.class.getResource(PACKAGE_ID.concat(sheet));
      if (resource != null) {
        scene.getStylesheets().add(resource.toExternalForm());
      } else {
        System.err.println("Stylesheet not found: " + sheet);
      }
    }
  }

  private static void _loadFonts() {
    String[] fonts = {
        "Inter_18pt-Black.ttf",        "Inter_18pt-BlackItalic.ttf",
        "Inter_18pt-Bold.ttf",         "Inter_18pt-BoldItalic.ttf",
        "Inter_18pt-ExtraBold.ttf",    "Inter_18pt-ExtraBoldItalic.ttf",
        "Inter_18pt-ExtraLight.ttf",   "Inter_18pt-ExtraLightItalic.ttf",
        "Inter_18pt-Italic.ttf",       "Inter_18pt-Light.ttf",
        "Inter_18pt-LightItalic.ttf",  "Inter_18pt-Medium.ttf",
        "Inter_18pt-MediumItalic.ttf", "Inter_18pt-Regular.ttf",
        "Inter_18pt-SemiBold.ttf",     "Inter_18pt-SemiBoldItalic.ttf",
        "Inter_18pt-Thin.ttf",         "Inter_18pt-ThinItalic.ttf",
    };

    for (String font : fonts) {
      _loadFont(PACKAGE_ID.concat("font/Inter/static/").concat(font));
    }
  }

  private static void _loadFont(String path) {
    Font font = Font.loadFont(
        CheECSEManagerApplication.class.getResourceAsStream(path), 12);

    if (font == null) {
      System.err.println("Failed to load font: " + path);
    }

    // System.out.println("===========================");
    // System.out.println("Loaded font: " + font.getFamily());
  }

  public static CheECSEManager getCheecseManager() {
    if (cheecsemanager == null) {
      // load model from saved file
      cheecsemanager = CheECSEManagerPersistence.load();
    }
    return cheecsemanager;
  }
}
